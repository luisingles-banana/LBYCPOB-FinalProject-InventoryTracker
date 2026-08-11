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
        int row = 0;
        addRow(form, c, row++, "Donor Name:", donorNameField);
        addRow(form, c, row++, "Donor Contact:", donorContactField);
        addRow(form, c, row++, "Item Name:", itemNameField);
        addRow(form, c, row++, "Category (if new item):", categoryBox);
        addRow(form, c, row++, "Quantity Donated:", quantityField);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(perishableCheck, c);
        row++;

        expirationField.setEnabled(false);
        expirationField.setToolTipText("Format: YYYY-MM-DD — only used if this creates a new item");
        addRow(form, c, row++, "Expiration Date (YYYY-MM-DD):", expirationField);

        perishableCheck.addActionListener(e -> expirationField.setEnabled(perishableCheck.isSelected()));

        JLabel hint = new JLabel("<html><i>If \"" + "Item Name" + "\" already exists, its stock is simply increased " +
                "and category/expiration below are ignored.</i></html>");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_MUTED);
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(hint, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.setBorder(new EmptyBorder(0, 20, 16, 20));
        JButton cancel = Theme.secondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = Theme.primaryButton("Log Donation");
        save.addActionListener(e -> onSave());
        buttons.add(cancel);
        buttons.add(save);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_BODY_BOLD);
        form.add(l, c);

        c.gridx = 1;
        c.weightx = 1;
        form.add(field, c);
    }

    private void onSave() {
        String itemName = itemNameField.getText().trim();
        if (itemName.isEmpty()) {
            showError("Enter the name of the item being donated.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Quantity must be a positive whole number.");
            return;
        }

        String category = (String) categoryBox.getSelectedItem();
        Optional<Item> existing = database.findItem(itemName);

        if (existing.isPresent()) {
            existing.get().addStock(quantity);
            database.saveItems();
            category = existing.get().getCategory();
        } else {
            LocalDate expirationDate = null;
            if (perishableCheck.isSelected()) {
                try {
                    expirationDate = LocalDate.parse(expirationField.getText().trim());
                } catch (DateTimeParseException ex) {
                    showError("Enter a valid expiration date in YYYY-MM-DD format.");
                    return;
                }
            }
            Item newItem = ItemFactory.create(category, itemName, quantity, 5, 15, expirationDate);
            database.addItem(newItem);
        }