package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Top-level application window: a navy top bar (with an always-visible
 * Emergency Hotline quick-call button), a sidebar for navigation, and a
 * CardLayout content area that swaps between the Dashboard, Inventory,
 * SOP Library, and Emergency Contacts pages.
 */
public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final DashboardPanel dashboardPanel;
    private final InventoryPanel inventoryPanel;
    private final SOPPanel sopPanel;
    private final EmergencyPanel emergencyPanel;

    private final List<JButton> navButtons = new ArrayList<>();
    private JButton inventoryNavButton;

    private Database currentDatabase;

    public MainFrame() {
        super("Proactive Emergency Donations & Materials Inventory System — DLSU");
        Theme.installGlobalDefaults();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        dashboardPanel = new DashboardPanel(this);
        inventoryPanel = new InventoryPanel(this);
        sopPanel = new SOPPanel();
        emergencyPanel = new EmergencyPanel(this);

        cardPanel.setBackground(Theme.BG_LIGHT);
        cardPanel.add(dashboardPanel, "dashboard");
        cardPanel.add(inventoryPanel, "inventory");
        cardPanel.add(sopPanel, "sop");
        cardPanel.add(emergencyPanel, "emergency");

        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        showDashboard();
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.NAVY);
        top.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Relief Inventory & SOP Tracker");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("De La Salle University — Disaster Preparedness & Relief Operations");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(new Color(0xC9, 0xD6, 0xD6));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(subtitle);

        JButton hotlineButton = Theme.dangerButton("\uD83D\uDEA8  Emergency Hotline");
        hotlineButton.setFont(Theme.FONT_BODY_BOLD);
        hotlineButton.addActionListener(e -> new HotlineQuickDialog(this).setVisible(true));

        top.add(titleBox, BorderLayout.WEST);
        top.add(hotlineButton, BorderLayout.EAST);
        return top;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.NAVY_LIGHT);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(18, 0, 18, 0));

        sidebar.add(navButton("\uD83D\uDCCA  Dashboard", () -> showDashboard()));
        inventoryNavButton = navButton("\uD83D\uDCE6  Inventory", () -> {
            if (currentDatabase != null) {
                showInventory(currentDatabase);
            } else {
                showDashboard();
            }
        });
        sidebar.add(inventoryNavButton);
        sidebar.add(navButton("\uD83D\uDCCB  SOP Library", () -> showCard("sop")));
        sidebar.add(navButton("\u260E  Emergency Contacts", () -> showCard("emergency")));

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("<html><center>LBYCPOB<br>Final Project</center></html>");
        footer.setFont(Theme.FONT_SMALL);
        footer.setForeground(new Color(0x8F, 0xA3, 0xA3));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(new EmptyBorder(0, 0, 8, 0));
        sidebar.add(footer);

        return sidebar;
    }
    private JButton navButton(String text, Runnable onClick) {
        JButton b = new JButton(text);
        b.setFont(Theme.FONT_BODY_BOLD);
        b.setForeground(Color.WHITE);
        b.setBackground(Theme.NAVY_LIGHT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(14, 22, 14, 16));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> onClick.run());
        navButtons.add(b);
        return b;
    }

    public void showDashboard() {
        dashboardPanel.refresh();
        showCard("dashboard");
    }

    public void showInventory(Database db) {
        this.currentDatabase = db;
        inventoryPanel.setDatabase(db);
        inventoryNavButton.setText("\uD83D\uDCE6  Inventory (" + db.getName() + ")");
        showCard("inventory");
    }

    public void refreshEmergencyContacts() {
        emergencyPanel.refresh();
    }

    public void showEmergencyContacts() {
        showCard("emergency");
    }

    private void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }
}
