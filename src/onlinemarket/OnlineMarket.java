package onlinemarket;

import java.sql.SQLException;
import java.util.ArrayList;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.database.Database;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UILabel;

/**
 * Main plugin class for the OnlineMarket system, handling plugin lifecycle,
 * player events, and UI interactions.
 *
 * @author yahgi
 */
public class OnlineMarket extends Plugin implements Listener {

    // Static database connections for accessing the OnlineMarket data (set by CreateDatabase)
    protected static Database OnlineDataBaseAccess1;
    protected static Database OnlineDataBaseAccess2;
    protected static Database OnlineDataBaseAccess3;

    // Stores the name of the world (not currently used)
    private static String WorldName;

    // Reference to the plugin instance
    protected static Plugin plugin;

    /**
     * Called when the plugin is enabled. Initializes the database structure
     * and registers event listeners.
     */
    @Override
    public void onEnable() {
        new CreateDatabase(this).createDatabaseStructure(); // Set up database tables
        registerEventListener(this); // Register this class as an event listener
    }

    /**
     * Handles the event when a player connects to the server.
     * Initializes player attributes and sets up the menu.
     *
     * @param event The PlayerConnectEvent triggered when a player joins.
     * @throws SQLException If a database operation fails.
     */
    @EventMethod
    public void onPlayerConnect(PlayerConnectEvent event) throws SQLException {
        Player player = event.getPlayer(); // Get the connecting player

        // Initialize arrays to track note selection buttons and selected notes
        ArrayList<UILabel> onlineMarketSelectButtons = new ArrayList<>();
        ArrayList<String> onlineMarketSelected = new ArrayList<>();

        // Store arrays in player attributes for later use
        player.setAttribute("OnlineMarketSelectBottons", onlineMarketSelectButtons);
        player.setAttribute("OnlineMarketSelected", onlineMarketSelected);

        ArrayList<UILabel> buyItemIcons = new ArrayList<>();
        player.setAttribute("buyItemIcons", buyItemIcons);
        //buyItemIcon

        // Create and display the initial menu for the player
        new CreateMenu(this).Menu(event);
    }

    /**
     * Handles UI element click events for the OnlineMarket system.
     * Delegates control to the MarketMenuControl class.
     *
     * @param event The PlayerUIElementClickEvent triggered by a UI click.
     * @throws SQLException If a database operation fails.
     */
    @EventMethod
    public void onPlayerUIEClick(PlayerUIElementClickEvent event) throws SQLException {
        MarketMenuControl menuControl = new MarketMenuControl(this); // Create menu controller
        menuControl.MenuControl(event); // Process the click event
    }

    /**
     * Called when the plugin is disabled. Logs a disable message.
     */
    @Override
    public void onDisable() {
        System.out.println(getName() + " plugin disabled!"); // Log plugin disable message
    }
}