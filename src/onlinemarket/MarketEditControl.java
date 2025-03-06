package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.UITextField;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess2;

/**
 * Handles logic for editing, removing, and bumping market listings in the Online Market.
 */
public class MarketEditControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketEditControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to editing market listings (confirm, remove, bump, etc.).
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void handleEditEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        String PlayerUID = player.getUID();
        String PlayerName = player.getName();
        int eventID = event.getUIElement().getID();

        int editUnlimitedToggleID = (int) player.getAttribute("EditItemUnlimitedToggleID");
        int editConfirmButtonID = (int) player.getAttribute("EditConfirmButtonID");
        int editRemoveButtonID = (int) player.getAttribute("EditRemoveButtonID");
        int editCancelButtonID = (int) player.getAttribute("EditCancelButtonID");
        int editExitButtonID = (int) player.getAttribute("EditExitButtonID");
        int editFeatureButtonID = (int) player.getAttribute("EditFeatureButtonID");

        // Handle toggling unlimited stock in edit popup (admin only)
        if (eventID == editUnlimitedToggleID && player.isAdmin()) {
            UILabel editUnlimitedToggle = (UILabel) player.getAttribute("EditItemUnlimitedToggle");
            int currentState = (int) player.getAttribute("SelectedItemIsUnlimited");
            int newState = (currentState == 1 ? 0 : 1);
            player.setAttribute("SelectedItemIsUnlimited", newState);
            editUnlimitedToggle.setText("<b>Unlimited Stock: " + (newState == 1 ? "ON" : "OFF") + "</b>");
            editUnlimitedToggle.setBackgroundColor(newState == 1 ? 0.0f : 0.5f, newState == 1 ? 0.5f : 0.0f, 0.0f, 0.9f);
        }

        // Handle confirming edits to a listing
        if (eventID == editConfirmButtonID) {
            int itemID = (int) player.getAttribute("SelectedItemID");
            String ownerUID = (String) player.getAttribute("SelectedItemOwnerUID");

            if (!PlayerUID.equals(ownerUID) && !player.isAdmin()) {
                player.sendYellMessage("You can only edit your own items!", 3, true);
                return;
            }

            UITextField editPriceField = (UITextField) player.getAttribute("EditItemPriceField");
            editPriceField.getCurrentText(player, (String priceText) -> {
                try {
                    int newPrice = Integer.parseInt(priceText);
                    int isUnlimited = (int) player.getAttribute("SelectedItemIsUnlimited");
                    String newOwnerName = isUnlimited == 1 ? "Server" : PlayerName;
                    String newOwnerUID = isUnlimited == 1 ? "SERVER_UID" : PlayerUID;

                    // Update listing in MarketListings
                    OnlineDataBaseAccess2.executeUpdate("UPDATE `MarketListings` SET ItemPrice = " + newPrice + ", IsUnlimited = " + isUnlimited + ", ItemOwnerName = '" + newOwnerName + "', ItemOwnerUID = '" + newOwnerUID + "' WHERE ID = " + itemID);
                    player.sendYellMessage("Item updated! New price: $" + newPrice + (isUnlimited == 1 ? " (Unlimited)" : ""), 3, true);

                    // Finalize edit
                    UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
                    editPopup.setVisible(false);
                    menuControl.getOpenOnlineMarket().OpenMenu(player);
                    menuControl.refreshStatsUI(player);
                } catch (NumberFormatException e) {
                    player.sendYellMessage("Invalid price — use whole numbers!", 3, true);
                }
            });
        }

        // Handle removing a listing from the market
        if (eventID == editRemoveButtonID) {
            int itemID = (int) player.getAttribute("SelectedItemID");
            String ownerUID = (String) player.getAttribute("SelectedItemOwnerUID");
            String itemName = (String) player.getAttribute("SelectedItemName");
            int itemQuantity = (int) player.getAttribute("SelectedItemQuantity");

            if (!PlayerUID.equals(ownerUID) && !player.isAdmin()) {
                player.sendYellMessage("You can only remove your own items!", 3, true);
                return;
            }

            try (ResultSet itemResult = OnlineDataBaseAccess1.executeQuery("SELECT * FROM `MarketListings` WHERE ID = " + itemID)) {
                if (itemResult.next()) {
                    String category = itemResult.getString("ItemCategory");
                    short itemId = itemResult.getShort("ItemID");
                    int variants = itemResult.getInt("ItemVariants");
                    int quantity = itemResult.getInt("ItemQuantity");
                    byte constructionID = itemResult.getByte("ItemID");

                    // Return item to owner's inventory if not server-owned
                    Player target = PlayerUID.equals(ownerUID) ? player : Server.getPlayerByUID(ownerUID);
                    if (target != null && !ownerUID.equals("SERVER_UID")) {
                        returnItemToInventory(target, category, itemId, variants, quantity, constructionID);
                        target.sendYellMessage("Your item " + itemName + " was returned!", 3, true);
                    }

                    // Remove from MarketListings
                    OnlineDataBaseAccess2.executeUpdate("DELETE FROM `MarketListings` WHERE ID = " + itemID);
                    player.sendYellMessage("Item removed from market!", 3, true);

                    // Log "Removal" transaction
                    String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                 "VALUES ('" + PlayerUID + "', '" + itemName + "', " + itemQuantity + ", 0, 0, 'Removal', '" + ownerUID + "')";
                    OnlineDataBaseAccess1.executeUpdate(sql);

                    // Finalize removal
                    UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
                    editPopup.setVisible(false);
                    menuControl.getOpenOnlineMarket().OpenMenu(player);
                    menuControl.refreshStatsUI(player);
                }
            } catch (SQLException e) {
                player.sendYellMessage("Error removing item or logging transaction: " + e.getMessage(), 3, true);
                e.printStackTrace();
            }
        }

        // Handle canceling the edit popup and returning to buy popup
        if (eventID == editCancelButtonID) {
            UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
            editPopup.setVisible(false);
            UILabel buyPopup = (UILabel) player.getAttribute("BuyItemPopupPanel");
            buyPopup.setVisible(true);
        }

        // Handle exiting the edit popup back to market menu
        if (eventID == editExitButtonID) {
            UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
            editPopup.setVisible(false);
            menuControl.getOpenOnlineMarket().OpenMenu(player);
        }

        // Handle bumping a listing to the top for a fee
        if (eventID == editFeatureButtonID) {
            int itemID = (int) player.getAttribute("SelectedItemID");
            String ownerUID = (String) player.getAttribute("SelectedItemOwnerUID");
            String itemName = (String) player.getAttribute("SelectedItemName");

            if (!PlayerUID.equals(ownerUID) && !player.isAdmin()) {
                player.sendYellMessage("You can only bump your own items!", 3, true);
                return;
            }

            try (ResultSet moneyResult = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + PlayerUID + "'")) {
                float playerMoney = moneyResult.next() ? moneyResult.getFloat("Money") : 0;
                float bumpFee = 1.0f;
                if (playerMoney >= bumpFee) {
                    try (ResultSet itemResult = OnlineDataBaseAccess1.executeQuery("SELECT * FROM `MarketListings` WHERE ID = " + itemID)) {
                        if (itemResult.next()) {
                            String itemPrice = itemResult.getString("ItemPrice");
                            String itemOwnerName = itemResult.getString("ItemOwnerName");
                            String itemOwnerUID = itemResult.getString("ItemOwnerUID");
                            String itemIDValue = itemResult.getString("ItemID");
                            String itemCategory = itemResult.getString("ItemCategory");
                            int itemVariants = itemResult.getInt("ItemVariants");
                            int itemQuantity = itemResult.getInt("ItemQuantity");
                            int isUnlimited = itemResult.getInt("IsUnlimited");

                            // Bump listing by deleting and re-adding
                            OnlineDataBaseAccess2.executeUpdate("DELETE FROM `MarketListings` WHERE ID = " + itemID);
                            OnlineDataBaseAccess2.executeUpdate("INSERT INTO `MarketListings` (ItemName, ItemPrice, ItemOwnerName, ItemOwnerUID, ItemID, ItemCategory, ItemVariants, ItemQuantity, IsUnlimited) " +
                                "VALUES ('" + itemName + "', '" + itemPrice + "', '" + itemOwnerName + "', '" + itemOwnerUID + "', '" + itemIDValue + "', '" + itemCategory + "', '" + itemVariants + "', '" + itemQuantity + "', '" + isUnlimited + "')");

                            // Update bumped item ID
                            try (ResultSet newIDResult = OnlineDataBaseAccess2.executeQuery("SELECT last_insert_rowid() AS NewID")) {
                                newIDResult.next();
                                int newID = newIDResult.getInt("NewID");
                                player.setAttribute("BumpedItemID", newID);
                            }

                            // Deduct bump fee and log transaction
                            menuControl.updatePlayerMoney(player, -bumpFee);
                            player.sendYellMessage("Item bumped to top for $1!", 3, true);
                            String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                         "VALUES ('" + PlayerUID + "', '" + itemName + "', 1, " + bumpFee + ", " + bumpFee + ", 'BumpFee', 'SERVER_UID')";
                            OnlineDataBaseAccess1.executeUpdate(sql);

                            // Finalize bump
                            menuControl.updateWalletDisplay(player);
                            UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
                            editPopup.setVisible(false);
                            menuControl.getOpenOnlineMarket().OpenMenu(player);
                            menuControl.refreshStatsUI(player);
                        }
                    }
                } else {
                    player.sendYellMessage("You need $1 to bump this item!", 3, true);
                }
            } catch (SQLException e) {
                player.sendYellMessage("Error bumping item or logging transaction: " + e.getMessage(), 3, true);
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns an item to a player's inventory based on its category.
     * @param player The player receiving the item.
     * @param category The category of the item (e.g., "construction", "clothing").
     * @param itemId The item ID for most categories.
     * @param variants The variant ID of the item.
     * @param quantity The quantity to return.
     * @param constructionID The construction ID (used for construction items).
     */
    private void returnItemToInventory(Player player, String category, short itemId, int variants, int quantity, byte constructionID) {
        if ("construction".equals(category)) {
            player.getInventory().addConstructionItem(constructionID, variants, quantity, 0);
        } else if ("clothing".equals(category)) {
            player.getInventory().addClothingItem(itemId, variants, quantity, 0, 0L);
        } else if ("objectkit".equals(category)) {
            player.getInventory().addObjectItem(itemId, variants, quantity);
        } else {
            player.getInventory().addItem(itemId, variants, quantity);
        }
    }
}