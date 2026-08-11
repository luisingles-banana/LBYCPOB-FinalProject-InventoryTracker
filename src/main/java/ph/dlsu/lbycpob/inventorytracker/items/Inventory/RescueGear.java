package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDate;

/** Non-perishable rescue equipment such as ropes, flashlights, and life vests. */
public class RescueGear extends Item {

    public RescueGear(String name, int quantity, int thresholdCritical, int thresholdLow, LocalDate expirationDate) {
        super(name, quantity, thresholdCritical, thresholdLow, expirationDate);
    }

    @Override
    public String getCategory() {
        return "Rescue Gear";
    }
}
