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
 * Handles logic for purchasing items from the Online Market.
 */
public class MarketPurchaseControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketPurchaseControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to purchasing items (buy, cancel, edit).
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void handlePurchaseEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        String PlayerUID = player.getUID();
        String PlayerName = player.getName();
        int eventID = event.getUIElement().getID();

        int BuyConfirmButtonID = (int) player.getAttribute("BuyConfirmButtonID");
        int BuyCancelButtonID = (int) player.getAttribute("BuyCancelButtonID");
        int BuyEditButtonID = (int) player.getAttribute("BuyEditButtonID");

        // Handle "Confirm Buy" button to purchase an item
        if (eventID == BuyConfirmButtonID) {
            try {
                int SaleitemID = (int) player.getAttribute("SelectedItemID");
                int itemPrice = (int) player.getAttribute("SelectedItemPrice");
                int itemQuantity = (int) player.getAttribute("SelectedItemQuantity");
                int isUnlimited = (int) player.getAttribute("SelectedItemIsUnlimited");
                String itemOwnerUID = (String) player.getAttribute("SelectedItemOwnerUID");
                String itemName = (String) player.getAttribute("SelectedItemName");
                String buyerUID = PlayerUID;
                String buyerName = PlayerName;

                // Check buyer's money balance
                float buyerMoney = getPlayerMoney(buyerUID, buyerName);
                if (buyerMoney >= itemPrice) {
                    // Deliver item to buyer
                    deliverItem(player, SaleitemID);

                    // Update money for buyer and seller
                    menuControl.updatePlayerMoney(player, -itemPrice);
                    if (isUnlimited == 0) {
                        Player seller = Server.getPlayerByUID(itemOwnerUID);
                        if (seller != null) {
                            menuControl.updatePlayerMoney(seller, itemPrice);
                        } else {
                            updateOfflineSellerMoney(itemOwnerUID, itemPrice);
                        }
                        OnlineDataBaseAccess2.executeUpdate("DELETE FROM `MarketListings` WHERE ID = " + SaleitemID);
                    }

                    // Log "Purchase" transaction
                    float totalAmount = itemPrice * (isUnlimited == 1 ? 1 : itemQuantity);
                    String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                 "VALUES ('" + buyerUID + "', '" + itemName + "', " + (isUnlimited == 1 ? 1 : itemQuantity) + ", " + itemPrice + ", " + totalAmount + ", 'Purchase', '" + itemOwnerUID + "')";
                    OnlineDataBaseAccess1.executeUpdate(sql);

                    // Notify buyer and seller
                    player.sendYellMessage("Bought " + itemName + " for $" + itemPrice + "!", 3, true);
                    Player seller = Server.getPlayerByUID(itemOwnerUID);
                    if (seller != null && !itemOwnerUID.equals("SERVER_UID")) {
                        seller.sendYellMessage(buyerName + " bought your " + itemName + " for $" + itemPrice + "!", 3, true);
                    }

                    // Finalize purchase
                    UILabel buyPopup = (UILabel) player.getAttribute("BuyItemPopupPanel");
                    if (buyPopup != null) {
                        buyPopup.setVisible(false);
                    }
                    menuControl.getOpenOnlineMarket().OpenMenu(player);
                    menuControl.updateWalletDisplay(player);
                    player.setMouseCursorVisible(true);
                    menuControl.refreshStatsUI(player);
                } else {
                    
                    UILabel buyInfo = (UILabel) player.getAttribute("BuyItemInfo"); // updated this line
                    if (buyInfo != null) {
                        buyInfo.setText("<b>Item:</b> " + itemName + "\n<b>Price:</b> $" + itemPrice + " | <b>Quantity:</b> " + (isUnlimited == 1 ? "1" : itemQuantity) + "\n<b>Seller:</b> " + (isUnlimited == 1 ? "Server" : itemOwnerUID) + "\n<b>Error:</b> Insufficient funds!");
                    }
                    player.sendYellMessage("You don’t have enough money to buy this item!", 3, true);
                }
            } catch (SQLException e) {
                player.sendYellMessage("Error processing purchase or logging transaction: " + e.getMessage(), 3, true);
                e.printStackTrace();
            }
        }

        // Handle canceling the buy popup
        if (eventID == BuyCancelButtonID) {
            UILabel buyPopup = (UILabel) player.getAttribute("BuyItemPopupPanel");
            if (buyPopup != null) {
                buyPopup.setVisible(false);
                
                    player.setAttribute("SelectedItemID", null);
                    player.setAttribute("SelectedItemPrice", null);
                    player.setAttribute("SelectedItemQuantity", null);
                    player.setAttribute("SelectedItemIsUnlimited", null);
                    player.setAttribute("SelectedItemOwnerUID", null);
                    player.setAttribute("SelectedItemName", null);
                
            }
            menuControl.getOpenOnlineMarket().OpenMenu(player);
        }

        // Handle opening the edit popup for an item
        if (eventID == BuyEditButtonID) {
            String ownerUID = (String) player.getAttribute("SelectedItemOwnerUID");
            if (!PlayerUID.equals(ownerUID) && !player.isAdmin()) {
                player.sendYellMessage("You can only edit your own items!", 3, true);
                return;
            }

            UILabel buyPopup = (UILabel) player.getAttribute("BuyItemPopupPanel");
            buyPopup.setVisible(false);

            // Setup edit popup UI
            UILabel editPopup = (UILabel) player.getAttribute("EditItemPopupPanel");
            UILabel editInfo = (UILabel) player.getAttribute("EditItemInfo");
            UITextField editPriceField = (UITextField) player.getAttribute("EditItemPriceField");
            UILabel editUnlimitedToggle = (UILabel) player.getAttribute("EditItemUnlimitedToggle");

            String itemName = (String) player.getAttribute("SelectedItemName");
            int itemPrice = (int) player.getAttribute("SelectedItemPrice");
            int itemQuantity = (int) player.getAttribute("SelectedItemQuantity");
            int isUnlimited = (int) player.getAttribute("SelectedItemIsUnlimited");

            editInfo.setText("<b>Item:</b> " + itemName + "\n<b>Quantity:</b> " + (isUnlimited == 1 ? "Unlimited" : itemQuantity));
            editPriceField.setText(String.valueOf(itemPrice));
            editUnlimitedToggle.setText("<b>Unlimited Stock: " + (isUnlimited == 1 ? "ON" : "OFF") + "</b>");
            if (!player.isAdmin()) {
                editUnlimitedToggle.setBackgroundColor(0.5f, 0.5f, 0.5f, 0.9f);
                editUnlimitedToggle.setClickable(false);
            } else {
                editUnlimitedToggle.setBackgroundColor(isUnlimited == 1 ? 0.0f : 0.5f, isUnlimited == 1 ? 0.5f : 0.0f, 0.0f, 0.9f);
                editUnlimitedToggle.setClickable(true);
            }

            editPopup.setVisible(true);
            player.setMouseCursorVisible(true);
        }
    }

    /**
     * Retrieves or initializes a player's money balance.
     * @param playerUID The UID of the player.
     * @param playerName The name of the player (for new account creation).
     * @return The player's current money balance.
     * @throws SQLException If a database operation fails.
     */
    private float getPlayerMoney(String playerUID, String playerName) throws SQLException {
        try (ResultSet buyerMoneyResult = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + playerUID + "'")) {
            if (!buyerMoneyResult.next()) {
                OnlineDataBaseAccess2.executeUpdate("INSERT INTO `PlayerMoney` (PlayerUID, PlayerName, Money) VALUES ('" + playerUID + "', '" + playerName + "', 0)");
                Player player = Server.getPlayerByUID(playerUID);
                if (player != null) {
                    player.sendYellMessage("Created a new money account for you with $0 — add funds to make purchases!", 3, true);
                }
                try (ResultSet newBuyerMoneyResult = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + playerUID + "'")) {
                    if (!newBuyerMoneyResult.next()) {
                        throw new SQLException("Error creating money account for " + playerUID);
                    }
                    return newBuyerMoneyResult.getFloat("Money");
                }
            }
            return buyerMoneyResult.getFloat("Money");
        }
    }

    /**
     * Delivers a purchased item to the buyer's inventory.
     * @param player The player receiving the item.
     * @param itemID The ID of the item in MarketListings.
     * @throws SQLException If a database operation fails or item not found.
     */
    private void deliverItem(Player player, int itemID) throws SQLException {
        try (ResultSet itemResult = OnlineDataBaseAccess1.executeQuery("SELECT * FROM `MarketListings` WHERE ID = " + itemID)) {
            if (itemResult.next()) {
                String product = itemResult.getString("ItemCategory");
                Short itemId = itemResult.getShort("ItemID");
                Short ClothingItem = itemResult.getShort("ItemID");
                Short ObjectItem = itemResult.getShort("ItemID");
                int quantity = itemResult.getInt("ItemQuantity");
                int variants = itemResult.getInt("ItemVariants");
                byte ConstructionItemID = itemResult.getByte("ItemID");

                int color = 0;
                long infoID = 0L;

                // Add item to player's inventory based on category
                if ("construction".equals(product)) {
                    player.getInventory().addConstructionItem(ConstructionItemID, variants, quantity, color);
                } else if ("clothing".equals(product)) {
                    player.getInventory().addClothingItem(ClothingItem, variants, quantity, 0, infoID);
                } else if ("objectkit".equals(product)) {
                    player.getInventory().addObjectItem(ObjectItem, variants, quantity);
                } else if ("blueprint".equals(product)) {
                    player.getInventory().addItem(itemId, variants, quantity);
                } else {
                    player.getInventory().addItem(itemId, variants, quantity);
                }
            } else {
                player.sendYellMessage("Item not found in market — please contact an admin!", 3, true);
                throw new SQLException("Item ID " + itemID + " not found in MarketListings");
            }
        }
    }

    /**
     * Updates an offline seller's money balance in the database.
     * @param sellerUID The UID of the offline seller.
     * @param amount The amount to add to the seller's money.
     * @throws SQLException If a database operation fails.
     */
    private void updateOfflineSellerMoney(String sellerUID, int amount) throws SQLException {
        try (ResultSet sellerMoneyResult = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + sellerUID + "'")) {
            float sellerMoney = sellerMoneyResult.next() ? sellerMoneyResult.getFloat("Money") : 0;
            OnlineDataBaseAccess2.executeUpdate("INSERT OR REPLACE INTO `PlayerMoney` (PlayerUID, PlayerName, Money) VALUES ('" + sellerUID + "', (SELECT PlayerName FROM `PlayerMoney` WHERE PlayerUID = '" + sellerUID + "' LIMIT 1), " + (sellerMoney + amount) + ")");
        }
    }
}