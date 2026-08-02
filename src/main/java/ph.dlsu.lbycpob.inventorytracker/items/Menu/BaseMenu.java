package ph.dlsu.lbycpob.inventorytracker.items.Menu;

import java.util.Scanner;

public abstract class BaseMenu {
    protected Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readChoice();
            running = userChoice(choice);
        }
    }

    protected int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected abstract void printMenu();

    // returns false when this menu should exit its loop
    protected abstract boolean userChoice(int choice);
}
