package onlinemarket;

import java.sql.SQLException;
import net.risingworld.api.Plugin;
import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.UITarget;
import net.risingworld.api.ui.UITextField;
import net.risingworld.api.ui.style.Font;
import net.risingworld.api.ui.style.ScaleMode;
import net.risingworld.api.ui.style.TextAnchor;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess2;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess3;
import static onlinemarket.OnlineMarket.plugin;

/**
 * Creates and initializes the UI elements for the OnlineMarket menu system.
 *
 * @author yahgi
 */
public class CreateMenu extends OnlineMarket {

    public CreateMenu(Plugin plugin) {
        this.plugin = plugin;
    }

    public void Menu(PlayerConnectEvent event) throws SQLException {
        Player player = event.getPlayer();
        TextureAsset noteBackground = TextureAsset.loadFromFile(plugin.getPath() + "/Assets/NoteBackGround.JPG");

        // Main OnlineMarket menu container
        UIElement onlineMarketMenu = new UIElement();
        player.addUIElement(onlineMarketMenu, UITarget.Inventory);
        onlineMarketMenu.setSize(200, 65, false);
        onlineMarketMenu.setClickable(false);
        onlineMarketMenu.setPosition(31, 5, true);
        onlineMarketMenu.setBorderEdgeRadius(5.0f, false);
        onlineMarketMenu.setBorder(3);
        onlineMarketMenu.setBorderColor(888);
        onlineMarketMenu.style.backgroundImage.set(noteBackground);
        onlineMarketMenu.style.backgroundImageScaleMode.set(ScaleMode.StretchToFill);
        onlineMarketMenu.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.95f);
        onlineMarketMenu.setVisible(true);
        player.setAttribute("OnlineMarketMenu", onlineMarketMenu);

        // "Open Online Market" button
        UILabel openOnlineMarketMenu = new UILabel();
        openOnlineMarketMenu.setClickable(true);
        openOnlineMarketMenu.setText("<b> Online Market </b>");
        openOnlineMarketMenu.setFont(Font.Medieval);
        openOnlineMarketMenu.style.textAlign.set(TextAnchor.MiddleCenter);
        openOnlineMarketMenu.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        openOnlineMarketMenu.setFontSize(16);
        openOnlineMarketMenu.setSize(180, 45, false);
        openOnlineMarketMenu.setBorder(2);
        openOnlineMarketMenu.setBorderColor(999);
        openOnlineMarketMenu.setBackgroundColor(500);
        openOnlineMarketMenu.setPosition(7, 6, false);
        openOnlineMarketMenu.style.borderBottomWidth.set(5);
        openOnlineMarketMenu.hoverStyle.backgroundColor.set(0.2f, 0.2f, 0.2f, 0.9f);
        openOnlineMarketMenu.hoverStyle.borderBottomWidth.set(5);
        openOnlineMarketMenu.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketMenu.addChild(openOnlineMarketMenu);
        player.setAttribute("OpenOnlineMarketMenuID", openOnlineMarketMenu.getID());

        // Main result area for the OnlineMarket
        UILabel onlineMarketTextField = new UILabel();
        player.addUIElement(onlineMarketTextField);
        onlineMarketTextField.setVisible(false);
        onlineMarketTextField.setSize(300, 700, false);
        onlineMarketTextField.setPosition(42.5f, 20, true);
        onlineMarketTextField.setBorderEdgeRadius(5.0f, false);
        onlineMarketTextField.setBorder(3);
        onlineMarketTextField.setBorderColor(888);
        onlineMarketTextField.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.9f);
        onlineMarketTextField.setPickable(true);
        player.setAttribute("OnlineMarketTextField", onlineMarketTextField);

        // Player Money Label
        UILabel playerMoneyLabel = new UILabel();
        playerMoneyLabel.setText("<b>Cash:</b> $0.00");
        playerMoneyLabel.style.textAlign.set(TextAnchor.MiddleLeft);
        playerMoneyLabel.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        playerMoneyLabel.setFontSize(12);
        playerMoneyLabel.setSize(250, 20, false);
        playerMoneyLabel.setPosition(100, 5, false); // Adjusted to avoid overlap
        onlineMarketTextField.addChild(playerMoneyLabel);
        player.setAttribute("PlayerMoneyLabel", playerMoneyLabel);

        // "Search" button
        UILabel onlineMarketSearchButton = new UILabel();
        onlineMarketSearchButton.setText("<b> Search </b>");
        onlineMarketSearchButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketSearchButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketSearchButton.setFontSize(10);
        onlineMarketSearchButton.setBorder(2);
        onlineMarketSearchButton.setBorderColor(999);
        onlineMarketSearchButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketSearchButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketSearchButton.setSize(250, 30, false);
        onlineMarketSearchButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketSearchButton.setPosition(24.5f, 60, false);
        onlineMarketSearchButton.style.borderBottomWidth.set(5);
        onlineMarketSearchButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketSearchButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketSearchButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketSearchButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketSearchButton);
        player.setAttribute("OnlineMarketSearchButton", onlineMarketSearchButton);
        player.setAttribute("OnlineMarketSearchButtonID", onlineMarketSearchButton.getID());

        // "Exit" button for the main menu
        UILabel onlineMarketExitButton = new UILabel();
        onlineMarketExitButton.setClickable(true);
        onlineMarketExitButton.setFontColor(50, 999, 999, 50);
        onlineMarketExitButton.setRichTextEnabled(true);
        onlineMarketExitButton.setFontSize(18);
        onlineMarketExitButton.setText("<b> X </b>");
        onlineMarketExitButton.hoverStyle.color.set(1.0f, 0.0f, 0.0f, 1.0f);
        onlineMarketExitButton.setPosition(270, 2, false);
        onlineMarketTextField.addChild(onlineMarketExitButton);
        player.setAttribute("OnlineMarketExitButton", onlineMarketExitButton);
        player.setAttribute("OnlineMarketExitButtonID", onlineMarketExitButton.getID());

        // "Next" Button
        UILabel onlineMarketNextButton = new UILabel();
        onlineMarketNextButton.setText("<b> Next > </b>");
        onlineMarketNextButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketNextButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketNextButton.setFontSize(10);
        onlineMarketNextButton.setBorder(2);
        onlineMarketNextButton.setBorderColor(999);
        onlineMarketNextButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketNextButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketNextButton.setSize(124, 25, false);
        onlineMarketNextButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketNextButton.setPosition(150.5f, 665, false); // Unchanged
        onlineMarketNextButton.style.borderBottomWidth.set(5);
        onlineMarketNextButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketNextButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketNextButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketNextButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketNextButton);
        player.setAttribute("OnlineMarketNextButton", onlineMarketNextButton);
        player.setAttribute("OnlineMarketNextButtonID", onlineMarketNextButton.getID());

        // "Back" Button
        UILabel onlineMarketBackButton = new UILabel();
        onlineMarketBackButton.setText("<b> < Back </b>");
        onlineMarketBackButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketBackButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketBackButton.setFontSize(10);
        onlineMarketBackButton.setBorder(2);
        onlineMarketBackButton.setBorderColor(999);
        onlineMarketBackButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketBackButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketBackButton.setSize(124, 25, false);
        onlineMarketBackButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketBackButton.setPosition(24.5f, 665, false); // Unchanged
        onlineMarketBackButton.style.borderBottomWidth.set(5);
        onlineMarketBackButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketBackButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketBackButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketBackButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketBackButton);
        player.setAttribute("OnlineMarketBackButton", onlineMarketBackButton);
        player.setAttribute("OnlineMarketBackButtonID", onlineMarketBackButton.getID());

        // Search input text field
        UITextField onlineMarketID = new UITextField();
        onlineMarketID.setClickable(true);
        onlineMarketID.setMaxCharacters(55);
        onlineMarketID.setText("Search");
        onlineMarketID.setBorderColor(143);
        onlineMarketID.setSize(250, 31, false);
        onlineMarketID.setPosition(24.5f, 25, false);
        onlineMarketID.getCurrentText(player, (String text) -> {
            player.setAttribute("OnlineMarketIDText", text);
            player.sendTextMessage("OnlineMarketID: " + text);
        });
        onlineMarketTextField.addChild((UIElement) onlineMarketID);
        player.addUIElement((UIElement) onlineMarketID);
        player.setAttribute("OnlineMarketID", onlineMarketID);
        player.setAttribute("OnlineMarketIDID", onlineMarketID.getID());

        // "Add Item" button (All Users)
        UILabel onlineMarketAddItemButton = new UILabel();
        onlineMarketAddItemButton.setText("<b> < Add Item > </b>");
        onlineMarketAddItemButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketAddItemButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketAddItemButton.setFontSize(10);
        onlineMarketAddItemButton.setBorder(2);
        onlineMarketAddItemButton.setBorderColor(999);
        onlineMarketAddItemButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketAddItemButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketAddItemButton.setSize(250, 30, false);
        onlineMarketAddItemButton.setBackgroundColor(0.6f, 0.6f, 0.2f, 0.9f);
        onlineMarketAddItemButton.setPosition(24.5f, 91f, false);
        onlineMarketAddItemButton.style.borderBottomWidth.set(5);
        onlineMarketAddItemButton.hoverStyle.backgroundColor.set(0.8f, 0.8f, 0.2f, 0.9f);
        onlineMarketAddItemButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketAddItemButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketAddItemButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketAddItemButton);
        player.setAttribute("OnlineMarketAddItemButton", onlineMarketAddItemButton);
        player.setAttribute("OnlineMarketAddItemButtonID", onlineMarketAddItemButton.getID());

        // "My Listings" Button (unchanged)
        UILabel onlineMarketMyListingsButton = new UILabel();
        onlineMarketMyListingsButton.setText("<b> My Listings </b>");
        onlineMarketMyListingsButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketMyListingsButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketMyListingsButton.setFontSize(10);
        onlineMarketMyListingsButton.setBorder(2);
        onlineMarketMyListingsButton.setBorderColor(999);
        onlineMarketMyListingsButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketMyListingsButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketMyListingsButton.setSize(124, 30, false);
        onlineMarketMyListingsButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketMyListingsButton.setPosition(24.5f, 121, false);
        onlineMarketMyListingsButton.style.borderBottomWidth.set(5);
        onlineMarketMyListingsButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketMyListingsButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketMyListingsButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketMyListingsButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketMyListingsButton);
        player.setAttribute("OnlineMarketMyListingsButton", onlineMarketMyListingsButton);
        player.setAttribute("OnlineMarketMyListingsButtonID", onlineMarketMyListingsButton.getID());

        // "Market Listings" Button (was Server Listings)
        UILabel onlineMarketMarketListingsButton = new UILabel();
        onlineMarketMarketListingsButton.setText("<b> Market Listings </b>");
        onlineMarketMarketListingsButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketMarketListingsButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketMarketListingsButton.setFontSize(10);
        onlineMarketMarketListingsButton.setBorder(2);
        onlineMarketMarketListingsButton.setBorderColor(999);
        onlineMarketMarketListingsButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketMarketListingsButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketMarketListingsButton.setSize(124, 30, false);
        onlineMarketMarketListingsButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketMarketListingsButton.setPosition(150.5f, 121, false);
        onlineMarketMarketListingsButton.style.borderBottomWidth.set(5);
        onlineMarketMarketListingsButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketMarketListingsButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketMarketListingsButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketMarketListingsButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketMarketListingsButton);
        player.setAttribute("OnlineMarketMarketListingsButton", onlineMarketMarketListingsButton);
        player.setAttribute("OnlineMarketMarketListingsButtonID", onlineMarketMarketListingsButton.getID());

        // "All Listings" Button
        UILabel onlineMarketAllListingsButton = new UILabel();
        onlineMarketAllListingsButton.setText("<b> All Listings </b>");
        onlineMarketAllListingsButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketAllListingsButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketAllListingsButton.setFontSize(10);
        onlineMarketAllListingsButton.setBorder(2);
        onlineMarketAllListingsButton.setBorderColor(999);
        onlineMarketAllListingsButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketAllListingsButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketAllListingsButton.setSize(250, 30, false); // Full width
        onlineMarketAllListingsButton.setBackgroundColor(0.2f, 0.4f, 0.2f, 0.9f);
        onlineMarketAllListingsButton.setPosition(24.5f, 151, false); // Below My/Market at 121 + 30
        onlineMarketAllListingsButton.style.borderBottomWidth.set(5);
        onlineMarketAllListingsButton.hoverStyle.backgroundColor.set(0.2f, 0.6f, 0.2f, 0.9f);
        onlineMarketAllListingsButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketAllListingsButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketAllListingsButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketAllListingsButton);
        player.setAttribute("OnlineMarketAllListingsButton", onlineMarketAllListingsButton);
        player.setAttribute("OnlineMarketAllListingsButtonID", onlineMarketAllListingsButton.getID());

        // "Stats" Button
        UILabel onlineMarketStatsButton = new UILabel();
        onlineMarketStatsButton.setText("<b>Stats</b>");
        onlineMarketStatsButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketStatsButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketStatsButton.setFontSize(10);
        onlineMarketStatsButton.setBorder(2);
        onlineMarketStatsButton.setBorderColor(999);
        onlineMarketStatsButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketStatsButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketStatsButton.setSize(250, 30, false);
        onlineMarketStatsButton.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.9f); // Dark gray, matching OpenOnlineMarket buttons
        onlineMarketStatsButton.setPosition(24.5f, 600, false); // Below All Listings at 151 + 20 (adjusted for visibility)
        onlineMarketStatsButton.style.borderBottomWidth.set(5);
        onlineMarketStatsButton.hoverStyle.backgroundColor.set(0.2f, 0.2f, 0.2f, 0.9f);
        onlineMarketStatsButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketStatsButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketStatsButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketStatsButton);
        player.setAttribute("OnlineMarketStatsButton", onlineMarketStatsButton);
        player.setAttribute("OnlineMarketStatsButtonID", onlineMarketStatsButton.getID());

        // Stats Panel
        UILabel onlineMarketStatsPanel = new UILabel();
        player.addUIElement(onlineMarketStatsPanel);
        onlineMarketStatsPanel.setVisible(false); // Initially hidden
        onlineMarketStatsPanel.setSize(300, 600, false);
        onlineMarketStatsPanel.setPosition(0, 0, true); // Adjust based on your layout
        onlineMarketStatsPanel.setBackgroundColor(0.1f, 0.1f, 0.1f, 0.9f);
        onlineMarketStatsPanel.setBorder(2);
        onlineMarketStatsPanel.setBorderColor(999);
        onlineMarketStatsPanel.setBorderEdgeRadius(5.0f, false);
        onlineMarketStatsPanel.setPickable(true);
        onlineMarketTextField.addChild(onlineMarketStatsPanel);
        player.setAttribute("OnlineMarketStatsPanel", onlineMarketStatsPanel);
        player.setAttribute("StatsPanelVisible", false); // Initialize StatsPanelVisible here

        // Stats Labels (initial placeholders, updated by Stats class)
        UILabel statsListingCount = new UILabel("Total Listings: Loading...");
        statsListingCount.setPosition(10, 10, false);
        statsListingCount.setSize(280, 20, false);
        statsListingCount.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        statsListingCount.setFontSize(12);
        statsListingCount.style.textAlign.set(TextAnchor.MiddleLeft); // Left-aligned
        onlineMarketStatsPanel.addChild(statsListingCount);
        player.setAttribute("StatsListingCount", statsListingCount);

        UILabel statsTotalSales = new UILabel("Total Sales: Loading...");
        statsTotalSales.setPosition(10, 40, false);
        statsTotalSales.setSize(280, 20, false);
        statsTotalSales.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        statsTotalSales.setFontSize(12);
        statsTotalSales.style.textAlign.set(TextAnchor.MiddleLeft); // Left-aligned
        onlineMarketStatsPanel.addChild(statsTotalSales);
        player.setAttribute("StatsTotalSales", statsTotalSales);

        UILabel statsWallet = new UILabel("Wallet: Loading...");
        statsWallet.setPosition(10, 70, false);
        statsWallet.setSize(280, 20, false);
        statsWallet.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        statsWallet.setFontSize(12);
        statsWallet.style.textAlign.set(TextAnchor.MiddleLeft); // Left-aligned
        onlineMarketStatsPanel.addChild(statsWallet);
        player.setAttribute("StatsWallet", statsWallet);

        UILabel statsMostPopularItem = new UILabel("Most Popular Item: Loading...");
        statsMostPopularItem.setPosition(10, 100, false);
        statsMostPopularItem.setSize(280, 20, false);
        statsMostPopularItem.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        statsMostPopularItem.setFontSize(12);
        statsMostPopularItem.style.textAlign.set(TextAnchor.MiddleLeft); // Left-aligned
        onlineMarketStatsPanel.addChild(statsMostPopularItem);
        player.setAttribute("StatsMostPopularItem", statsMostPopularItem);

        UILabel statsTopEarners = new UILabel("Top Player Earners: Loading...");
        statsTopEarners.setPosition(10, 130, false);
        statsTopEarners.setSize(280, 20, false);
        statsTopEarners.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        statsTopEarners.setFontSize(12);
        statsTopEarners.style.textAlign.set(TextAnchor.MiddleLeft); // Left-aligned
        onlineMarketStatsPanel.addChild(statsTopEarners);
        player.setAttribute("StatsTopEarners", statsTopEarners);

        // "Back" Button in Stats Panel
        UILabel onlineMarketStatsBackButton = new UILabel();
        onlineMarketStatsBackButton.setText("<b>Back</b>");
        onlineMarketStatsBackButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketStatsBackButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketStatsBackButton.setFontSize(10);
        onlineMarketStatsBackButton.setBorder(2);
        onlineMarketStatsBackButton.setBorderColor(999);
        onlineMarketStatsBackButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketStatsBackButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketStatsBackButton.setSize(145, 30, false);
        onlineMarketStatsBackButton.setBackgroundColor(0.1f, 0.1f, 0.4f, 0.9f); // Dark gray
        onlineMarketStatsBackButton.setPosition(2.5f, 560, false); // Bottom-left, adjusted to fit
        onlineMarketStatsBackButton.style.borderBottomWidth.set(5);
        onlineMarketStatsBackButton.hoverStyle.backgroundColor.set(0.2f, 0.2f, 0.6f, 0.9f);
        onlineMarketStatsBackButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketStatsBackButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketStatsBackButton.setClickable(true);
        onlineMarketStatsPanel.addChild(onlineMarketStatsBackButton);
        player.setAttribute("OnlineMarketStatsBackButton", onlineMarketStatsBackButton);
        player.setAttribute("OnlineMarketStatsBackButtonID", onlineMarketStatsBackButton.getID());

        // "Exit" Button in Stats Panel
        UILabel onlineMarketStatsExitButton = new UILabel();
        onlineMarketStatsExitButton.setText("<b>Exit</b>");
        onlineMarketStatsExitButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketStatsExitButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketStatsExitButton.setFontSize(10);
        onlineMarketStatsExitButton.setBorder(2);
        onlineMarketStatsExitButton.setBorderColor(999);
        onlineMarketStatsExitButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketStatsExitButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketStatsExitButton.setSize(145, 30, false);
        onlineMarketStatsExitButton.setBackgroundColor(0.4f, 0.1f, 0.1f, 0.9f); // Dark gray
        onlineMarketStatsExitButton.setPosition(150, 560, false); // Bottom-right, adjusted to fit
        onlineMarketStatsExitButton.style.borderBottomWidth.set(5);
        onlineMarketStatsExitButton.hoverStyle.backgroundColor.set(0.6f, 0.2f, 0.2f, 0.9f);
        onlineMarketStatsExitButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketStatsExitButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketStatsExitButton.setClickable(true);
        onlineMarketStatsPanel.addChild(onlineMarketStatsExitButton);
        player.setAttribute("OnlineMarketStatsExitButton", onlineMarketStatsExitButton);
        player.setAttribute("OnlineMarketStatsExitButtonID", onlineMarketStatsExitButton.getID());

        // "Sell Stone" Button
        UILabel onlineMarketSellStoneButton = new UILabel();
        onlineMarketSellStoneButton.setText("<b> < Sell Stone > </b>");
        onlineMarketSellStoneButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketSellStoneButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketSellStoneButton.setFontSize(10);
        onlineMarketSellStoneButton.setBorder(2);
        onlineMarketSellStoneButton.setBorderColor(999);
        onlineMarketSellStoneButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketSellStoneButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketSellStoneButton.setSize(124, 30, false);
        onlineMarketSellStoneButton.setBackgroundColor(0.6f, 0.6f, 0.2f, 0.9f);
        onlineMarketSellStoneButton.setPosition(24.5f, 635, false); // Was 630, lowered by 50px for stats panel
        onlineMarketSellStoneButton.style.borderBottomWidth.set(5);
        onlineMarketSellStoneButton.hoverStyle.backgroundColor.set(0.8f, 0.8f, 0.2f, 0.9f);
        onlineMarketSellStoneButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketSellStoneButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketSellStoneButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketSellStoneButton);
        player.setAttribute("OnlineMarketSellStoneButton", onlineMarketSellStoneButton);
        player.setAttribute("OnlineMarketSellStoneButtonID", onlineMarketSellStoneButton.getID());

        // "Sell Lumber" Button
        UILabel onlineMarketSellLumberButton = new UILabel();
        onlineMarketSellLumberButton.setText("<b> < Sell Lumber > </b>");
        onlineMarketSellLumberButton.style.textAlign.set(TextAnchor.MiddleCenter);
        onlineMarketSellLumberButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        onlineMarketSellLumberButton.setFontSize(10);
        onlineMarketSellLumberButton.setBorder(2);
        onlineMarketSellLumberButton.setBorderColor(999);
        onlineMarketSellLumberButton.style.borderBottomColor.set(0.0f, 0.0f, 0.2f, 0.9f);
        onlineMarketSellLumberButton.setBorderEdgeRadius(5.0f, false);
        onlineMarketSellLumberButton.setSize(124, 30, false);
        onlineMarketSellLumberButton.setBackgroundColor(0.6f, 0.6f, 0.2f, 0.9f);
        onlineMarketSellLumberButton.setPosition(150.5f, 635, false); // Was 630, lowered by 50px for stats panel
        onlineMarketSellLumberButton.style.borderBottomWidth.set(5);
        onlineMarketSellLumberButton.hoverStyle.backgroundColor.set(0.8f, 0.8f, 0.2f, 0.9f);
        onlineMarketSellLumberButton.hoverStyle.borderBottomWidth.set(5);
        onlineMarketSellLumberButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        onlineMarketSellLumberButton.setClickable(true);
        onlineMarketTextField.addChild(onlineMarketSellLumberButton);
        player.setAttribute("OnlineMarketSellLumberButton", onlineMarketSellLumberButton);
        player.setAttribute("OnlineMarketSellLumberButtonID", onlineMarketSellLumberButton.getID());

        // Add Item Popup Panel
        UILabel addItemPopupPanel = new UILabel();
        player.addUIElement(addItemPopupPanel);
        addItemPopupPanel.setVisible(false);
        addItemPopupPanel.setSize(300, 240, false);
        addItemPopupPanel.setPosition(42.5f, 40, true);
        addItemPopupPanel.setBackgroundColor(0.1f, 0.1f, 0.1f, 1.0f);
        addItemPopupPanel.setBorder(2);
        addItemPopupPanel.setBorderColor(999);
        addItemPopupPanel.setPickable(true);
        player.setAttribute("AddItemPopupPanel", addItemPopupPanel);

        // Item Info Label in Add Popup
        UILabel addItemInfo = new UILabel();
        addItemInfo.setPosition(10, 10, false);
        addItemInfo.setText("<b>Item:</b> None\n<b>Amount:</b> 0");
        addItemInfo.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        addItemInfo.setFontSize(12);
        addItemPopupPanel.addChild(addItemInfo);
        player.setAttribute("AddItemInfo", addItemInfo);

        // Price Input Field in Add Popup
        UITextField addItemPriceField = new UITextField();
        addItemPriceField.setClickable(true);
        addItemPriceField.setMaxCharacters(10);
        addItemPriceField.setText("Enter sale price (e.g., 5)");
        addItemPriceField.setBorderColor(143);
        addItemPriceField.setSize(280, 30, false);
        addItemPriceField.setPosition(10, 60, false);
        addItemPriceField.getCurrentText(player, (String text) -> {
            player.setAttribute("AddItemPriceText", text);
        });
        addItemPopupPanel.addChild((UIElement) addItemPriceField);
        player.addUIElement((UIElement) addItemPriceField);
        player.setAttribute("AddItemPriceField", addItemPriceField);
        player.setAttribute("AddItemPriceFieldID", addItemPriceField.getID());

        // Unlimited Toggle Button in Add Popup
        UILabel addItemUnlimitedToggle = new UILabel();
        addItemUnlimitedToggle.setPosition(10, 100, false);
        addItemUnlimitedToggle.setSize(280, 30, false);
        addItemUnlimitedToggle.setText("<b>Unlimited Stock: OFF</b>");
        addItemUnlimitedToggle.style.textAlign.set(TextAnchor.MiddleCenter);
        addItemUnlimitedToggle.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        addItemUnlimitedToggle.setFontSize(10);
        addItemUnlimitedToggle.setBorder(2);
        addItemUnlimitedToggle.setBorderColor(999);
        addItemUnlimitedToggle.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
        addItemUnlimitedToggle.style.borderBottomWidth.set(5);
        addItemUnlimitedToggle.setClickable(true);
        addItemUnlimitedToggle.setVisible(true);
        addItemPopupPanel.addChild(addItemUnlimitedToggle);
        player.setAttribute("AddItemUnlimitedToggle", addItemUnlimitedToggle);
        player.setAttribute("AddItemUnlimitedToggleID", addItemUnlimitedToggle.getID());
        player.setAttribute("UnlimitedStock", false);

        // Confirm Button in Add Popup
        UILabel addItemConfirmButton = new UILabel();
        addItemConfirmButton.setPosition(10, 140, false);
        addItemConfirmButton.setSize(280, 30, false);
        addItemConfirmButton.setText("<b>Confirm</b>");
        addItemConfirmButton.style.textAlign.set(TextAnchor.MiddleCenter);
        addItemConfirmButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        addItemConfirmButton.setFontSize(10);
        addItemConfirmButton.setBorder(2);
        addItemConfirmButton.setBorderColor(999);
        addItemConfirmButton.setBackgroundColor(0.0f, 0.5f, 0.0f, 0.9f);
        addItemConfirmButton.style.borderBottomWidth.set(5);
        addItemConfirmButton.hoverStyle.backgroundColor.set(0.0f, 0.7f, 0.0f, 0.9f);
        addItemConfirmButton.hoverStyle.borderBottomWidth.set(5);
        addItemConfirmButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        addItemConfirmButton.setClickable(true);
        addItemConfirmButton.setVisible(true);
        addItemPopupPanel.addChild(addItemConfirmButton);
        player.setAttribute("AddItemConfirmButton", addItemConfirmButton);
        player.setAttribute("AddItemConfirmButtonID", addItemConfirmButton.getID());

        // Cancel Button in Add Popup
        UILabel addItemCancelButton = new UILabel();
        addItemCancelButton.setPosition(10, 180, false);
        addItemCancelButton.setSize(280, 30, false);
        addItemCancelButton.setText("<b>Cancel</b>");
        addItemCancelButton.style.textAlign.set(TextAnchor.MiddleCenter);
        addItemCancelButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        addItemCancelButton.setFontSize(10);
        addItemCancelButton.setBorder(2);
        addItemCancelButton.setBorderColor(999);
        addItemCancelButton.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
        addItemCancelButton.style.borderBottomWidth.set(5);
        addItemCancelButton.hoverStyle.backgroundColor.set(0.7f, 0.0f, 0.0f, 0.9f);
        addItemCancelButton.hoverStyle.borderBottomWidth.set(5);
        addItemCancelButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        addItemCancelButton.setClickable(true);
        addItemCancelButton.setVisible(true);
        addItemPopupPanel.addChild(addItemCancelButton);
        player.setAttribute("AddItemCancelButton", addItemCancelButton);
        player.setAttribute("AddItemCancelButtonID", addItemCancelButton.getID());

        // Buy Item Popup Panel
        UILabel buyItemPopupPanel = new UILabel();
        player.addUIElement(buyItemPopupPanel);
        buyItemPopupPanel.setVisible(false);
        buyItemPopupPanel.setSize(300, 280, false);
        buyItemPopupPanel.setPosition(42.5f, 40, true);
        buyItemPopupPanel.setBackgroundColor(0.1f, 0.1f, 0.1f, 1.0f);
        buyItemPopupPanel.setBorder(2);
        buyItemPopupPanel.setBorderColor(999);
        buyItemPopupPanel.setPickable(true);
        player.setAttribute("BuyItemPopupPanel", buyItemPopupPanel);
          // buyInfo ????
        // Item Info Label (shifted right)
        UILabel buyItemInfo = new UILabel();
        buyItemInfo.setPosition(70, 10, false); // Moved from 10 to 70 to accommodate icon
        buyItemInfo.setSize(220, 80, false); // Reduced from 280 to 220 width
        buyItemInfo.setText("<b>Item:</b> [ItemName]\n<b>Price:</b> $[Price] | <b>Quantity:</b> [Quantity]\n<b>Seller:</b> [ItemOwnerName]");
        buyItemInfo.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        buyItemInfo.setFontSize(12);
        buyItemPopupPanel.addChild(buyItemInfo);
        player.setAttribute("BuyItemInfo", buyItemInfo);

        // Edit Button in Buy Popup (for admins/owners)
        UILabel buyEditButton = new UILabel();
        buyEditButton.setPosition(10, 80, false);
        buyEditButton.setSize(280, 30, false);
        buyEditButton.setText("<b>Edit</b>");
        buyEditButton.style.textAlign.set(TextAnchor.MiddleCenter);
        buyEditButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        buyEditButton.setFontSize(10);
        buyEditButton.setBorder(2);
        buyEditButton.setBorderColor(999);
        buyEditButton.setBackgroundColor(0.0f, 0.0f, 0.5f, 0.9f);
        buyEditButton.style.borderBottomWidth.set(5);
        buyEditButton.hoverStyle.backgroundColor.set(0.0f, 0.0f, 0.7f, 0.9f);
        buyEditButton.hoverStyle.borderBottomWidth.set(5);
        buyEditButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        buyEditButton.setClickable(true);
        buyItemPopupPanel.addChild(buyEditButton);
        player.setAttribute("BuyEditButton", buyEditButton);
        player.setAttribute("BuyEditButtonID", buyEditButton.getID());

        // Cancel Button in Buy Popup
        UILabel buyCancelButton = new UILabel();
        buyCancelButton.setPosition(10, 120, false);
        buyCancelButton.setSize(130, 30, false);
        buyCancelButton.setText("<b>Cancel</b>");
        buyCancelButton.style.textAlign.set(TextAnchor.MiddleCenter);
        buyCancelButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        buyCancelButton.setFontSize(10);
        buyCancelButton.setBorder(2);
        buyCancelButton.setBorderColor(999);
        buyCancelButton.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
        buyCancelButton.style.borderBottomWidth.set(5);
        buyCancelButton.hoverStyle.backgroundColor.set(0.7f, 0.0f, 0.0f, 0.9f);
        buyCancelButton.hoverStyle.borderBottomWidth.set(5);
        buyCancelButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        buyCancelButton.setClickable(true);
        buyItemPopupPanel.addChild(buyCancelButton);
        player.setAttribute("BuyCancelButton", buyCancelButton);
        player.setAttribute("BuyCancelButtonID", buyCancelButton.getID());

        // Buy (Confirm) Button in Buy Popup
        UILabel buyConfirmButton = new UILabel();
        buyConfirmButton.setPosition(160, 120, false);
        buyConfirmButton.setSize(130, 30, false);
        buyConfirmButton.setText("<b>Buy</b>");
        buyConfirmButton.style.textAlign.set(TextAnchor.MiddleCenter);
        buyConfirmButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        buyConfirmButton.setFontSize(10);
        buyConfirmButton.setBorder(2);
        buyConfirmButton.setBorderColor(999);
        buyConfirmButton.setBackgroundColor(0.0f, 0.5f, 0.0f, 0.9f);
        buyConfirmButton.style.borderBottomWidth.set(5);
        buyConfirmButton.hoverStyle.backgroundColor.set(0.0f, 0.7f, 0.0f, 0.9f);
        buyConfirmButton.hoverStyle.borderBottomWidth.set(5);
        buyConfirmButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        buyConfirmButton.setClickable(true);
        buyItemPopupPanel.addChild(buyConfirmButton);
        player.setAttribute("BuyConfirmButton", buyConfirmButton);
        player.setAttribute("BuyConfirmButtonID", buyConfirmButton.getID());

        // Edit Item Popup Panel
        UILabel editItemPopupPanel = new UILabel();
        player.addUIElement(editItemPopupPanel);
        editItemPopupPanel.setVisible(false);
        editItemPopupPanel.setSize(300, 240, false);
        editItemPopupPanel.setPosition(42.5f, 40, true);
        editItemPopupPanel.setBackgroundColor(0.1f, 0.1f, 0.1f, 1.0f);
        editItemPopupPanel.setBorder(2);
        editItemPopupPanel.setBorderColor(999);
        editItemPopupPanel.setPickable(true);
        player.setAttribute("EditItemPopupPanel", editItemPopupPanel);

        // Item Info Label in Edit Popup
        UILabel editItemInfo = new UILabel();
        editItemInfo.setPosition(10, 10, false);
        editItemInfo.setSize(280, 60, false);
        editItemInfo.setText("<b>Item:</b> [ItemName]\n<b>Quantity:</b> [Quantity]");
        editItemInfo.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editItemInfo.setFontSize(12);
        editItemPopupPanel.addChild(editItemInfo);
        player.setAttribute("EditItemInfo", editItemInfo);

        // Price Input Field in Edit Popup
        UITextField editItemPriceField = new UITextField();
        editItemPriceField.setClickable(true);
        editItemPriceField.setMaxCharacters(10);
        editItemPriceField.setText("Enter new price");
        editItemPriceField.setBorderColor(143);
        editItemPriceField.setSize(280, 30, false);
        editItemPriceField.setPosition(10, 80, false);
        editItemPriceField.getCurrentText(player, (String text) -> {
            player.setAttribute("EditItemPriceText", text);
        });
        editItemPopupPanel.addChild((UIElement) editItemPriceField);
        player.addUIElement((UIElement) editItemPriceField);
        player.setAttribute("EditItemPriceField", editItemPriceField);

        // Unlimited Toggle Button in Edit Popup
        UILabel editItemUnlimitedToggle = new UILabel();
        editItemUnlimitedToggle.setPosition(10, 120, false);
        editItemUnlimitedToggle.setSize(280, 30, false);
        editItemUnlimitedToggle.setText("<b>Unlimited Stock: OFF</b>");
        editItemUnlimitedToggle.style.textAlign.set(TextAnchor.MiddleCenter);
        editItemUnlimitedToggle.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editItemUnlimitedToggle.setFontSize(10);
        editItemUnlimitedToggle.setBorder(2);
        editItemUnlimitedToggle.setBorderColor(999);
        editItemUnlimitedToggle.setBackgroundColor(0.5f, 0.5f, 0.5f, 0.9f);
        editItemUnlimitedToggle.style.borderBottomWidth.set(5);
        editItemUnlimitedToggle.setClickable(false);
        editItemPopupPanel.addChild(editItemUnlimitedToggle);
        player.setAttribute("EditItemUnlimitedToggle", editItemUnlimitedToggle);
        player.setAttribute("EditItemUnlimitedToggleID", editItemUnlimitedToggle.getID());

        // Confirm Button in Edit Popup
        UILabel editConfirmButton = new UILabel();
        editConfirmButton.setPosition(10, 160, false);
        editConfirmButton.setSize(70, 30, false);
        editConfirmButton.setText("<b>Confirm</b>");
        editConfirmButton.style.textAlign.set(TextAnchor.MiddleCenter);
        editConfirmButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editConfirmButton.setFontSize(10);
        editConfirmButton.setBorder(2);
        editConfirmButton.setBorderColor(999);
        editConfirmButton.setBackgroundColor(0.0f, 0.5f, 0.0f, 0.9f);
        editConfirmButton.style.borderBottomWidth.set(5);
        editConfirmButton.hoverStyle.backgroundColor.set(0.0f, 0.7f, 0.0f, 0.9f);
        editConfirmButton.hoverStyle.borderBottomWidth.set(5);
        editConfirmButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        editConfirmButton.setClickable(true);
        editItemPopupPanel.addChild(editConfirmButton);
        player.setAttribute("EditConfirmButton", editConfirmButton);
        player.setAttribute("EditConfirmButtonID", editConfirmButton.getID());

        // Remove Button in Edit Popup
        UILabel editRemoveButton = new UILabel();
        editRemoveButton.setPosition(80, 160, false);
        editRemoveButton.setSize(70, 30, false);
        editRemoveButton.setText("<b>Remove</b>");
        editRemoveButton.style.textAlign.set(TextAnchor.MiddleCenter);
        editRemoveButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editRemoveButton.setFontSize(10);
        editRemoveButton.setBorder(2);
        editRemoveButton.setBorderColor(999);
        editRemoveButton.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
        editRemoveButton.style.borderBottomWidth.set(5);
        editRemoveButton.hoverStyle.backgroundColor.set(0.7f, 0.0f, 0.0f, 0.9f);
        editRemoveButton.hoverStyle.borderBottomWidth.set(5);
        editRemoveButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        editRemoveButton.setClickable(true);
        editItemPopupPanel.addChild(editRemoveButton);
        player.setAttribute("EditRemoveButton", editRemoveButton);
        player.setAttribute("EditRemoveButtonID", editRemoveButton.getID());

        // Cancel Button in Edit Popup
        UILabel editCancelButton = new UILabel();
        editCancelButton.setPosition(150, 160, false);
        editCancelButton.setSize(70, 30, false);
        editCancelButton.setText("<b>Cancel</b>");
        editCancelButton.style.textAlign.set(TextAnchor.MiddleCenter);
        editCancelButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editCancelButton.setFontSize(10);
        editCancelButton.setBorder(2);
        editCancelButton.setBorderColor(999);
        editCancelButton.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
        editCancelButton.style.borderBottomWidth.set(5);
        editCancelButton.hoverStyle.backgroundColor.set(0.7f, 0.0f, 0.0f, 0.9f);
        editCancelButton.hoverStyle.borderBottomWidth.set(5);
        editCancelButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        editCancelButton.setClickable(true);
        editItemPopupPanel.addChild(editCancelButton);
        player.setAttribute("EditCancelButton", editCancelButton);
        player.setAttribute("EditCancelButtonID", editCancelButton.getID());

        // Exit Button in Edit Popup
        UILabel editExitButton = new UILabel();
        editExitButton.setPosition(220, 160, false);
        editExitButton.setSize(70, 30, false);
        editExitButton.setText("<b>Exit</b>");
        editExitButton.style.textAlign.set(TextAnchor.MiddleCenter);
        editExitButton.setFontColor(9.0f, 9.0f, 9.0f, 1.0f);
        editExitButton.setFontSize(10);
        editExitButton.setBorder(2);
        editExitButton.setBorderColor(999);
        editExitButton.setBackgroundColor(0.2f, 0.2f, 0.2f, 0.9f);
        editExitButton.style.borderBottomWidth.set(5);
        editExitButton.hoverStyle.backgroundColor.set(0.4f, 0.4f, 0.4f, 0.9f);
        editExitButton.hoverStyle.borderBottomWidth.set(5);
        editExitButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        editExitButton.setClickable(true);
        editItemPopupPanel.addChild(editExitButton);
        player.setAttribute("EditExitButton", editExitButton);
        player.setAttribute("EditExitButtonID", editExitButton.getID());

        // Feature Button in Edit Popup
        UILabel editFeatureButton = new UILabel();
        editFeatureButton.setPosition(10, 200, false);
        editFeatureButton.setSize(280, 30, false);
        editFeatureButton.setText("<b>Bump Listing to Top ($1)</b>");
        editFeatureButton.style.textAlign.set(TextAnchor.MiddleCenter);
        editFeatureButton.setFontColor(0.0f, 0.0f, 0.0f, 1.0f); // Black text as per your change
        editFeatureButton.setFontSize(10);
        editFeatureButton.setBorder(2);
        editFeatureButton.setBorderColor(999);
        editFeatureButton.setBackgroundColor(1.0f, 0.843f, 0.0f, 0.9f); // Gold background
        editFeatureButton.style.borderBottomWidth.set(5);
        editFeatureButton.hoverStyle.backgroundColor.set(1.0f, 0.9f, 0.2f, 0.9f); // Brighter gold on hover
        editFeatureButton.hoverStyle.borderBottomWidth.set(5);
        editFeatureButton.hoverStyle.borderBottomColor.set(0.1f, 0.1f, 0.9f, 0.9f);
        editFeatureButton.setClickable(true);
        editItemPopupPanel.addChild(editFeatureButton);
        player.setAttribute("EditFeatureButton", editFeatureButton);
        player.setAttribute("EditFeatureButtonID", editFeatureButton.getID());
    }
}

