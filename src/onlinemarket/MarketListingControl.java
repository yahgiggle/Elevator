package onlinemarket;

import java.sql.SQLException;
import net.risingworld.api.Plugin;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.objects.Item;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.UITextField;

import static onlinemarket.OnlineMarket.OnlineDataBaseAccess1;
import static onlinemarket.OnlineMarket.OnlineDataBaseAccess2;

/**
 * Handles logic for listing items to the Online Market.
 */
public class MarketListingControl extends OnlineMarket {
    private final Plugin plugin;
    private final MarketMenuControl menuControl;

    /**
     * Constructor initializing the plugin and parent coordinator.
     * @param plugin The Rising World plugin instance.
     * @param menuControl The parent MarketMenuControl for shared utilities.
     */
    public MarketListingControl(Plugin plugin, MarketMenuControl menuControl) {
        this.plugin = plugin;
        this.menuControl = menuControl;
    }

    /**
     * Processes events related to listing items (add, toggle unlimited, cancel).
     * @param event The UI click event triggered by a player.
     * @throws SQLException If a database operation fails.
     */
    public void handleListingEvent(PlayerUIElementClickEvent event) throws SQLException {
        Player player = event.getPlayer();
        String PlayerUID = player.getUID();
        String PlayerName = player.getName();
        int eventID = event.getUIElement().getID();

        int onlineMarketAddItemButtonID = (int) player.getAttribute("OnlineMarketAddItemButtonID");
        int addItemConfirmButtonID = (int) player.getAttribute("AddItemConfirmButtonID");
        int addItemUnlimitedToggleID = (int) player.getAttribute("AddItemUnlimitedToggleID");
        int addItemCancelButtonID = (int) player.getAttribute("AddItemCancelButtonID");

        // Handle "Add Item" button click to show the listing popup
        if (eventID == onlineMarketAddItemButtonID) {
            UILabel OnlineMarketTextField = (UILabel) player.getAttribute("OnlineMarketTextField");
            OnlineMarketTextField.setVisible(false);

            Item equippedItem = player.getEquippedItem();
            if (equippedItem == null) {
                player.sendYellMessage("Hold an item to add it to the market!", 3, true);
                player.setMouseCursorVisible(false);
                return;
            }

            int stack = equippedItem.getStack();
            String itemName;
            int variant = equippedItem.getVariant();

            // Determine item type and set attributes
            if (equippedItem instanceof Item.ConstructionItem construction) {
                player.setAttribute("SellItemID", construction.getConstructionID());
                byte GetItemID = construction.getConstructionDefinition().id;
                player.setAttribute("gotthisID", GetItemID);
                itemName = construction.getConstructionName();
                player.setAttribute("ItemCategorys", "construction");
            } else if (equippedItem instanceof Item.ObjectItem object) {
                player.setAttribute("SellItemID", object.getObjectID());
                short GetItemID = object.getObjectID();
                player.setAttribute("gotthisID", GetItemID);
                itemName = object.getObjectName();
                player.setAttribute("ItemCategorys", "objectkit");
            } else if (equippedItem instanceof Item.ClothingItem clothing) {
                player.setAttribute("SellItemID", clothing.getClothingID());
                short GetItemID = clothing.getClothingID();
                player.setAttribute("gotthisID", GetItemID);
                itemName = clothing.getClothingName();
                player.setAttribute("ItemCategorys", "clothing");
            } else if (equippedItem instanceof Item.BlueprintItem blueprint) {
                player.setAttribute("SellItemID", blueprint.getBlueprintID());
                long GetItemID = blueprint.getBlueprintID();
                player.setAttribute("gotthisID", GetItemID);
                itemName = blueprint.getBlueprintName();
                player.setAttribute("ItemCategorys", "blueprint");
            } else {
                itemName = equippedItem.getName();
                short GetItemID = equippedItem.getDefinition().id;
                player.setAttribute("gotthisID", GetItemID);
                player.setAttribute("ItemCategorys", itemName);
                player.setAttribute("SellItemID", equippedItem.getDefinition().id);
            }

            // Store item details for later use
            player.setAttribute("SellItemName", itemName);
            player.setAttribute("SellItemAmount", stack);
            player.setAttribute("SellItemVariant", variant);

            // Setup the add item popup UI
            UILabel addItemPopupPanel = (UILabel) player.getAttribute("AddItemPopupPanel");
            UILabel addItemInfo = (UILabel) player.getAttribute("AddItemInfo");
            UITextField AddItemPriceField = (UITextField) player.getAttribute("AddItemPriceField");
            UILabel addItemUnlimitedToggle = (UILabel) player.getAttribute("AddItemUnlimitedToggle");

            addItemInfo.setText("<b>Item:</b> " + itemName + "\n<b>Amount:</b> " + stack);
            AddItemPriceField.setText("");
            addItemUnlimitedToggle.setText("<b>Unlimited Stock: OFF</b>");
            addItemUnlimitedToggle.setBackgroundColor(0.5f, 0.0f, 0.0f, 0.9f);
            player.setAttribute("UnlimitedStock", false);
            addItemPopupPanel.setVisible(true);
            player.setMouseCursorVisible(true);
        }

        // Handle "Confirm" button to list the item
        if (eventID == addItemConfirmButtonID) {
            UILabel addItemPopupPanel = (UILabel) player.getAttribute("AddItemPopupPanel");
            UITextField AddItemPriceField = (UITextField) player.getAttribute("AddItemPriceField");

            AddItemPriceField.getCurrentText(player, (String priceText) -> {
                if (priceText == null || priceText.trim().isEmpty()) {
                    player.sendYellMessage("Please enter a price!", 3, true);
                    return;
                }

                try {
                    int price = Integer.parseInt(priceText);
                    String itemName = (String) player.getAttribute("SellItemName");
                    int stack = (int) player.getAttribute("SellItemAmount");
                    String GetCategory = (String) player.getAttribute("ItemCategorys");
                    boolean unlimited = (boolean) player.getAttribute("UnlimitedStock");
                    int variant = (int) player.getAttribute("SellItemVariant");
                    int itemQuantity = stack;
                    String ownerName = unlimited ? "Server" : PlayerName;
                    String ownerUID = unlimited ? "SERVER_UID" : PlayerUID;

                    // Add item to MarketListings table
                    OnlineDataBaseAccess2.executeUpdate("INSERT INTO `MarketListings` (ItemName, ItemPrice, ItemOwnerName, ItemOwnerUID, ItemID, ItemCategory, ItemVariants, ItemQuantity, IsUnlimited) " +
                        "VALUES ('" + itemName + "', '" + price + "', '" + ownerName + "', '" + ownerUID + "', '" + player.getAttribute("gotthisID") + "', '" + GetCategory + "', '" + variant + "', '" + itemQuantity + "', '" + (unlimited ? 1 : 0) + "')");

                    // Remove item from inventory if not unlimited
                    if (!unlimited) {
                        Item equippedItem = player.getEquippedItem();
                        player.setAttribute("itemRemoved", false);

                        if (equippedItem != null) {
                            String equippedName = getItemName(equippedItem);
                            player.sendTextMessage("Equipped item name: " + equippedName + ", Expected: " + itemName);

                            if (isItemMatch(equippedItem, itemName, stack, variant)) {
                                removeItemFromInventory(player, itemName, stack, variant);
                                if (Boolean.FALSE.equals(player.getAttribute("itemRemoved"))) {
                                    removeItemFromQuickslot(player, itemName, stack, variant);
                                }
                                if (Boolean.FALSE.equals(player.getAttribute("itemRemoved"))) {
                                    player.sendYellMessage("Couldn’t find " + stack + " " + itemName + " (variant " + variant + ") to remove — contact an admin!", 3, true);
                                    System.out.println("Failed to remove item " + itemName + " (stack: " + stack + ", variant: " + variant + ") for player " + PlayerName);
                                }
                            } else {
                                player.sendYellMessage("Equipped item mismatch — expected " + itemName + " (stack: " + stack + ", variant: " + variant + "). Contact an admin!", 3, true);
                                System.out.println("Item mismatch for " + PlayerName + ": Expected " + itemName + " (stack: " + stack + ", variant: " + variant + "), Got: " + equippedName);
                            }
                        } else {
                            player.sendYellMessage("No item equipped — expected " + itemName + " (stack: " + stack + ", variant: " + variant + "). Contact an admin!", 3, true);
                            System.out.println("No item equipped for " + PlayerName + ": Expected " + itemName + " (stack: " + stack + ", variant: " + variant + ")");
                        }

                        // Log "StockAdded" transaction for non-unlimited items
                        float totalAmount = price * stack;
                        String sql = "INSERT INTO `MarketTransactions` (PlayerUID, ItemName, QuantitySold, Price, TotalAmount, TransactionType, SellerUID) " +
                                     "VALUES ('" + PlayerUID + "', '" + itemName + "', " + stack + ", " + price + ", " + totalAmount + ", 'StockAdded', '" + PlayerUID + "')";
                        OnlineDataBaseAccess1.executeUpdate(sql);
                    }

                    // Finalize listing process
                    addItemPopupPanel.setVisible(false);
                    player.setMouseCursorVisible(false);
                    menuControl.getOpenOnlineMarket().OpenMenu(player);
                    player.sendYellMessage("Added " + (unlimited ? "unlimited " : stack + " ") + itemName + " to market stock for " + price + " coins!", 5, true);
                    menuControl.refreshStatsUI(player);
                } catch (NumberFormatException ex) {
                    player.sendYellMessage("Invalid price use whole numbers!", 3, true);
                }
            });
        }

        // Handle toggling unlimited stock option (admin only)
        if (eventID == addItemUnlimitedToggleID) {
            if (!player.isAdmin()) {
                player.sendYellMessage("Sorry, only admins can add unlimited items!", 3, true);
                return;
            }
            UILabel addItemUnlimitedToggle = (UILabel) player.getAttribute("AddItemUnlimitedToggle");
            boolean currentState = (boolean) player.getAttribute("UnlimitedStock");
            player.setAttribute("UnlimitedStock", !currentState);
            addItemUnlimitedToggle.setText("<b>Unlimited Stock: " + (!currentState ? "ON" : "OFF") + "</b>");
            addItemUnlimitedToggle.setBackgroundColor(!currentState ? 0.0f : 0.5f, !currentState ? 0.5f : 0.0f, 0.0f, 0.9f);
        }

        // Handle canceling the add item popup
        if (eventID == addItemCancelButtonID) {
            UILabel addItemPopup = (UILabel) player.getAttribute("AddItemPopupPanel");
            if (addItemPopup != null) {
                addItemPopup.setVisible(false);
            }
            menuControl.getOpenOnlineMarket().OpenMenu(player);
            player.setMouseCursorVisible(true);
        }
    }

    /**
     * Retrieves the name of an equipped item based on its type.
     * @param item The item to get the name for.
     * @return The name of the item.
     */
    private String getItemName(Item item) {
        if (item instanceof Item.ObjectItem) return ((Item.ObjectItem) item).getObjectName();
        if (item instanceof Item.ConstructionItem) return ((Item.ConstructionItem) item).getConstructionName();
        if (item instanceof Item.ClothingItem) return ((Item.ClothingItem) item).getClothingName();
        if (item instanceof Item.BlueprintItem) return ((Item.BlueprintItem) item).getBlueprintName();
        return item.getName();
    }

    /**
     * Checks if an equipped item matches the expected name, stack, and variant.
     * @param item The equipped item to check.
     * @param expectedName The expected name of the item.
     * @param stack The expected stack size.
     * @param variant The expected variant ID.
     * @return True if the item matches, false otherwise.
     */
    private boolean isItemMatch(Item item, String expectedName, int stack, int variant) {
        String itemName = getItemName(item);
        return itemName.equals(expectedName) && item.getStack() == stack && item.getVariant() == variant;
    }

    /**
     * Removes an item from the player's inventory if it matches the criteria.
     * @param player The player whose inventory is being modified.
     * @param itemName The name of the item to remove.
     * @param stack The stack size to remove.
     * @param variant The variant ID of the item.
     */
    private void removeItemFromInventory(Player player, String itemName, int stack, int variant) {
        for (int i = 0; i < 36; i++) {
            Item slotItem = player.getInventory().getItem(i, Inventory.SlotType.Inventory);
            if (slotItem != null) {
                String slotName = getItemName(slotItem);
                if (slotName.equals(itemName) && slotItem.getStack() == stack && slotItem.getVariant() == variant) {
                    player.getInventory().removeItem(i, Inventory.SlotType.Inventory, stack);
                    player.setAttribute("itemRemoved", true);
                    player.sendYellMessage("Removed " + stack + " " + itemName + " (variant " + variant + ") from inventory slot " + i + " for sale.", 3, true);
                    break;
                }
            }
        }
    }

    /**
     * Removes an item from the player's quickslot if it matches the criteria.
     * @param player The player whose quickslot is being modified.
     * @param itemName The name of the item to remove.
     * @param stack The stack size to remove.
     * @param variant The variant ID of the item.
     */
    private void removeItemFromQuickslot(Player player, String itemName, int stack, int variant) {
        int quickslotFocus = player.getInventory().getQuickslotFocus();
        Item quickslotItem = player.getInventory().getItem(quickslotFocus, Inventory.SlotType.Quickslot);
        if (quickslotItem != null) {
            String quickslotName = getItemName(quickslotItem);
            if (quickslotName.equals(itemName) && quickslotItem.getStack() == stack && quickslotItem.getVariant() == variant) {
                player.getInventory().removeItem(quickslotFocus, Inventory.SlotType.Quickslot, stack);
                player.setAttribute("itemRemoved", true);
                player.sendYellMessage("Removed " + stack + " " + itemName + " (variant " + variant + ") from quickslot for sale.", 3, true);
            }
        }
    }
}

