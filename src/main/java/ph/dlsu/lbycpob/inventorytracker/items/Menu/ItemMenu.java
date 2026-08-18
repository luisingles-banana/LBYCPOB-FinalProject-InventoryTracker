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

        var existing = database.findItem(itemName);
        if (existing.isPresent()) {
            existing.get().addStock(quantity);
            database.saveItems();
        } else {
            IO.println("Low stock threshold (Yellow alert level):");
            int thresholdLow = readInt();
            IO.println("Critical stock threshold (Red alert level):");
            int thresholdCritical = readInt();

            IO.println("Expiration date (YYYY-MM-DD), leave blank if non-perishable:");
            String expInput = scanner.nextLine().trim();
            LocalDate expirationDate = null;
            if (!expInput.isEmpty()) {
                try {
                    expirationDate = LocalDate.parse(expInput);
                } catch (DateTimeParseException e) {
                    IO.println("Invalid date format, treating item as non-perishable.");
                }
            }

            try {
                Item newItem = ItemFactory.create(categoryChoice, itemName, quantity, thresholdCritical, thresholdLow, expirationDate);
                database.addItem(newItem);
            } catch (IllegalArgumentException e) {
                IO.println("Invalid category selection. Donation not logged.");
                return;
            }
        }

        IO.println("Donor name (blank for Anonymous):");
        String donorName = scanner.nextLine().trim();
        IO.println("Donor contact (email/phone, optional):");
        String donorContact = scanner.nextLine().trim();

        String category = database.findItem(itemName).map(Item::getCategory).orElse("Unknown");
        Donor donor = new Donor(donorName, donorContact);
        database.logDonation(new Donation(donor, itemName, category, quantity));

        IO.println("Donation logged: " + quantity + " x " + itemName + " from " + donor.getName());
    }

    private void dispatchItem() {
        IO.println("Item name to dispatch:");
        String itemName = scanner.nextLine().trim();
        IO.println("Quantity to dispatch:");
        int quantity = readInt();

        boolean ok = database.dispatchItem(itemName, quantity);
        if (ok) {
            IO.println("Dispatched " + quantity + " x " + itemName + ".");
        } else {
            IO.println("Could not dispatch. Check the item name and available stock.");
        }
    }

    private void viewExpiringSoon() {
        List<Item> expiring = database.getExpiringSoon(7);
        if (expiring.isEmpty()) {
            IO.println("No items are expiring within the next 7 days.");
            return;
        }
        IO.println("--- Items Expiring Within 7 Days (deploy first!) ---");
        for (Item item : expiring) {
            IO.println(item.toString());
        }
    }

    private void viewDonationLog() {
        List<Donation> donations = database.getDonations();
        if (donations.isEmpty()) {
            IO.println("No donations logged yet.");
            return;
        }
        IO.println("--- Donation Log ---");
        for (Donation donation : donations) {
            IO.println(donation.toString());
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
