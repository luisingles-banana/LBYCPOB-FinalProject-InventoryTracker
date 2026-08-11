package ph.dlsu.lbycpob.inventorytracker.items.Menu;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Donation;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Donor;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.ItemFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/** Menu shown after entering a specific database: the Dynamic Inventory Dashboard. */
public class ItemMenu extends BaseMenu {
    private final Database database;

    public ItemMenu(Database database) {
        this.database = database;
    }

    @Override
    protected void printMenu() {
        IO.println("---------------------------------------------------");
        IO.println("  " + database.getName().toUpperCase());
        IO.println("---------------------------------------------------");
        IO.println(database.getDashboardSummary());
        IO.println("""
    1. View All Items (FIFO - soonest expiry first)
    2. Log a Donation / Add Item
    3. Dispatch Item
    4. View Items Expiring Soon
    5. View Donation Log
    0. Back to Inventory Tracker
                """);
        IO.println("Choose an option: ");
    }

    @Override
    protected boolean userChoice(int choice) {
        switch (choice) {
            case 1 -> viewAllItems();
            case 2 -> logDonation();
            case 3 -> dispatchItem();
            case 4 -> viewExpiringSoon();
            case 5 -> viewDonationLog();
            case 0 -> { return false; }
            default -> IO.println("Invalid option, try again.");
        }
        return true;
    }

    private void viewAllItems() {
        List<Item> items = database.getItemsFifo();
        if (items.isEmpty()) {
            IO.println("No items in this database yet.");
            return;
        }
        IO.println("--- Items (First-In, First-Out order) ---");
        for (Item item : items) {
            IO.println(item.toString());
        }
    }

    private void logDonation() {
        IO.println("Item category: 1) Medical Supply  2) Food Pack  3) Rescue Gear");
        String categoryChoice = scanner.nextLine().trim();

        IO.println("Item name:");
        String itemName = scanner.nextLine().trim();
        if (itemName.isEmpty()) {
            IO.println("Item name cannot be empty.");
            return;
        }

        IO.println("Quantity donated:");
        int quantity = readInt();
        if (quantity <= 0) {
            IO.println("Quantity must be greater than zero.");
            return;
        }