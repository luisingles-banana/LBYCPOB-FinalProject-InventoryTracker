package ph.dlsu.lbycpob.inventorytracker.items;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.DatabaseManager;
import ph.dlsu.lbycpob.inventorytracker.items.Menu.Menu;
import ph.dlsu.lbycpob.inventorytracker.items.gui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.loadAll();

        // Pass "--console" as a program argument to fall back to the original
        // text-menu interface; by default the GUI launches.
        if (args.length > 0 && args[0].equalsIgnoreCase("--console")) {
            new Menu().run();
            return;
        }

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
