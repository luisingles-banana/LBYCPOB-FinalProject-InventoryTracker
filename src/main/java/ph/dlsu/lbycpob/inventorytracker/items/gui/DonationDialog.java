package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Donation;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Donor;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.ItemFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Modal dialog for logging a donation. If the named item already exists in
 * the database its stock is simply increased; otherwise a new item is
 * created and classified automatically via {@link ItemFactory}, matching the
 * "Donation Logging System" feature described in the project README.
 */
public class DonationDialog extends JDialog {
    private static final String[] CATEGORIES = {"Medical Supply", "Food Pack", "Rescue Gear"};

    private final Database database;

    private final JTextField donorNameField = new JTextField(18);
    private final JTextField donorContactField = new JTextField(18);
    private final JTextField itemNameField = new JTextField(18);
    private final JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
    private final JTextField quantityField = new JTextField("1", 8);
    private final JCheckBox perishableCheck = new JCheckBox("New item is perishable");
    private final JTextField expirationField = new JTextField(10);

    private boolean submitted = false;

    public DonationDialog(Frame owner, Database database) {
        super(owner, "Log Donation", true);
        this.database = database;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
