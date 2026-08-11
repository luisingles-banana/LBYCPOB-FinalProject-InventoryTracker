package ph.dlsu.lbycpob.inventorytracker.items.Emergency;

/**
 * A single emergency contact/hotline entry (e.g. campus security, national
 * emergency hotline, nearest hospital). Kept simple and CSV-friendly so it
 * follows the same encapsulation pattern as the rest of the model layer.
 */
public class EmergencyContact {
    private String name;
    private String number;
    private String category; // e.g. Campus Security, Medical, Fire, National
    private boolean priority; // shown first / highlighted in the quick-call dialog

    public EmergencyContact(String name, String number, String category, boolean priority) {
        this.name = name;
        this.number = number;
        this.category = category;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String toCsvRow() {
        return String.join(",",
                escape(name),
                escape(number),
                escape(category),
                String.valueOf(priority));
    }

    private String escape(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    @Override
    public String toString() {
        return name + " - " + number;
    }
}
