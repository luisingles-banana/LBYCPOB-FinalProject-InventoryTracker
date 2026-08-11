package ph.dlsu.lbycpob.inventorytracker.items.Menu;

import ph.dlsu.lbycpob.inventorytracker.items.SOP.SOP;
import ph.dlsu.lbycpob.inventorytracker.items.SOP.SOPRepository;

import java.util.List;

/** Categorized library of crisis-specific SOPs paired with supply checklists. */
public class SOPMenu extends BaseMenu {
    private final List<SOP> sops = SOPRepository.getAllSOPs();

    @Override
    protected void printMenu() {
        IO.println("""
---------------------------------------------------
      SOP & EMERGENCY PROTOCOL LIBRARY
---------------------------------------------------""");
        for (int i = 0; i < sops.size(); i++) {
            IO.println("    " + (i + 1) + ". " + sops.get(i).getScenario());
        }
        IO.println("    0. Back to Main Menu");
        IO.println("Choose a scenario: ");
    }

    @Override
    protected boolean userChoice(int choice) {
        if (choice == 0) {
            return false;
        }
        if (choice >= 1 && choice <= sops.size()) {
            IO.println(sops.get(choice - 1).toDetailedString());
        } else {
            IO.println("Invalid option, try again.");
        }
        return true;
    }
}
