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
