package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.ItemFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Modal dialog for creating a new {@link Item}. Delegates the actual
 * subclass construction to {@link ItemFactory} so the GUI never needs an
 * {@code instanceof} check.
 */
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
