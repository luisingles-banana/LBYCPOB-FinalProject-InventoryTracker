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

        toolbar.add(addItem);
        toolbar.add(restock);
        toolbar.add(dispatch);
        toolbar.add(logDonation);
        toolbar.add(fifoToggle);

        header.add(titleLabel);
        header.add(summaryLabel);
        header.add(toolbar);
        return header;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BODY_BOLD);

        itemTable.setRowHeight(30);
        itemTable.setFont(Theme.FONT_BODY);
        itemTable.getTableHeader().setFont(Theme.FONT_BODY_BOLD);
        itemTable.setSelectionBackground(new Color(0xDD, 0xF0, 0xE6));
        itemTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        tabs.addTab("Items", new JScrollPane(itemTable));

        donationTable.setRowHeight(28);
        donationTable.setFont(Theme.FONT_BODY);
        donationTable.getTableHeader().setFont(Theme.FONT_BODY_BOLD);
        tabs.addTab("Donation History", new JScrollPane(donationTable));

        return tabs;
    }

    public void setDatabase(Database database) {
        this.database = database;
        showingFifoView = false;
        refreshAll();
    }

    private void refreshAll() {
        if (database == null) return;
        titleLabel.setText(database.getName());
        refreshItemTable();
        refreshDonationTable();
    }

    private void refreshItemTable() {
        itemTableModel.setRowCount(0);
        if (database == null) return;

        int green = 0, yellow = 0, red = 0;
        for (Item item : database.getItems()) {
            StockStatus s = item.getStockStatus();
            if (s == StockStatus.GREEN) green++;
            else if (s == StockStatus.YELLOW) yellow++;
            else red++;
        }
        int expiringSoon = database.getExpiringSoon(7).size();
        summaryLabel.setText(String.format(
                "%d item(s)  •  Safe: %d  •  Low: %d  •  Critical: %d%s",
                database.getItems().size(), green, yellow, red,
                expiringSoon > 0 ? "  •  " + expiringSoon + " expiring within 7 days (deploy FIFO first!)" : ""));

        List<Item> rows = showingFifoView ? database.getItemsFifo() : database.getItems();
        for (Item item : rows) {
            String exp = item.isPerishable()
                    ? item.getExpirationDate() + (item.isExpired() ? " [EXPIRED]" : "")
                    : "—";
            itemTableModel.addRow(new Object[]{
                    item.getName(), item.getCategory(), item.getQuantity(),
                    item.getStockStatus().name(), exp
            });
        }
    }

    private void refreshDonationTable() {
        donationTableModel.setRowCount(0);
        if (database == null) return;
        List<Donation> donations = database.getDonations();
        for (int i = donations.size() - 1; i >= 0; i--) {
            Donation d = donations.get(i);
            donationTableModel.addRow(new Object[]{
                    d.getDateLogged().format(DATE_FMT), d.getDonor().getName(), d.getDonor().getContact(),
                    d.getItemName(), d.getCategory(), d.getQuantity()
            });
        }
    }

    private Item getSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an item from the table first.",
                    "No Item Selected", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        String name = (String) itemTableModel.getValueAt(row, 0);
        return database.findItem(name).orElse(null);
    }

    private void onAddItem() {
        if (database == null) return;
        ItemFormDialog dialog = new ItemFormDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        Item created = dialog.getCreatedItem();
        if (created != null) {
            if (database.findItem(created.getName()).isPresent()) {
                JOptionPane.showMessageDialog(this, "An item named \"" + created.getName() + "\" already exists.",
                        "Duplicate Item", JOptionPane.WARNING_MESSAGE);
                return;
            }
            database.addItem(created);
            refreshItemTable();
        }
    }

    private void onRestock() {
        if (database == null) return;
        Item item = getSelectedItem();
        if (item == null) return;
        String input = JOptionPane.showInputDialog(this, "Restock amount for \"" + item.getName() + "\":", "1");
        if (input == null) return;
        try {
            int amount = Integer.parseInt(input.trim());
            if (amount <= 0) throw new NumberFormatException();
            item.addStock(amount);
            database.saveItems();
            refreshItemTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a positive whole number.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDispatch() {
        if (database == null) return;
        Item item = getSelectedItem();
        if (item == null) return;
        String input = JOptionPane.showInputDialog(this,
                "Dispatch amount for \"" + item.getName() + "\" (available: " + item.getQuantity() + "):", "1");
        if (input == null) return;
        try {
            int amount = Integer.parseInt(input.trim());
            boolean ok = database.dispatchItem(item.getName(), amount);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Not enough stock to dispatch that amount.",
                        "Dispatch Failed", JOptionPane.ERROR_MESSAGE);
            }
            refreshItemTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a positive whole number.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
        }
    }