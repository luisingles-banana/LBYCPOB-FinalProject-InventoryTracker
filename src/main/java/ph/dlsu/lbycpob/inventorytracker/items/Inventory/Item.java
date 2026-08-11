package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDate;

/**
 * Abstract representation of a single material tracked by the inventory
 * system (e.g. medical supplies, food packs, rescue gear).
 * <p>
 * Demonstrates:
 * - Encapsulation: all fields are private with controlled access.
 * - Abstraction: subclasses only need to supply {@link #getCategory()}.
 * - Polymorphism: {@link #toString()} and {@link #getCategory()} behave
 *   differently per concrete subclass.
 */
public abstract class Item {
    private final String name;
    private int quantity;
    private int thresholdLow;
    private int thresholdCritical;
    private final LocalDate expirationDate; // null means non-perishable
    private final LocalDate dateAdded;

    protected Item(String name, int quantity, int thresholdCritical, int thresholdLow, LocalDate expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.thresholdCritical = thresholdCritical;
        this.thresholdLow = thresholdLow;
        this.expirationDate = expirationDate;
        this.dateAdded = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addStock(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    /**
     * Attempts to dispatch (remove) stock from this item.
     *
     * @return true if there was enough stock and it was dispatched.
     */
    public boolean dispatch(int amount) {
        if (amount <= 0 || amount > quantity) {
            return false;
        }
        quantity -= amount;
        return true;
    }

    public int getThresholdLow() {
        return thresholdLow;
    }

    public int getThresholdCritical() {
        return thresholdCritical;
    }

    public void setThresholds(int thresholdCritical, int thresholdLow) {
        this.thresholdCritical = thresholdCritical;
        this.thresholdLow = thresholdLow;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public boolean isPerishable() {
        return expirationDate != null;
    }

    public boolean isExpired() {
        return isPerishable() && expirationDate.isBefore(LocalDate.now());
    }

    /** True when a perishable item expires within the given number of days (and isn't already expired). */
    public boolean isNearingExpiration(int daysThreshold) {
        if (!isPerishable() || isExpired()) {
            return false;
        }
        return !expirationDate.isAfter(LocalDate.now().plusDays(daysThreshold));
    }

    /** Dashboard alert level: Green = Safe, Yellow = Low, Red = Critical. */
    public StockStatus getStockStatus() {
        if (quantity <= thresholdCritical) {
            return StockStatus.RED;
        }
        if (quantity <= thresholdLow) {
            return StockStatus.YELLOW;
        }
        return StockStatus.GREEN;
    }

    /** Polymorphic hook implemented by each concrete material type. */
    public abstract String getCategory();

    /** Serializes this item as one CSV row for persistence. */
    public String toCsvRow() {
        String exp = isPerishable() ? expirationDate.toString() : "";
        return String.join(",",
                escape(name),
                escape(getCategory()),
                String.valueOf(quantity),
                String.valueOf(thresholdLow),
                String.valueOf(thresholdCritical),
                exp);
    }

    private String escape(String value) {
        return value.replace(",", " ");
    }

    @Override
    public String toString() {
        String exp = "";
        if (isPerishable()) {
            exp = ", Exp: " + expirationDate + (isExpired() ? " [EXPIRED]" : "");
        }
        return String.format("[%-14s] %-20s Qty: %-5d Status: %-16s%s",
                getCategory(), name, quantity, getStockStatus(), exp);
    }
}
