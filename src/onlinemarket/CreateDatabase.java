package onlinemarket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.risingworld.api.Plugin;
import net.risingworld.api.World;

/**
 * Initializes and manages the SQLite database structure for the OnlineMarket plugin.
 *
 * @author yahgi
 */
public class CreateDatabase extends OnlineMarket {
    // Reference to the plugin instance, set once in constructor
    private final Plugin plugin;

    /**
     * Constructor taking the plugin instance as parameter.
     *
     * @param plugin The Rising World plugin instance.
     */
    public CreateDatabase(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the database structure and initializes base data for OnlineMarket.
     * Sets up tables for market listings and player money, and tracks database version.
     */
    public void createDatabaseStructure() {
        // Log database loading to console
        System.out.println("-- OnlineMarket Database Loaded --");

        // Get the current world name and construct database path
        String worldName = World.getName();
        String dbPath = plugin.getPath() + "/" + worldName + "/database.db";

        // Establish SQLite connection using the constructed path
        OnlineDataBaseAccess1 = getSQLiteConnection(dbPath);
        OnlineDataBaseAccess2 = getSQLiteConnection(dbPath);
        OnlineDataBaseAccess3 = getSQLiteConnection(dbPath);

        // Create MarketListings table if it doesn't exist
        // Stores market items with their details, including Featured Listings support
        OnlineDataBaseAccess1.execute(
            "CREATE TABLE IF NOT EXISTS `MarketListings` (" +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + // Unique identifier for each item
                "`ItemName` VARCHAR(64), " +                          // Item title/description
                "`ItemPrice` INTEGER, " +                            // Price of the item in coins
                "`ItemOwnerName` VARCHAR(64), " +                    // Name of item owner
                "`ItemOwnerUID` VARCHAR(36), " +                     // Unique ID of item owner (UUID format)
                "`ItemID` INTEGER, " +                               // Item type identifier from Rising World
                "`ItemCategory` VARCHAR(64), " +                     // Item category
                "`ItemVariants` INTEGER, " +                         // Item variants
                "`ItemQuantity` INTEGER DEFAULT 0, " +              // Quantity (0 for unlimited server items)
                "`IsUnlimited` INTEGER DEFAULT 0, " +                // 0 for limited, 1 for unlimited (server items)
                "`IsFeatured` INTEGER DEFAULT 0, " +                 // 0 for not featured, 1 for featured
                "`FeatureEndTime` TIMESTAMP NULL" +                  // End time for featured status, null if not featured
            ");"
        );
        
        // Create PlayerMoney table if it doesn't exist
        // Tracks player coin balances with name and UID
        OnlineDataBaseAccess1.execute(
            "CREATE TABLE IF NOT EXISTS `PlayerMoney` (" +
                "`PlayerUID` VARCHAR(36) PRIMARY KEY NOT NULL, " +  // Player's unique ID (stable key)
                "`PlayerName` VARCHAR(64), " +                      // Player's current name (for reference)
                "`Money` INTEGER DEFAULT 0" +                      // Player's coin balance
            ");"
        );

        // Create DataBaseVersion table if it doesn't exist
        // Tracks database schema version
        OnlineDataBaseAccess1.execute(
            "CREATE TABLE IF NOT EXISTS `DataBaseVersion` (" +
                "`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + // Version record ID
                "`Version` INTEGER" +                                 // Database version number
            ");"
        );

        // Check if version record exists and initialize if not
        try (ResultSet result = OnlineDataBaseAccess1.executeQuery(
                "SELECT * FROM `DataBaseVersion` WHERE ID = '1'")) {
            if (!result.next()) {
                // If no version record exists, insert initial version 0
                OnlineDataBaseAccess1.executeUpdate(
                    "INSERT INTO `DataBaseVersion` (Version) VALUES ('0');"
                );
            }
        } catch (SQLException ex) {
            // Log any database errors with stack trace
            Logger.getLogger(CreateDatabase.class.getName())
                  .log(Level.SEVERE, "Database initialization failed", ex);
        }
    }
}