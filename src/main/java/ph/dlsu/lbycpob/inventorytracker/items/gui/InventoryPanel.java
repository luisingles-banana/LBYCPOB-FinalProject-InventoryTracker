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