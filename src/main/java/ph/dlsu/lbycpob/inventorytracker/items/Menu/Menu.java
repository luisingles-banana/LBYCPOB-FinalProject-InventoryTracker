package ph.dlsu.lbycpob.inventorytracker.items.Menu;

public class Menu extends BaseMenu{

    @Override
    protected void printMenu() {
        IO.println("""
---------------------------------------------------
      FINANCE & MATERIALS INVENTORY TRACKER
---------------------------------------------------
    1. Finance Tracker
    2. Inventory Tracker
    3. SOP & Emergency Protocols
    0. Exit
                """);
        IO.println("Choose an option: ");
    }

    @Override
    protected boolean userChoice(int choice) {
        switch (choice) {
            case 1 -> enterFinance();
            case 2 -> enterInventory();
            case 3 -> enterSOP();
            case 0 -> { return false; }
            default -> IO.println("Invalid option, try again.");
        }
        return true;
    }

    private void enterInventory() { new InventoryMenu().run(); }
    private void enterSOP() { new SOPMenu().run(); }
    private void enterFinance() { /* TODO */ }
}
