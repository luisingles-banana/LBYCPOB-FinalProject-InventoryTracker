package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDate;

/** Medical supplies such as first-aid kits, medicine, or PPE. */
public class MedicalSupply extends Item {

    public MedicalSupply(String name, int quantity, int thresholdCritical, int thresholdLow, LocalDate expirationDate) {
        super(name, quantity, thresholdCritical, thresholdLow, expirationDate);
    }

    @Override
    public String getCategory() {
        return "Medical Supply";
    }
}
