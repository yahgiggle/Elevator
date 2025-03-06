package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;

/**
 * Manages player statistics for the Online Market, loading and displaying market-related data.
 */
public class Stats {
    private final Plugin plugin;

    /**
     * Constructor initializing the plugin instance.
     * @param plugin The Rising World plugin instance.
     */
    public Stats(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads player statistics from the database, including listings, wallet, and transaction-based metrics.
     * @param player The player whose stats are being loaded.
     */
    public void loadPlayerStats(Player player) {
        String playerUID = player.getUID();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(java.util.Locale.US);
        try {
            // Total Listings
            ResultSet rs = OnlineDataBaseAccess1.executeQuery("SELECT COUNT(*) as listingCount FROM MarketListings WHERE ItemOwnerUID = '" + playerUID + "'");
            if (rs.next()) {
                player.setAttribute("StatsListingCountValue", rs.getInt("listingCount"));
            }

            // Wallet Balance
            ResultSet rs2 = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM PlayerMoney WHERE PlayerUID = '" + playerUID + "'");
            if (rs2.next()) {
                player.setAttribute("StatsWalletValue", rs2.getFloat("Money"));
            }

            // Current Stock (StockAdded - Purchase)
            ResultSet rs3 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SUM(CASE WHEN TransactionType = 'StockAdded' THEN QuantitySold ELSE 0 END) - " +
                "SUM(CASE WHEN TransactionType = 'Purchase' THEN QuantitySold ELSE 0 END) AS stock " +
                "FROM MarketTransactions WHERE PlayerUID = '" + playerUID + "' OR SellerUID = '" + playerUID + "'"
            );
            if (rs3.next()) {
                player.setAttribute("StatsStockValue", rs3.getInt("stock"));
            }

            // Total Items Sold (Purchase + Sale as seller)
            ResultSet rs4 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SUM(QuantitySold) AS itemsSold " +
                "FROM MarketTransactions WHERE (TransactionType = 'Purchase' AND SellerUID = '" + playerUID + "') " +
                "OR (TransactionType = 'Sale' AND PlayerUID = '" + playerUID + "')"
            );
            if (rs4.next()) {
                player.setAttribute("StatsItemsSoldValue", rs4.getInt("itemsSold"));
            }

            // Money Spent (Purchases as buyer)
            ResultSet rs5 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SUM(TotalAmount) AS moneySpent " +
                "FROM MarketTransactions WHERE TransactionType = 'Purchase' AND PlayerUID = '" + playerUID + "'"
            );
            if (rs5.next()) {
                float moneySpent = rs5.getFloat("moneySpent");
                player.setAttribute("StatsMoneySpentValue", moneySpent);
                player.setAttribute("StatsMoneySpentFormatted", currencyFormatter.format(moneySpent));
            }

            // Money Earned (Purchases as seller)
            ResultSet rs6 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SUM(TotalAmount) AS moneyEarned " +
                "FROM MarketTransactions WHERE TransactionType = 'Purchase' AND SellerUID = '" + playerUID + "'"
            );
            if (rs6.next()) {
                float moneyEarned = rs6.getFloat("moneyEarned");
                player.setAttribute("StatsMoneyEarnedValue", moneyEarned);
                player.setAttribute("StatsMoneyEarnedFormatted", currencyFormatter.format(moneyEarned));
            }

            // Total Sales (QuantitySold for Purchases and Sales)
            ResultSet rs7 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SUM(QuantitySold) AS totalSales " +
                "FROM MarketTransactions WHERE (TransactionType = 'Purchase' AND PlayerUID = '" + playerUID + "') " +
                "OR (TransactionType = 'Sale' AND PlayerUID = '" + playerUID + "')"
            );
            if (rs7.next()) {
                player.setAttribute("StatsTotalSalesValue", rs7.getInt("totalSales"));
            }

            // Most Popular Item (by frequency in Purchases and Sales for this player)
            ResultSet rs8 = OnlineDataBaseAccess1.executeQuery(
                "SELECT ItemName, COUNT(*) as count " +
                "FROM MarketTransactions WHERE (TransactionType = 'Purchase' AND PlayerUID = '" + playerUID + "') " +
                "OR (TransactionType = 'Sale' AND PlayerUID = '" + playerUID + "') " +
                "GROUP BY ItemName ORDER BY count DESC LIMIT 1"
            );
            if (rs8.next()) {
                player.setAttribute("StatsMostPopularItemValue", rs8.getString("ItemName"));
            }

            // Top Player Earners (Top 3 players by total earnings from Purchases as sellers)
            ResultSet rs9 = OnlineDataBaseAccess1.executeQuery(
                "SELECT SellerUID, SUM(TotalAmount) as totalEarned " +
                "FROM MarketTransactions WHERE TransactionType = 'Purchase' " +
                "GROUP BY SellerUID ORDER BY totalEarned DESC LIMIT 3"
            );
            StringBuilder topEarners = new StringBuilder();
            while (rs9.next()) {
                String sellerUID = rs9.getString("SellerUID");
                float earned = rs9.getFloat("totalEarned");

                // Fetch Seller Name from MarketListings using SellerUID
                String sellerName = getSellerNameFromMarketListings(sellerUID);
                if (sellerName == null || sellerName.isEmpty()) {
                    // Fallback to PlayerMoney or online player if no name found in MarketListings
                    sellerName = getSellerNameFallback(sellerUID);
                }

                topEarners.append(sellerName).append(": ").append(currencyFormatter.format(earned)).append("\n");
            }
            player.setAttribute("StatsTopEarnersValue", topEarners.toString().trim());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the stats UI panel with the loaded player statistics.
     * @param statsPanel The UI panel to display stats in.
     * @param player The player whose stats are being displayed.
     */
    public void updateStatsUI(UILabel statsPanel, Player player) {
        UILabel statsListingCount = (UILabel) player.getAttribute("StatsListingCount");
        UILabel statsWallet = (UILabel) player.getAttribute("StatsWallet");
        UILabel statsStock = (UILabel) player.getAttribute("StatsStock");
        UILabel statsItemsSold = (UILabel) player.getAttribute("StatsItemsSold");
        UILabel statsMoneySpent = (UILabel) player.getAttribute("StatsMoneySpent");
        UILabel statsMoneyEarned = (UILabel) player.getAttribute("StatsMoneyEarned");
        UILabel statsTotalSales = (UILabel) player.getAttribute("StatsTotalSales");
        UILabel statsMostPopularItem = (UILabel) player.getAttribute("StatsMostPopularItem");
        UILabel statsTopEarners = (UILabel) player.getAttribute("StatsTopEarners");

        if (statsListingCount != null) {
            Integer listingCount = (Integer) player.getAttribute("StatsListingCountValue");
            statsListingCount.setText("Total Listings: " + (listingCount != null ? listingCount : 0));
        }
        if (statsWallet != null) {
            Float walletValue = (Float) player.getAttribute("StatsWalletValue");
            statsWallet.setText("Wallet: $" + (walletValue != null ? walletValue : 0));
        }
        if (statsStock != null) {
            Integer stockValue = (Integer) player.getAttribute("StatsStockValue");
            statsStock.setText("Current Stock: " + (stockValue != null ? stockValue : 0));
        }
        if (statsItemsSold != null) {
            Integer itemsSold = (Integer) player.getAttribute("StatsItemsSoldValue");
            statsItemsSold.setText("Total Items Sold: " + (itemsSold != null ? itemsSold : 0));
        }
        if (statsMoneySpent != null) {
            String moneySpentFormatted = (String) player.getAttribute("StatsMoneySpentFormatted");
            statsMoneySpent.setText("Money Spent: " + (moneySpentFormatted != null ? moneySpentFormatted : "$0.00"));
        }
        if (statsMoneyEarned != null) {
            String moneyEarnedFormatted = (String) player.getAttribute("StatsMoneyEarnedFormatted");
            statsMoneyEarned.setText("Money Earned: " + (moneyEarnedFormatted != null ? moneyEarnedFormatted : "$0.00"));
        }
        if (statsTotalSales != null) {
            Integer totalSales = (Integer) player.getAttribute("StatsTotalSalesValue");
            statsTotalSales.setText("Total Sales: " + (totalSales != null ? totalSales : "Loading..."));
        }
        if (statsMostPopularItem != null) {
            String mostPopularItem = (String) player.getAttribute("StatsMostPopularItemValue");
            statsMostPopularItem.setText("Most Popular Item: " + (mostPopularItem != null ? mostPopularItem : "Loading..."));
        }
        if (statsTopEarners != null) {
            String topEarners = (String) player.getAttribute("StatsTopEarnersValue");
            statsTopEarners.setText("Top Player Earners:\n" + (topEarners != null && !topEarners.isEmpty() ? topEarners : "Loading..."));
        }
    }

    /**
     * Retrieves a seller's name from MarketListings based on their UID.
     * @param sellerUID The UID of the seller.
     * @return The seller's name from MarketListings, or null if not found.
     */
    private String getSellerNameFromMarketListings(String sellerUID) {
        try (ResultSet rs = OnlineDataBaseAccess1.executeQuery(
                "SELECT ItemOwnerName FROM MarketListings WHERE ItemOwnerUID = '" + sellerUID + "' LIMIT 1")) {
            if (rs.next()) {
                return rs.getString("ItemOwnerName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fallback method to retrieve a seller's name from PlayerMoney or online player if not found in MarketListings.
     * @param sellerUID The UID of the seller.
     * @return The seller's name, or the UID if no name is found.
     */
    private String getSellerNameFallback(String sellerUID) {
        // Check if player is online first
        Player seller = Server.getPlayerByUID(sellerUID);
        if (seller != null) {
            return seller.getName();
        }

        // Fallback to PlayerMoney table
        try (ResultSet rs = OnlineDataBaseAccess1.executeQuery(
                "SELECT PlayerName FROM PlayerMoney WHERE PlayerUID = '" + sellerUID + "' LIMIT 1")) {
            if (rs.next()) {
                String name = rs.getString("PlayerName");
                return name != null && !name.isEmpty() ? name : sellerUID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If all else fails, return UID
        return sellerUID;
    }
}