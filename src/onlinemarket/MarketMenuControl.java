package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import net.risingworld.api.Plugin;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;

/**
 * Main coordinator class for handling market menu interactions.
 * Delegates UI click events to specialized control classes and provides shared utilities.
 */
public class MarketMenuControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketListingControl listingControl;
    private final MarketPurchaseControl purchaseControl;
    private final MarketResourceControl resourceControl;
    private final MarketEditControl editControl;
    private final MarketNavigationControl navigationControl;
    private final MarketStatsControl statsControl;
    private final OpenOnlineMarket openOnlineMarket;
    private final Stats stats;

    /**
     * Constructor initializing the plugin and all control classes.
     * @param plugin The Rising World plugin instance.
     */
    public MarketMenuControl(Plugin plugin) {
        this.plugin = plugin;
        this.openOnlineMarket = new OpenOnlineMarket(plugin); // Single instance for UI refresh
        this.stats = new Stats(plugin); // Single instance for stats management
        this.listingControl = new MarketListingControl(plugin, this);
        this.purchaseControl = new MarketPurchaseControl(plugin, this);
        this.resourceControl = new MarketResourceControl(plugin, this);
        this.editControl = new MarketEditControl(plugin, this);
        this.navigationControl = new MarketNavigationControl(plugin, this);
        this.statsControl = new MarketStatsControl(plugin, this);
    }

    /**
     * Main event handler for UI clicks, routing events to appropriate control classes.
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void MenuControl(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        int eventID = event.getUIElement() != null ? event.getUIElement().getID() : 0;

        int OpenOnlineMarketMenuID = (int) player.getAttribute("OpenOnlineMarketMenuID");
        int OnlineMarketExitButtonID = (int) player.getAttribute("OnlineMarketExitButtonID");

        // Handle opening the market menu
        if (eventID == OpenOnlineMarketMenuID) {
            player.hideInventory();
            player.setAttribute("Search", "SELECT count(*) FROM `MarketListings`");
            player.setAttribute("SearchRsult", "MarketListings");
            openOnlineMarket.OpenMenu(player);
            updateWalletDisplay(player);
            return;
        }

        // Handle closing the market menu
        if (eventID == OnlineMarketExitButtonID) {
            UILabel OnlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");
            OnlineMarketTextField.setVisible(false);
            player.setMouseCursorVisible(false);
            return;
        }

        // Delegate to specialized control classes
        listingControl.handleListingEvent(event);
        purchaseControl.handlePurchaseEvent(event);
        resourceControl.handleResourceEvent(event);
        editControl.handleEditEvent(event);
        navigationControl.handleNavigationEvent(event);
        statsControl.handleStatsEvent(event);
    }

    /**
     * Updates the player's wallet display UI with their current money balance.
     * @param player The player whose wallet is being updated.
     */
    public void updateWalletDisplay(Player player) {
        try (ResultSet result = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + player.getUID() + "'")) {
            float wallet = result.next() ? result.getFloat("Money") : 0;
            NumberFormat walletFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            String formattedWallet = walletFormatter.format(wallet);
            UILabel playerMoneyLabel = (UILabel) player.getAttribute("PlayerMoneyLabel");
            if (playerMoneyLabel != null) {
                playerMoneyLabel.setText("<b>Cash:</b> " + formattedWallet);
            }
            player.setAttribute("Wallet", formattedWallet);
        } catch (SQLException e) {
            player.sendYellMessage("Error fetching wallet: " + e.getMessage(), 3, true);
            e.printStackTrace();
        }
    }

    /**
     * Adjusts a player's money balance in the database and updates their stats attribute.
     * @param player The player whose money is being adjusted.
     * @param amount The amount to add (positive) or subtract (negative) from the player's money.
     * @throws SQLException If the database update fails.
     */
    public void updatePlayerMoney(Player player, float amount) throws SQLException {
        String playerUID = player.getUID();
        ResultSet rs = OnlineDataBaseAccess1.executeQuery("SELECT Money FROM `PlayerMoney` WHERE PlayerUID = '" + playerUID + "'");
        float currentMoney = rs.next() ? rs.getFloat("Money") : 0;
        float newMoney = currentMoney + amount;
        OnlineDataBaseAccess1.executeUpdate("UPDATE `PlayerMoney` SET Money = " + newMoney + " WHERE PlayerUID = '" + playerUID + "'");
        player.setAttribute("StatsWalletValue", newMoney);
    }

    /**
     * Refreshes the stats UI if it’s visible, using the shared Stats instance.
     * @param player The player whose stats UI is being refreshed.
     */
    public void refreshStatsUI(Player player) {
        Boolean statsPanelVisible = (Boolean) player.getAttribute("StatsPanelVisible");
        if (statsPanelVisible != null && statsPanelVisible) {
            stats.loadPlayerStats(player);
            UILabel statsPanel = (UILabel) player.getAttribute("OnlineMarketStatsPanel");
            if (statsPanel != null) {
                stats.updateStatsUI(statsPanel, player);
            }
        }
    }

    /**
     * Getter for the shared OpenOnlineMarket instance.
     * @return The OpenOnlineMarket instance for refreshing the market UI.
     */
    public OpenOnlineMarket getOpenOnlineMarket() {
        return openOnlineMarket;
    }

    /**
     * Getter for the shared Stats instance.
     * @return The Stats instance for managing player statistics.
     */
    public Stats getStats() {
        return stats;
    }
}