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
