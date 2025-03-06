package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.objects.Item;
import net.risingworld.api.ui.UILabel;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;

/**
 * Handles logic for selling stone (all types) and lumber resources (including all tree log types) to the server in the Online Market.
 */
public class MarketResourceControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    // List of all stone item names from the provided table (IDs 400–414)
    private static final String[] STONE_TYPES = {
        "stone", "gravel", "dirt", "mud", "forestground", "volcanicrock", "obsidianrock",
        "sand", "sandstone", "redclay", "hellstone", "cobble", "underwaterrock"
    };

    // List of all tree log item names from the previous table (IDs 510–525)
    private static final String[] TREELOG_TYPES = {
        "treelog", "treeloghickory", "treelogacacia", "treelogaraucaria", "treelogbirch",
        "treeloglondonplane", "treelogcypress", "treelogcactus", "treelogpalm", "treelogredmaple",
        "treelogspruce", "treelogweepingbeech", "treelogwillowoak", "treelogapple", "treelogmesquite",
        "treelogaleppo"
    };

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketResourceControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to selling stone (all types) and lumber resources.
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void handleResourceEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        String PlayerUID = player.getUID();
        String PlayerName = player.getName(); // Added for new account creation
        int eventID = event.getUIElement().getID();

        int onlineMarketSellStoneButtonID = (int) player.getAttribute("OnlineMarketSellStoneButtonID");
        int onlineMarketSellLumberButtonID = (int) player.getAttribute("OnlineMarketSellLumberButtonID");

        // Handle selling stone (all types) to the server
        if (eventID == onlineMarketSellStoneButtonID) {
            Item equippedItem = player.getEquippedItem();
            player.setAttribute("itemRemoved", false);
            if (equippedItem == null) {
                player.sendYellMessage("You need to be holding Stone to sell it!!", 1, false);
                return;
            }

            try {
                float wallet = getPlayerMoney(PlayerUID, PlayerName); // Ensure account exists
                String itemName = equippedItem.getDefinition().name;
                if (isStone(itemName)) {
                    int stack = equippedItem.getStack();
                    if (stack >= 1) {
                        float sellPrice = 5.0f; // Consistent sell price for all stone types
                        float totalPaid = sellPrice * stack;
                        wallet += totalPaid;

                        // Update player's money and remove stone
                        menuControl.updatePlayerMoney(player, totalPaid);
                        removeItemFromInventory(player, itemName, stack);

                        // Update wallet display
                        NumberFormat walletFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                        String formattedWallet = walletFormatter.format(wallet);
                        player.setAttribute("Wallet", formattedWallet);
                        updatePointsInfo(player, formattedWallet);

                        // Log "Sale" transaction for the specific stone type
                        String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                     "VALUES ('" + PlayerUID + "', '" + itemName + "', " + stack + ", " + sellPrice + ", " + totalPaid + ", 'Sale', 'SERVER_UID')";
                        OnlineDataBaseAccess1.executeUpdate(sql);

                        // Notify player and refresh UI
                        player.sendYellMessage("Sold " + stack + " " + formatStoneName(itemName) + " for " + walletFormatter.format(totalPaid) + "! New balance: " + formattedWallet + ".", 3, true);
                        menuControl.updateWalletDisplay(player);
                        menuControl.refreshStatsUI(player);
                    }
                } else {
                    player.sendYellMessage("You need to be holding Stone to sell it!!", 1, false);
                }
            } catch (SQLException e) {
                player.sendYellMessage("Error selling stones or logging transaction: " + e.getMessage(), 3, true);
                e.printStackTrace();
            }
        }

        // Handle selling lumber (all tree log types) to the server
        if (eventID == onlineMarketSellLumberButtonID) {
            Item equippedItem = player.getEquippedItem();
            player.setAttribute("itemRemoved", false);
            if (equippedItem == null) {
                player.sendYellMessage("You need to be holding Logs to sell them!!", 1, false);
                return;
            }

            try {
                float wallet = getPlayerMoney(PlayerUID, PlayerName); // Ensure account exists
                String itemName = equippedItem.getDefinition().name;
                if (isTreeLog(itemName)) {
                    int stack = equippedItem.getStack();
                    if (stack >= 1) {
                        float sellPrice = 10.0f; // Consistent sell price for all tree logs
                        float totalPaid = sellPrice * stack;
                        wallet += totalPaid;

                        // Update player's money and remove lumber
                        menuControl.updatePlayerMoney(player, totalPaid);
                        removeItemFromInventory(player, itemName, stack);

                        // Update wallet display
                        NumberFormat walletFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                        String formattedWallet = walletFormatter.format(wallet);
                        player.setAttribute("Wallet", formattedWallet);
                        updatePointsInfo(player, formattedWallet);

                        // Log "Sale" transaction for the specific tree log
                        String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                     "VALUES ('" + PlayerUID + "', '" + itemName + "', " + stack + ", " + sellPrice + ", " + totalPaid + ", 'Sale', 'SERVER_UID')";
                        OnlineDataBaseAccess1.executeUpdate(sql);

                        // Notify player and refresh UI
                        player.sendYellMessage("Sold " + stack + " " + formatTreeLogName(itemName) + " for " + walletFormatter.format(totalPaid) + "! New balance: " + formattedWallet + ".", 3, true);
                        menuControl.updateWalletDisplay(player);
                        menuControl.refreshStatsUI(player);
                    }
                } else {
                    player.sendYellMessage("You need to be holding Logs to sell them!!", 1, false);
                }
            } catch (SQLException e) {
                player.sendYellMessage("Error selling logs or logging transaction: " + e.getMessage(), 3, true);
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a player's current money balance, creating a new account if none exists.
     * @param playerUID The UID of the player.
     * @param playerName The name of the player (for new account creation).
     * @return The player's current money balance.
     * @throws SQLException If a database operation fails.
     */
    private float getPlayerMoney(String playerUID, String playerName) throws SQLException {
        try (ResultSet result = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE `PlayerUID` = '" + playerUID + "'")) {
            if (!result.next()) {
                // Create a new money account with 0 balance if none exists
                OnlineDataBaseAccess1.executeUpdate("INSERT INTO `PlayerMoney` (PlayerUID, PlayerName, Money) VALUES ('" + playerUID + "', '" + playerName + "', 0)");
                Player player = Server.getPlayerByUID(playerUID);
                if (player != null) {
                    player.sendYellMessage("Created a new money account for you with $0!", 3, true);
                }
                return 0; // New account starts at 0
            }
            return result.getFloat("Money");
        }
    }

    /**
     * Checks if the given item name is one of the stone types.
     * @param itemName The name of the item to check.
     * @return True if the item is a stone type, false otherwise.
     */
    public boolean isStone(String itemName) { // Changed from private to public
        for (String stone : STONE_TYPES) {
            if (stone.equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given item name is one of the tree log types.
     * @param itemName The name of the item to check.
     * @return True if the item is a tree log, false otherwise.
     */
    public boolean isTreeLog(String itemName) { // Changed from private to public
        for (String treeLog : TREELOG_TYPES) {
            if (treeLog.equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Formats a stone name for display, making it more readable (e.g., "stone" → "Stone").
     * @param itemName The raw item name from the game.
     * @return A formatted, user-friendly name.
     */
    private String formatStoneName(String itemName) {
        // Capitalize the first letter and make the rest lowercase for readability
        return itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
    }

    /**
     * Formats a tree log name for display, making it more readable (e.g., "treeloghickory" → "Hickory Tree Log").
     * @param itemName The raw item name from the game.
     * @return A formatted, user-friendly name.
     */
    private String formatTreeLogName(String itemName) {
        // Remove the "treelog" prefix and capitalize the rest for readability
        if (itemName.startsWith("treelog")) {
            String type = itemName.substring(7); // Remove "treelog"
            if (type.isEmpty()) return "Tree Log";
            return type.substring(0, 1).toUpperCase() + type.substring(1) + " Tree Log";
        }
        return itemName; // Fallback for unexpected names
    }

    /**
     * Removes an item from the player's inventory or quickslot if it matches the criteria.
     * @param player The player whose inventory is being modified.
     * @param itemName The name of the item to remove (e.g., "stone", "treelog").
     * @param stack The stack size to remove.
     */
    private void removeItemFromInventory(Player player, String itemName, int stack) {
        for (int i = 0; i < 36; i++) {
            Item slotItem = player.getInventory().getItem(i, Inventory.SlotType.Inventory);
            if (slotItem != null && slotItem.getDefinition().name.equals(itemName) && slotItem.getStack() == stack) {
                player.getInventory().removeItem(i, Inventory.SlotType.Inventory, stack);
                player.setAttribute("itemRemoved", true);
                return;
            }
        }

        // If not found in inventory, check quickslot
        if (Boolean.FALSE.equals(player.getAttribute("itemRemoved"))) {
            int quickslotFocus = player.getInventory().getQuickslotFocus();
            Item quickslotItem = player.getInventory().getItem(quickslotFocus, Inventory.SlotType.Quickslot);
            if (quickslotItem != null && quickslotItem.getDefinition().name.equals(itemName) && quickslotItem.getStack() == stack) {
                player.getInventory().removeItem(quickslotFocus, Inventory.SlotType.Quickslot, stack);
                player.setAttribute("itemRemoved", true);
            }
        }
    }

    /**
     * Updates the points info UI label with the player's current wallet balance.
     * @param player The player whose points info is being updated.
     * @param formattedWallet The formatted wallet balance string.
     */
    private void updatePointsInfo(Player player, String formattedWallet) {
        UILabel pointsInfo = (UILabel) player.getAttribute("PointsInfo");
        if (pointsInfo != null) {
            pointsInfo.setText("Wallet Cash=" + formattedWallet); // Use formattedWallet for consistency
        }
    }
}