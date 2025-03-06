package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import net.risingworld.api.Plugin;
import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.style.ScaleMode;
import net.risingworld.api.ui.style.TextAnchor;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.definitions.Clothing;
import net.risingworld.api.definitions.Clothing.ClothingDefinition;
import net.risingworld.api.definitions.Constructions;
import net.risingworld.api.definitions.Constructions.ConstructionDefinition;
import net.risingworld.api.definitions.Definitions;
import net.risingworld.api.definitions.Items;
import net.risingworld.api.definitions.Objects;
import net.risingworld.api.definitions.Objects.ObjectDefinition;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess2;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess3;

/**
 * Handles opening and displaying the OnlineMarket menu for a player.
 *
 * @author yahgi
 */
public class OpenOnlineMarket extends OnlineMarket {

    /**
     * Constructor to initialize the OpenOnlineMarket with the plugin instance.
     *
     * @param plugin The Rising World plugin instance.
     */
    public OpenOnlineMarket(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Opens the OnlineMarket menu for the specified player, displaying a paginated list of notes
     * with item icons.
     *
     * @param player The player for whom the menu is opened.
     */
    public void OpenMenu(Player player) {
        // Retrieve player details
        String playerName = player.getName();
        String playerUID = player.getUID();
        Vector3f playerPosition = player.getPosition();

        // Retrieve UI elements from player attributes
        UILabel onlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");

        // Retrieve note selection data structures from player attributes
        @SuppressWarnings("unchecked")
        ArrayList<UILabel> onlineMarketSelectButtons = (ArrayList<UILabel>) player.getAttribute("OnlineMarketSelectBottons");
        @SuppressWarnings("unchecked")
        ArrayList<String> onlineMarketSelected = (ArrayList<String>) player.getAttribute("OnlineMarketSelected");
        ArrayList<UILabel> copyOnlineMarketSelected = new ArrayList<>(onlineMarketSelectButtons);

        // Show the menu and enable mouse cursor
        player.setMouseCursorVisible(true);
        onlineMarketTextField.setVisible(true);

        // Clear existing note selections
        for (UILabel selected : copyOnlineMarketSelected) {
            if (selected != null) {
                onlineMarketSelectButtons.clear();
                onlineMarketSelected.clear();
                selected.setText("");
                selected.setVisible(false);
            }
        }

        // Initialize menu display
        onlineMarketTextField.setText("");
        player.setAttribute("OnlineMarketPageNumber", 1);
        int setRowPosition = 0;
        int row = 0;
        int limit = 8;

        // Retrieve search query and result table from player attributes
        String search = (String) player.getAttribute("Search");
        String searchResult = (String) player.getAttribute("SearchRsult");

        // Fetch and display notes from the database with icons
        try (ResultSet result = OnlineDataBaseAccess1.executeQuery(search)) {
            result.next();
            int dbCount = result.getInt(1);
            int pageMax = (dbCount + limit - 1) / limit;
            int pageNow = 1;

            if (pageNow <= pageMax) {
                int start = (pageNow - 1) * limit;
                onlineMarketTextField.setText("  Page " + pageNow + " / " + pageMax + "\n");

                try (ResultSet result2 = OnlineDataBaseAccess2.executeQuery(
                    "SELECT * FROM " + searchResult + " ORDER BY ID DESC LIMIT " + start + ", " + limit)) {
                    NumberFormat priceFormatter = NumberFormat.getCurrencyInstance(Locale.US);

                    while (result2.next()) {
                        row++;
                        setRowPosition = row * 53 + 130;

                        UILabel onlineMarketSelect = new UILabel();
                        onlineMarketSelect.style.textAlign.set(TextAnchor.MiddleCenter);
                        onlineMarketSelect.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
                        onlineMarketSelect.setFontSize(12);
                        onlineMarketSelect.setBorder(2);
                        onlineMarketSelect.setBorderColor(999);
                        onlineMarketSelect.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
                        onlineMarketSelect.setBorderEdgeRadius(5.0f, false);
                        onlineMarketSelect.setSize(250, 50, false);
                        onlineMarketSelect.style.borderBottomWidth.set(5);
                        onlineMarketSelect.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.9f);
                        onlineMarketSelect.setPosition(24.5f, setRowPosition, false);
                        onlineMarketSelect.hoverStyle.backgroundColor.set(0.2f, 0.2f, 0.2f, 0.9f);
                        onlineMarketSelect.hoverStyle.borderBottomWidth.set(5);
                        onlineMarketSelect.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
                        onlineMarketSelect.setClickable(true);

                        // Fetch icon based on category, name, and variant
                        String category = result2.getString("ItemCategory");
                        String itemName = result2.getString("ItemName");
                        int variants = result2.getInt("ItemVariants");
                        TextureAsset itemIcon = null;

                        if ("construction".equals(category)) {
                            ConstructionDefinition def = Definitions.getConstructionDefinition(itemName);
                            if (def != null) {
                                itemIcon = (TextureAsset) def.getIcon(variants);
                            }
                        } else if ("clothing".equals(category)) {
                            ClothingDefinition def = Definitions.getClothingDefinition(itemName);
                            if (def != null) {
                                itemIcon = (TextureAsset) def.getIcon(variants);
                            }
                        } else if ("objectkit".equals(category)) {
                            ObjectDefinition def = Definitions.getObjectDefinition(itemName);
                            if (def != null) {
                                itemIcon = (TextureAsset) def.getIcon(variants);
                            }
                        } else if ("blueprint".equals(category)) {
                            Items.ItemDefinition def = Definitions.getItemDefinition(itemName); // Adjust if needed
                            if (def != null) {
                                itemIcon = (TextureAsset) def.getIcon(variants);
                            }
                        } else {
                            Items.ItemDefinition def = Definitions.getItemDefinition(itemName); // Fallback
                            if (def != null) {
                                itemIcon = (TextureAsset) def.getIcon(variants);
                            }
                        }

                        // Add icon as a child label if available, positioned hard left
                        if (itemIcon != null) {
                            UILabel iconLabel = new UILabel();
                            iconLabel.setSize(30, 30, false); // 30x30 icon size
                            iconLabel.setPosition(5, 10, false); // Hard left, slight padding
                            iconLabel.style.backgroundImage.set(itemIcon);
                            iconLabel.style.backgroundImageScaleMode.set(ScaleMode.ScaleToFit); // Keeps aspect ratio
                            onlineMarketSelect.addChild(iconLabel);
                        } else {
                            player.sendTextMessage("No icon available for " + itemName + ".");
                          
                        }

                        // Set button text (centered, unchanged for now)
                        String formattedPrice = priceFormatter.format(result2.getInt("ItemPrice"));
                        onlineMarketSelect.setText("<b>" + result2.getString("ItemName") + "</b>\n" + 
                            formattedPrice + " Quantity= " + result2.getInt("ItemQuantity"));

                        onlineMarketTextField.addChild(onlineMarketSelect);
                        onlineMarketSelectButtons.add(onlineMarketSelect);
                        onlineMarketSelected.add(onlineMarketSelect.getID() + "," + result2.getInt("ID"));
                    }
                }
            }
        } catch (SQLException e) {
            // Silently handle SQL exceptions (consider logging in production)
            player.sendTextMessage("Database error loading market: " + e.getMessage());
        }
    }
}