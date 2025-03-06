package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import net.risingworld.api.Plugin;
import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.UITextField;
import net.risingworld.api.ui.style.ScaleMode;
import net.risingworld.api.ui.style.TextAnchor;
import net.risingworld.api.definitions.Definitions;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess2;

/**
 * Handles logic for navigating and searching market listings in the Online Market.
 */
public class MarketNavigationControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketNavigationControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to searching, paginating, viewing market listings, and selecting items.
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void handleNavigationEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        String PlayerUID = player.getUID();
        int eventID = event.getUIElement().getID();

        int OnlineMarketSearchButtonID = (int) player.getAttribute("OnlineMarketSearchButtonID");
        int OnlineMarketNextButtonID = (int) player.getAttribute("OnlineMarketNextButtonID");
        int OnlineMarketBackButtonID = (int) player.getAttribute("OnlineMarketBackButtonID");
        int OnlineMarketMyListingsButtonID = (int) player.getAttribute("OnlineMarketMyListingsButtonID");
        int OnlineMarketMarketListingsButtonID = (int) player.getAttribute("OnlineMarketMarketListingsButtonID");
        int OnlineMarketAllListingsButtonID = (int) player.getAttribute("OnlineMarketAllListingsButtonID");

        UILabel OnlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");
        @SuppressWarnings("unchecked")
        ArrayList<UILabel> OnlineMarketSelectBottons = (ArrayList<UILabel>) player.getAttribute("OnlineMarketSelectBottons");
        @SuppressWarnings("unchecked")
        ArrayList<String> OnlineMarketSelected = (ArrayList<String>) player.getAttribute("OnlineMarketSelected");
        ArrayList<UILabel> CopyOnlineMarketSelected = new ArrayList<>(OnlineMarketSelectBottons);
        UITextField OnlineMarketID = (UITextField) player.getAttribute("OnlineMarketID");

        // Handle searching market listings
        if (eventID == OnlineMarketSearchButtonID) {
            OnlineMarketTextField.setText("");
            clearListingDisplay(CopyOnlineMarketSelected, OnlineMarketSelectBottons, OnlineMarketSelected);

            OnlineMarketID.getCurrentText(player, (String OnlineMarketIDText) -> {
                player.setAttribute("OnlineMarketIDText", OnlineMarketIDText);
                String searchQuery = "SELECT count(*) FROM `MarketListings` WHERE ItemName LIKE '" + OnlineMarketIDText + "%' OR ItemName LIKE '%" + OnlineMarketIDText + "' OR ItemName LIKE '%" + OnlineMarketIDText + "%' OR ItemPrice LIKE '" + OnlineMarketIDText + "%' OR ItemPrice LIKE '%" + OnlineMarketIDText + "' OR ItemPrice LIKE '%" + OnlineMarketIDText + "%' OR ItemOwnerName LIKE '" + OnlineMarketIDText + "%' OR ItemOwnerName LIKE '%" + OnlineMarketIDText + "' OR ItemOwnerName LIKE '%" + OnlineMarketIDText + "%'";
                String searchResultQuery = "MarketListings WHERE ItemName LIKE '" + OnlineMarketIDText + "%' OR ItemName LIKE '%" + OnlineMarketIDText + "' OR ItemName LIKE '%" + OnlineMarketIDText + "%' OR ItemPrice LIKE '" + OnlineMarketIDText + "%' OR ItemPrice LIKE '%" + OnlineMarketIDText + "' OR ItemPrice LIKE '%" + OnlineMarketIDText + "%' OR ItemOwnerName LIKE '" + OnlineMarketIDText + "%' OR ItemOwnerName LIKE '%" + OnlineMarketIDText + "' OR ItemOwnerName LIKE '%" + OnlineMarketIDText + "%'";
                player.setAttribute("Search", searchQuery);
                player.setAttribute("SearchRsult", searchResultQuery);

                int PageNumber = 1;
                player.setAttribute("OnlineMarketPageNumber", PageNumber);

                displayListings(player, OnlineMarketTextField, OnlineMarketSelectBottons, OnlineMarketSelected);
            });
        }

        // Handle moving to the next page of listings
        if (eventID == OnlineMarketNextButtonID) {
            OnlineMarketTextField.setText("");
            clearListingDisplay(CopyOnlineMarketSelected, OnlineMarketSelectBottons, OnlineMarketSelected);

            int PageNumber = (int) player.getAttribute("OnlineMarketPageNumber") + 1;
            player.setAttribute("OnlineMarketPageNumber", PageNumber);

            displayListings(player, OnlineMarketTextField, OnlineMarketSelectBottons, OnlineMarketSelected);
        }

        // Handle moving to the previous page of listings
        if (eventID == OnlineMarketBackButtonID) {
            OnlineMarketTextField.setText("");
            clearListingDisplay(CopyOnlineMarketSelected, OnlineMarketSelectBottons, OnlineMarketSelected);

            int PageNumber = Math.max(1, (int) player.getAttribute("OnlineMarketPageNumber") - 1);
            player.setAttribute("OnlineMarketPageNumber", PageNumber);

            displayListings(player, OnlineMarketTextField, OnlineMarketSelectBottons, OnlineMarketSelected);
        }

        // Handle viewing player's own listings
        if (eventID == OnlineMarketMyListingsButtonID) {
            player.setAttribute("Search", "SELECT count(*) FROM `MarketListings` WHERE ItemOwnerUID = '" + PlayerUID + "'");
            player.setAttribute("SearchRsult", "MarketListings WHERE ItemOwnerUID = '" + PlayerUID + "'");
            menuControl.getOpenOnlineMarket().OpenMenu(player);
        }

        // Handle viewing server (unlimited) listings
        if (eventID == OnlineMarketMarketListingsButtonID) {
            player.setAttribute("Search", "SELECT count(*) FROM `MarketListings` WHERE IsUnlimited = 1");
            player.setAttribute("SearchRsult", "MarketListings WHERE IsUnlimited = 1");
            menuControl.getOpenOnlineMarket().OpenMenu(player);
        }

        // Handle viewing all listings
        if (eventID == OnlineMarketAllListingsButtonID) {
            player.setAttribute("Search", "SELECT count(*) FROM `MarketListings`");
            player.setAttribute("SearchRsult", "MarketListings");
            menuControl.getOpenOnlineMarket().OpenMenu(player);
        }

        // Handle clicking an OnlineMarketSelect button to open the buy popup
        for (String selected : OnlineMarketSelected) {
            if (selected != null) {
                String[] parts = selected.split(",", 2);
                if (parts.length == 2) {
                    int buttonID = Integer.parseInt(parts[0]);
                    String itemID = parts[1];
                    if (eventID == buttonID) {
                        try (ResultSet result = OnlineDataBaseAccess1.executeQuery("SELECT * FROM `MarketListings` WHERE ID = '" + itemID + "'")) {
                            if (result.next()) {
                                OnlineMarketTextField.setVisible(false);
                                UILabel buyPopup = (UILabel) player.getAttribute("BuyItemPopupPanel");
                                if (buyPopup == null) {
                                    System.out.println("BuyItemPopupPanel is null!");
                                    return;
                                }
                                buyPopup.setVisible(true);
                                UILabel buyInfo = (UILabel) player.getAttribute("BuyItemInfo");
                                if (buyInfo == null) {
                                    System.out.println("BuyItemInfo is null!");
                                    return;
                                }

                                String quantityText = (result.getInt("IsUnlimited") == 1) ? "1" : Integer.toString(result.getInt("ItemQuantity"));
                                buyInfo.setText("<b>Item:</b> " + result.getString("ItemName") +
                                    "\n<b>Price:</b> $" + result.getInt("ItemPrice") + " | <b>Quantity:</b> " + quantityText +
                                    "\n<b>Seller:</b> " + result.getString("ItemOwnerName"));

                                // Set attributes for purchase
                                player.setAttribute("SelectedItemID", result.getInt("ID"));
                                player.setAttribute("SelectedItemPrice", result.getInt("ItemPrice"));
                                player.setAttribute("SelectedItemQuantity", result.getInt("ItemQuantity"));
                                player.setAttribute("SelectedItemIsUnlimited", result.getInt("IsUnlimited"));
                                player.setAttribute("SelectedItemOwnerUID", result.getString("ItemOwnerUID"));
                                player.setAttribute("SelectedItemName", result.getString("ItemName"));

                                // Add item icon to buy popup
                                String category = result.getString("ItemCategory");
                                String itemName = result.getString("ItemName");
                                int variants = result.getInt("ItemVariants");
                                TextureAsset itemIcon = getItemIcon(category, itemName, variants);
                                if (itemIcon != null) {
                                    @SuppressWarnings("unchecked")
                                    ArrayList<UILabel> buyItemIcons = (ArrayList<UILabel>) player.getAttribute("buyItemIcons");
                                    for (UILabel icon : buyItemIcons) {
                                        icon.setVisible(false);
                                        icon.removeFromParent();
                                        player.removeUIElement(icon);
                                    }
                                    buyItemIcons.clear();
                                    UILabel buyItemIcon = new UILabel();
                                    buyItemIcon.setPosition(10, 10, false);
                                    buyItemIcon.setSize(50, 50, false);
                                    buyItemIcon.style.backgroundImage.set(itemIcon);
                                    buyItemIcon.style.backgroundImageScaleMode.set(ScaleMode.StretchToFill);
                                    buyPopup.addChild(buyItemIcon);
                                    buyItemIcons.add(buyItemIcon);
                                }

                                player.setMouseCursorVisible(true);
                            }
                        } catch (SQLException e) {
                            player.sendYellMessage("Error loading item: " + e.getMessage(), 3, true);
                            e.printStackTrace();
                        }
                        for (UILabel label : CopyOnlineMarketSelected) {
                            if (label != null) label.removeFromParent();
                        }
                        OnlineMarketTextField.setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * Clears the current listing display to prepare for a new page or search.
     * @param copyOnlineMarketSelected The list of current UI labels to clear.
     * @param onlineMarketSelectBottons The list of select buttons to reset.
     * @param onlineMarketSelected The list of selected item IDs to reset.
     */
    private void clearListingDisplay(ArrayList<UILabel> copyOnlineMarketSelected, ArrayList<UILabel> onlineMarketSelectBottons, ArrayList<String> onlineMarketSelected) {
        for (UILabel OnlineMarketsSeleted : copyOnlineMarketSelected) {
            if (OnlineMarketsSeleted != null) {
                onlineMarketSelectBottons.clear();
                onlineMarketSelected.clear();
                OnlineMarketsSeleted.setText("");
                OnlineMarketsSeleted.setVisible(false);
            }
        }
    }

    /**
     * Displays market listings based on the current search and page number.
     * @param player The player viewing the listings.
     * @param OnlineMarketTextField The UI label to display the listings in.
     * @param OnlineMarketSelectBottons The list to store select button UI elements.
     * @param OnlineMarketSelected The list to store selected item IDs.
     */
    private void displayListings(Player player, UILabel OnlineMarketTextField, ArrayList<UILabel> OnlineMarketSelectBottons, ArrayList<String> OnlineMarketSelected) {
        OnlineMarketTextField.setText("");
        player.setMouseCursorVisible(true);
        int setRowPosition = 0;
        int Row = 0;
        int limit = 8;
        String Search = (String) player.getAttribute("Search");
        String SearchRsult = (String) player.getAttribute("SearchRsult");
        int PageNumber = (int) player.getAttribute("OnlineMarketPageNumber");

        try (ResultSet Result = OnlineDataBaseAccess1.executeQuery(Search)) {
            Result.next();
            int db_count = Result.getInt(1);
            int page_max = (db_count + limit - 1) / limit;
            int page_now = Math.min(PageNumber, page_max);

            if (page_now >= 1) {
                int start = (page_now - 1) * limit;
                OnlineMarketTextField.setText("  Page " + page_now + " / " + page_max + "\n");
                try (ResultSet Result2 = OnlineDataBaseAccess2.executeQuery("SELECT * FROM " + SearchRsult + " ORDER BY ID DESC LIMIT " + start + ", " + limit)) {
                    NumberFormat priceFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                    while (Result2.next()) {
                        Row++;
                        setRowPosition = Row * 53 + 130;

                        // Create a selectable listing UI element
                        UILabel OnlineMarketSelect = new UILabel();
                        OnlineMarketSelect.style.textAlign.set(TextAnchor.MiddleCenter);
                        OnlineMarketSelect.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
                        OnlineMarketSelect.setFontSize(12);
                        OnlineMarketSelect.setBorder(2);
                        OnlineMarketSelect.setBorderColor(999);
                        OnlineMarketSelect.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
                        OnlineMarketSelect.setBorderEdgeRadius(5.0f, false);
                        OnlineMarketSelect.setSize(250, 50, false);
                        OnlineMarketSelect.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.9f);
                        OnlineMarketSelect.setPosition(24.5f, setRowPosition, false);
                        OnlineMarketSelect.style.borderBottomWidth.set(5);
                        OnlineMarketSelect.hoverStyle.backgroundColor.set(0.2f, 0.2f, 0.2f, 0.9f);
                        OnlineMarketSelect.hoverStyle.borderBottomWidth.set(5);
                        OnlineMarketSelect.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
                        OnlineMarketSelect.setClickable(true);

                        // Add item icon to the listing
                        String category = Result2.getString("ItemCategory");
                        String itemName = Result2.getString("ItemName");
                        int variants = Result2.getInt("ItemVariants");
                        TextureAsset itemIcon = getItemIcon(category, itemName, variants);

                        if (itemIcon != null) {
                            UILabel iconLabel = new UILabel();
                            iconLabel.setSize(30, 30, false);
                            iconLabel.setPosition(5, 10, false);
                            iconLabel.style.backgroundImage.set(itemIcon);
                            iconLabel.style.backgroundImageScaleMode.set(ScaleMode.ScaleToFit);
                            OnlineMarketSelect.addChild(iconLabel);
                        } else {
                            player.sendTextMessage("No icon available for " + itemName + ".");
                        }

                        // Set listing text with price and quantity
                        String formattedPrice = priceFormatter.format(Result2.getInt("ItemPrice"));
                        OnlineMarketSelect.setText("<b>" + itemName + "</b>\n" + formattedPrice + " Quantity= " + Result2.getString("ItemQuantity"));
                        OnlineMarketTextField.addChild(OnlineMarketSelect);
                        OnlineMarketSelectBottons.add(OnlineMarketSelect);
                        OnlineMarketSelected.add(OnlineMarketSelect.getID() + "," + Result2.getInt("ID"));
                    }
                }
            }
        } catch (SQLException sQLException) {
            player.sendYellMessage("Navigation Error: " + sQLException.getMessage(), 3, true);
            sQLException.printStackTrace();
        }
    }

    /**
     * Fetches the appropriate icon for an item based on its category, name, and variant.
     * @param category The category of the item (e.g., "construction", "clothing").
     * @param itemName The name of the item.
     * @param variants The variant ID of the item.
     * @return The TextureAsset icon for the item, or null if not found.
     */
    private TextureAsset getItemIcon(String category, String itemName, int variants) {
        TextureAsset itemIcon = null;
        if ("construction".equals(category)) {
            var def = Definitions.getConstructionDefinition(itemName);
            if (def != null) itemIcon = (TextureAsset) def.getIcon(variants);
        } else if ("clothing".equals(category)) {
            var def = Definitions.getClothingDefinition(itemName);
            if (def != null) itemIcon = (TextureAsset) def.getIcon(variants);
        } else if ("objectkit".equals(category)) {
            var def = Definitions.getObjectDefinition(itemName);
            if (def != null) itemIcon = (TextureAsset) def.getIcon(variants);
        } else if ("blueprint".equals(category)) {
            var def = Definitions.getItemDefinition(itemName);
            if (def != null) itemIcon = (TextureAsset) def.getIcon(variants);
        } else {
            var def = Definitions.getItemDefinition(itemName);
            if (def != null) itemIcon = (TextureAsset) def.getIcon(variants);
        }
        return itemIcon;
    }
}