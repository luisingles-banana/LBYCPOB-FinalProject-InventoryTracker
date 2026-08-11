package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Donation;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.StockStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Live dashboard for one relief-goods {@link Database}: item table with
 * color-coded stock status, quick actions (add / restock / dispatch / log
 * donation), a FIFO / expiring-soon view toggle, and a donation history tab.
 */
public class InventoryPanel extends JPanel {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final MainFrame mainFrame;
    private Database database;

    private final JLabel titleLabel = new JLabel();
    private final JLabel summaryLabel = new JLabel();

    private final DefaultTableModel itemTableModel = new DefaultTableModel(
            new Object[]{"Name", "Category", "Quantity", "Status", "Expiration"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable itemTable = new JTable(itemTableModel);

    private final DefaultTableModel donationTableModel = new DefaultTableModel(
            new Object[]{"Date", "Donor", "Contact", "Item", "Category", "Quantity"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable donationTable = new JTable(donationTableModel);

    private boolean showingFifoView = false;


    public InventoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_LIGHT);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        summaryLabel.setFont(Theme.FONT_BODY);
        summaryLabel.setForeground(Theme.TEXT_MUTED);
        summaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setBorder(new EmptyBorder(12, 0, 0, 0));

        JButton addItem = Theme.primaryButton("+ Add Item");
        addItem.addActionListener(e -> onAddItem());

        JButton restock = Theme.secondaryButton("↑ Restock Selected");
        restock.addActionListener(e -> onRestock());

        JButton dispatch = Theme.secondaryButton("↓ Dispatch Selected");
        dispatch.addActionListener(e -> onDispatch());

        JButton logDonation = Theme.secondaryButton("♥ Log Donation");
        logDonation.addActionListener(e -> onLogDonation());

        JToggleButton fifoToggle = new JToggleButton("Show FIFO / Expiring-Soon Order");
        fifoToggle.setFont(Theme.FONT_BODY);
        fifoToggle.setFocusPainted(false);
        fifoToggle.addActionListener(e -> {
            showingFifoView = fifoToggle.isSelected();
            refreshItemTable();
        });
