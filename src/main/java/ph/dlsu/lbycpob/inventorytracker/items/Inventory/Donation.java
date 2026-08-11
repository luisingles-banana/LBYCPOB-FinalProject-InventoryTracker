package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A single logged contribution: who gave what, how much, and when. */
public class Donation {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Donor donor;
    private final String itemName;
    private final String category;
    private final int quantity;
    private final LocalDateTime dateLogged;

    public Donation(Donor donor, String itemName, String category, int quantity) {
        this(donor, itemName, category, quantity, LocalDateTime.now());
    }

    public Donation(Donor donor, String itemName, String category, int quantity, LocalDateTime dateLogged) {
        this.donor = donor;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.dateLogged = dateLogged;
    }

    public Donor getDonor() {
        return donor;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getDateLogged() {
        return dateLogged;
    }

    public String toCsvRow() {
        return String.join(",",
                donor.getName().replace(",", " "),
                donor.getContact().replace(",", " "),
                itemName.replace(",", " "),
                category.replace(",", " "),
                String.valueOf(quantity),
                dateLogged.toString());
    }

    @Override
    public String toString() {
        return String.format("%-16s | %-15s x%-4d | from %-20s | %s",
                dateLogged.format(DISPLAY_FORMAT), itemName, quantity, donor.getName(), category);
    }
}
