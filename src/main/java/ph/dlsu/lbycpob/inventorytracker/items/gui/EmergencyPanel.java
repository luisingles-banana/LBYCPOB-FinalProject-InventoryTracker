package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Emergency.EmergencyContact;
import ph.dlsu.lbycpob.inventorytracker.items.Emergency.EmergencyContactRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Full-page emergency contacts directory: call/copy any number, and
 * add/edit/delete entries (e.g. a specific department's local extension).
 */
public class EmergencyPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JPanel listPanel = new JPanel();

    public EmergencyPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_LIGHT);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.BG_LIGHT);
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setBackground(Theme.BG_LIGHT);
        scroll.getViewport().setBackground(Theme.BG_LIGHT);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_LIGHT);
        header.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel title = Theme.sectionTitle("Emergency Contacts & Hotlines");
        JLabel subtitle = new JLabel("Tap Call to dial, or Copy to grab the number. Add campus-specific extensions as needed.");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_MUTED);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(subtitle);

        JButton addButton = Theme.primaryButton("+ Add Contact");
        addButton.addActionListener(e -> addContact());

        header.add(titleBox, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);
        return header;
    }

    private void addContact() {
        ContactFormDialog dialog = new ContactFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.wasSubmitted()) {
            EmergencyContactRepository.add(dialog.buildContact());
            refresh();
            mainFrame.refreshEmergencyContacts();
        }
    }

    public void refresh() {
        listPanel.removeAll();
        List<EmergencyContact> contacts = EmergencyContactRepository.getAll();
        for (EmergencyContact contact : contacts) {
            listPanel.add(buildRow(contact));
            listPanel.add(Box.createVerticalStrut(10));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildRow(EmergencyContact contact) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(16, 0));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        if (contact.isPriority()) {
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.RED_EMERGENCY, 2, true),
                    BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        }

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(contact.getName());
        name.setFont(Theme.FONT_BODY_BOLD);
        JLabel category = new JLabel(contact.getCategory());
        category.setFont(Theme.FONT_SMALL);
        category.setForeground(Theme.TEXT_MUTED);
        left.add(name);
        left.add(category);
