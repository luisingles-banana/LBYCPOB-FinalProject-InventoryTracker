package ph.dlsu.lbycpob.inventorytracker.items;

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

    private void displayAllDatabases() { /* TODO */ }
    private void enterDatabase() { /* TODO */ }
    private void addDatabase() { /* TODO */ }
    private void deleteDatabase() { /* TODO */ }
}
