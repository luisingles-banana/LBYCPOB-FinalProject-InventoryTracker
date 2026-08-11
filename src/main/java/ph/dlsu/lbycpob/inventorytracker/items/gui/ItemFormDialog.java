package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.ItemFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class ItemFormDialog extends JDialog {
    private static final String[] CATEGORIES = {"Medical Supply", "Food Pack", "Rescue Gear"};

    private final JTextField nameField = new JTextField(18);
    private final JComboBox<String> categoryBox = new JComboBox<>(CATEGORIES);
    private final JTextField quantityField = new JTextField("0", 8);
    private final JTextField lowField = new JTextField("15", 8);
    private final JTextField criticalField = new JTextField("5", 8);
    private final JCheckBox perishableCheck = new JCheckBox("This item is perishable");
    private final JTextField expirationField = new JTextField(10);

    private Item createdItem;

    public ItemFormDialog(Frame owner) {
        super(owner, "Add Inventory Item", true);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(form, c, row++, "Item Name:", nameField);
        addRow(form, c, row++, "Category:", categoryBox);
        addRow(form, c, row++, "Quantity:", quantityField);
        addRow(form, c, row++, "Low-Stock Threshold:", lowField);
        addRow(form, c, row++, "Critical-Stock Threshold:", criticalField);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(perishableCheck, c);
        row++;

        expirationField.setEnabled(false);
        expirationField.setToolTipText("Format: YYYY-MM-DD");
        addRow(form, c, row++, "Expiration Date (YYYY-MM-DD):", expirationField);

        perishableCheck.addActionListener(e -> expirationField.setEnabled(perishableCheck.isSelected()));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.setBorder(new EmptyBorder(0, 20, 16, 20));
        JButton cancel = Theme.secondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = Theme.primaryButton("Add Item");
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
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showError("Enter an item name.");
            return;
        }

        int quantity, low, critical;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
            low = Integer.parseInt(lowField.getText().trim());
            critical = Integer.parseInt(criticalField.getText().trim());
            if (quantity < 0 || low < 0 || critical < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Quantity and thresholds must be non-negative whole numbers.");
            return;
        }

        LocalDate expirationDate = null;
        if (perishableCheck.isSelected()) {
            try {
                expirationDate = LocalDate.parse(expirationField.getText().trim());
            } catch (DateTimeParseException ex) {
                showError("Enter a valid expiration date in YYYY-MM-DD format.");
                return;
            }
        }

        String category = (String) categoryBox.getSelectedItem();
        createdItem = ItemFactory.create(category, name, quantity, critical, low, expirationDate);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    public Item getCreatedItem() {
        return createdItem;
    }
}
