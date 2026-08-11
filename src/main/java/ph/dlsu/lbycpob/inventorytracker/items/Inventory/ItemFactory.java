package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDate;

/**
 * Creates the correct {@link Item} subclass from a category label.
 * Keeps item-construction/classification logic in one place so donations
 * and CSV loading are automatically sorted into the right type.
 */
public final class ItemFactory {

    private ItemFactory() {
    }

    public static Item create(String category, String name, int quantity,
                              int thresholdCritical, int thresholdLow, LocalDate expirationDate) {
        String key = category == null ? "" : category.trim().toLowerCase();
        return switch (key) {
            case "medical supply", "medical", "medicine", "1" ->
                    new MedicalSupply(name, quantity, thresholdCritical, thresholdLow, expirationDate);
            case "food pack", "food", "2" ->
                    new FoodPack(name, quantity, thresholdCritical, thresholdLow, expirationDate);
            case "rescue gear", "rescue", "3" ->
                    new RescueGear(name, quantity, thresholdCritical, thresholdLow, expirationDate);
            default -> throw new IllegalArgumentException("Unknown item category: " + category);
        };
    }
}
