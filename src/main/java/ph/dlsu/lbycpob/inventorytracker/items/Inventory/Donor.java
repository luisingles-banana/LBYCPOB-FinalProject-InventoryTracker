package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

/** A contributor to the relief effort: a student, faculty member, or external partner. */
public class Donor {
    private final String name;
    private final String contact;

    public Donor(String name, String contact) {
        this.name = (name == null || name.isBlank()) ? "Anonymous" : name;
        this.contact = (contact == null || contact.isBlank()) ? "N/A" : contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return name + " (" + contact + ")";
    }
}
