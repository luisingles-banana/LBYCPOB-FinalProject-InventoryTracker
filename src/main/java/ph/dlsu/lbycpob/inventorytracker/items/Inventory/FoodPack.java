package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDate;

/** Ready-to-eat meals, canned goods, and other food relief packs. Typically perishable. */
public class FoodPack extends Item {

    public FoodPack(String name, int quantity, int thresholdCritical, int thresholdLow, LocalDate expirationDate) {
        super(name, quantity, thresholdCritical, thresholdLow, expirationDate);
    }

    @Override
    public String getCategory() {
        return "Food Pack";
    }
}
