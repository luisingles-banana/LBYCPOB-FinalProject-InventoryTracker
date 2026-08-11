package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Emergency.EmergencyContact;
import ph.dlsu.lbycpob.inventorytracker.items.Emergency.EmergencyContactRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Lightweight modal reachable from the always-visible top-bar "Emergency
 * Hotline" button. Surfaces the priority contacts in one click from anywhere
 * in the app, without navigating away from whatever the user was doing.
 */
public class HotlineQuickDialog extends JDialog {
    public HotlineQuickDialog(Frame owner) {
        super(owner, "Emergency Hotline", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CARD_WHITE);

        JPanel banner = new JPanel();
        banner.setBackground(Theme.RED_EMERGENCY);
        banner.setBorder(new EmptyBorder(16, 20, 16, 20));
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        JLabel bannerTitle = new JLabel("\uD83D\uDEA8 In an emergency, call now");
        bannerTitle.setFont(Theme.FONT_HEADER);
        bannerTitle.setForeground(Color.WHITE);
        JLabel bannerSub = new JLabel("Tap Call to dial. If this computer can't place calls, the number is copied for you.");
        bannerSub.setFont(Theme.FONT_SMALL);
        bannerSub.setForeground(new Color(0xFF, 0xE3, 0xE3));
        banner.add(bannerTitle);
        banner.add(bannerSub);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(16, 20, 16, 20));
        list.setBackground(Theme.CARD_WHITE);

        List<EmergencyContact> all = EmergencyContactRepository.getAll();
        boolean any = false;
        for (EmergencyContact contact : all) {
            if (!contact.isPriority()) continue;
            any = true;
            list.add(buildRow(contact));
            list.add(Box.createVerticalStrut(10));
        }
        if (!any) {
            for (EmergencyContact contact : all) {
                list.add(buildRow(contact));
                list.add(Box.createVerticalStrut(10));
            }
        }

        JButton manageButton = Theme.secondaryButton("Manage all contacts →");
        manageButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        manageButton.addActionListener(e -> {
            dispose();
            if (owner instanceof MainFrame mf) {
                mf.showEmergencyContacts();
            }
        });
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Theme.CARD_WHITE);
        footer.setBorder(new EmptyBorder(0, 20, 16, 20));
        JButton close = Theme.secondaryButton("Close");
        close.addActionListener(e -> dispose());
        footer.add(manageButton);
        footer.add(close);

        add(banner, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        setSize(420, 480);
        setLocationRelativeTo(owner);
    }

    private JPanel buildRow(EmergencyContact contact) {
        JPanel row = Theme.card();
        row.setLayout(new BorderLayout(12, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("<html><body style='width:180px'>" + contact.getName() + "</body></html>");
        name.setFont(Theme.FONT_BODY_BOLD);
        JLabel number = new JLabel(contact.getNumber());
        number.setFont(Theme.FONT_MONO_NUMBER);
        number.setForeground(Theme.RED_EMERGENCY);
        left.add(name);
        left.add(number);

        JButton call = Theme.dangerButton("\u260E Call");
        call.addActionListener(e -> CallHelper.call(this, contact.getName(), contact.getNumber()));
        row.add(left, BorderLayout.CENTER);
        row.add(call, BorderLayout.EAST);
        return row;
    }
}
