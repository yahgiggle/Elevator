package onlinemarket;

import java.sql.SQLException;
import net.risingworld.api.Plugin;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;

/**
 * Handles logic for toggling and managing the stats panel in the Online Market.
 */
public class MarketStatsControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketStatsControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to toggling the stats panel (show, back, exit).
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails (not directly used here, but included for consistency).
     */
    public void handleStatsEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        int eventID = event.getUIElement().getID();

        int OnlineMarketStatsButtonID = (int) player.getAttribute("OnlineMarketStatsButtonID");
        int OnlineMarketStatsBackButtonID = (int) player.getAttribute("OnlineMarketStatsBackButtonID");
        int OnlineMarketStatsExitButtonID = (int) player.getAttribute("OnlineMarketStatsExitButtonID");

        // Handle toggling the stats panel visibility
        if (eventID == OnlineMarketStatsButtonID) {
            UILabel onlineMarketStatsPanel = (UILabel) player.getAttribute("OnlineMarketStatsPanel");
            UILabel onlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");
            if (onlineMarketStatsPanel != null && onlineMarketTextField != null) {
                boolean isVisible = (Boolean) player.getAttribute("StatsPanelVisible");

                // Toggle visibility of stats panel and market text field
                onlineMarketStatsPanel.setVisible(!isVisible);
                onlineMarketTextField.setVisible(isVisible);
                player.setAttribute("StatsPanelVisible", !isVisible);

                // Load and update stats if showing the panel
                if (!isVisible) {
                    menuControl.getStats().loadPlayerStats(player);
                    menuControl.getStats().updateStatsUI(onlineMarketStatsPanel, player);
                } else {
                    // Reset stats labels to "Loading..." when hiding
                    resetStatsLabels(player);
                }
            } else {
                player.sendTextMessage("Stats Panel or Market Menu not found!");
            }
        }

        // Handle going back from stats panel to market menu
        if (eventID == OnlineMarketStatsBackButtonID) {
            UILabel onlineMarketStatsPanel = (UILabel) player.getAttribute("OnlineMarketStatsPanel");
            if (onlineMarketStatsPanel != null) {
                onlineMarketStatsPanel.setVisible(false);
                player.setAttribute("StatsPanelVisible", false);
                menuControl.getOpenOnlineMarket().OpenMenu(player);
            }
        }

        // Handle exiting the stats panel entirely
        if (eventID == OnlineMarketStatsExitButtonID) {
            UILabel onlineMarketStatsPanel = (UILabel) player.getAttribute("OnlineMarketStatsPanel");
            UILabel onlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");
            if (onlineMarketStatsPanel != null) onlineMarketStatsPanel.setVisible(false);
            if (onlineMarketTextField != null) onlineMarketTextField.setVisible(false);
            player.setAttribute("StatsPanelVisible", false);
            player.setMouseCursorVisible(false);
        }
    }

    /**
     * Resets the stats UI labels to "Loading..." when the stats panel is hidden.
     * @param player The player whose stats UI is being reset.
     */
    private void resetStatsLabels(Player player) {
        UILabel statsListingCount = (UILabel) player.getAttribute("StatsListingCount");
        UILabel statsSalesValue = (UILabel) player.getAttribute("StatsSalesValue");
        UILabel statsWallet = (UILabel) player.getAttribute("StatsWallet");
        UILabel statsPopularItem = (UILabel) player.getAttribute("StatsPopularItem");
        UILabel statsTopEarners = (UILabel) player.getAttribute("StatsTopEarners");

        if (statsListingCount != null) statsListingCount.setText("Total Listings: Loading...");
        if (statsSalesValue != null) statsSalesValue.setText("Total Sales: Loading...");
        if (statsWallet != null) statsWallet.setText("Wallet: Loading...");
        if (statsPopularItem != null) statsPopularItem.setText("Most Popular Item: Loading...");
        if (statsTopEarners != null) statsTopEarners.setText("Top Player Earners: Loading...");
    }
}