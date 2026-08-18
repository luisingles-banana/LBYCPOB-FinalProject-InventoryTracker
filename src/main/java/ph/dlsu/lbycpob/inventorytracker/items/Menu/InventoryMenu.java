package ph.dlsu.lbycpob.inventorytracker.items.Menu;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.DatabaseManager;

import java.util.List;

public class InventoryMenu extends BaseMenu{
    @Override
    protected void printMenu() {
        IO.println("""
---------------------------------------------------
              INVENTORY TRACKER
---------------------------------------------------
""");
        displayAllDatabases();
        IO.println("""
    1. Enter Database
    2. Add Database
    3. Delete Database
    0. Back to Main Menu
                """);
        IO.println("Choose an option: ");
    }

    @Override
    protected boolean userChoice(int choice) {
        switch (choice) {
            case 1 -> enterDatabase();
            case 2 -> addDatabase();
            case 3 -> deleteDatabase();
            case 0 -> { return false; }
            default -> IO.println("Invalid option, try again.");
        }
        return true;
    }

    private void displayAllDatabases() {
        List<Database> databases = DatabaseManager.listDatabases();
        if (databases.isEmpty()) {
            IO.println("No databases yet. Use 'Add Database' to create one.\n");
            return;
        }
        IO.println("Available Databases:");
        for (Database db : databases) {
            int itemCount = db.getItems().size();
            IO.println("  - " + db.getName() + " (" + itemCount + " item type(s))");
        }
        IO.println("");
    }

    private void enterDatabase() {
        IO.println("Enter Database Name:");
        String name = scanner.nextLine().trim();

        DatabaseManager.findDatabase(name).ifPresentOrElse(
                db -> new ItemMenu(db).run(),
                () -> IO.println("Database '" + name + "' not found.")
        );
    }

    private void addDatabase() {
        IO.println("Enter Database Name:");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            IO.println("Database name cannot be empty");
            return;
        }

        if (DatabaseManager.findDatabase(name).isPresent()) {
            IO.println("A database named '" + name + "' already exists.");
            return;
        }

        DatabaseManager.createDatabase(name);
        IO.println("Database " + name + " created!");
    }

    private void deleteDatabase() {
        IO.println("Enter Database Name to delete:");
        String name = scanner.nextLine().trim();

        boolean deleted = DatabaseManager.deleteDatabase(name);
        if (deleted) {
            IO.println("Database '" + name + "' deleted.");
        } else {
            IO.println("Database '" + name + "' not found.");
        }
    }
}