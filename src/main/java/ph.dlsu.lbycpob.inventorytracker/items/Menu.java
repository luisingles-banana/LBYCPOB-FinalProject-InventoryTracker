package ph.dlsu.lbycpob.inventorytracker.items;
import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;
        while (running) {
            mainMenu();
            int choice = readChoice();
            switch (choice) {
                case 1 -> IO.println("Option 1");
                case 2 -> IO.println("Option 2");
                case 0 -> running = false;
                default -> IO.println("Invalid option, try again.");
            }
        }
        IO.println("Goodbye!");
    }

    public void mainMenu() {
        IO.println("""
---------------------------------------------------
      FINANCE & MATERIALS INVENTORY TRACKER
---------------------------------------------------
    1. Finance Tracker
    2. Inventory Tracker
    3. Exit
                """);
        IO.println("Choose an option: ");
    }

    // Helper Function
    private int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
