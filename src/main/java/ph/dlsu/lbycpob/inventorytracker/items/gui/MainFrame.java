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
