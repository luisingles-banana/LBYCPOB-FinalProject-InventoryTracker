package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.DatabaseManager;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.StockStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Landing page: lets the user create a new relief-goods database or open an
 * existing one. Each database is shown as a summary card with Green/Yellow/Red
 * stock counts and an expiring-soon flag, mirroring {@link Database#getDashboardSummary()}.
 */
public class DashboardPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JPanel listPanel = new JPanel();

    public DashboardPanel(MainFrame mainFrame) {
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
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_LIGHT);
        header.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel title = Theme.sectionTitle("Relief-Goods Databases");
        JLabel subtitle = new JLabel("Create a database per ledger (e.g. Main Campus, Laguna Campus) or open an existing one.");
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_MUTED);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(subtitle);

        JButton newDbButton = Theme.primaryButton("+ New Database");
        newDbButton.addActionListener(e -> createDatabase());

        header.add(titleBox, BorderLayout.WEST);
        header.add(newDbButton, BorderLayout.EAST);
        return header;
    }

    private void createDatabase() {
        String name = JOptionPane.showInputDialog(this,
                "Name for the new database (e.g. \"Main Campus\"):",
                "New Database", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.isBlank()) return;
        if (DatabaseManager.findDatabase(name.trim()).isPresent()) {
            JOptionPane.showMessageDialog(this, "A database named \"" + name.trim() + "\" already exists.",
                    "Duplicate Name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DatabaseManager.createDatabase(name.trim());
        refresh();
    }

    public void refresh() {
        listPanel.removeAll();
        List<Database> databases = DatabaseManager.listDatabases();

        if (databases.isEmpty()) {
            JLabel empty = new JLabel("No databases yet. Click \"+ New Database\" to create your first relief-goods ledger.");
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_MUTED);
            empty.setBorder(new EmptyBorder(30, 4, 0, 0));
            listPanel.add(empty);
        } else {
            for (Database db : databases) {
                listPanel.add(buildCard(db));
                listPanel.add(Box.createVerticalStrut(12));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

