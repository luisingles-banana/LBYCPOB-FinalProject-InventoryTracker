package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Emergency.EmergencyContact;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Add/Edit modal for an {@link EmergencyContact}. When editing, the passed-in
 * contact is mutated directly in place; when adding, a new contact is built
 * and made available via {@link #buildContact()}.
 */
public class ContactFormDialog extends JDialog {
    private final JTextField nameField = new JTextField(20);
    private final JTextField numberField = new JTextField(20);
    private final JTextField categoryField = new JTextField(20);
    private final JCheckBox priorityCheck = new JCheckBox("Show as priority contact (highlighted, listed first)");

    private final EmergencyContact editing;
    private boolean submitted = false;

    public ContactFormDialog(Frame owner, EmergencyContact editing) {
        super(owner, editing == null ? "Add Emergency Contact" : "Edit Emergency Contact", true);
        this.editing = editing;
        setLayout(new BorderLayout());

        if (editing != null) {
            nameField.setText(editing.getName());
            numberField.setText(editing.getNumber());
            categoryField.setText(editing.getCategory());
            priorityCheck.setSelected(editing.isPriority());
        } else {
            categoryField.setText("Campus Security");
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(form, c, row++, "Name:", nameField);
        addRow(form, c, row++, "Number:", numberField);
        addRow(form, c, row++, "Category:", categoryField);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        form.add(priorityCheck, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.setBorder(new EmptyBorder(0, 20, 16, 20));
        JButton cancel = Theme.secondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = Theme.primaryButton(editing == null ? "Add Contact" : "Save Changes");
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
        String number = numberField.getText().trim();
        String category = categoryField.getText().trim();

        if (name.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and number are required.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (editing != null) {
            editing.setName(name);
            editing.setNumber(number);
            editing.setCategory(category.isEmpty() ? "General" : category);
            editing.setPriority(priorityCheck.isSelected());
        }

        submitted = true;
        dispose();
    }

    public boolean wasSubmitted() {
        return submitted;
    }

    /** Only meaningful in "add" mode (editing == null). */
    public EmergencyContact buildContact() {
        return new EmergencyContact(nameField.getText().trim(), numberField.getText().trim(),
                categoryField.getText().trim().isEmpty() ? "General" : categoryField.getText().trim(),
                priorityCheck.isSelected());
    }
}
