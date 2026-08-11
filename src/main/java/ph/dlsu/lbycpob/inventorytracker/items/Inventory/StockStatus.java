package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

/**
 * Represents the dashboard alert level for an inventory item,
 * based on how current quantity compares to configured thresholds.
 */
public enum StockStatus {
    GREEN("Safe"),
    YELLOW("Low"),
    RED("Critical");

    private final String label;

    StockStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return name() + " (" + label + ")";
    }
}