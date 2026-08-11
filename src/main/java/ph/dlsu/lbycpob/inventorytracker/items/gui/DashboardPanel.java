package ph.dlsu.lbycpob.inventorytracker.items.gui;

import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Database;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.DatabaseManager;
import ph.dlsu.lbycpob.inventorytracker.items.Inventory.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;


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

    private JPanel buildCard(Database db) {
        int green = 0, yellow = 0, red = 0;
        for (Item item : db.getItems()) {
            StockStatus s = item.getStockStatus();
            if (s == StockStatus.GREEN) green++;
            else if (s == StockStatus.YELLOW) yellow++;
            else red++;
        }
        int expiringSoon = db.getExpiringSoon(7).size();

        JPanel card = Theme.card();
        card.setLayout(new BorderLayout(16, 0));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(db.getName());
        name.setFont(Theme.FONT_HEADER);
        JLabel meta = new JLabel(db.getItems().size() + " item(s) tracked" +
                (expiringSoon > 0 ? "  •  " + expiringSoon + " expiring within 7 days" : ""));
        meta.setFont(Theme.FONT_SMALL);
        meta.setForeground(expiringSoon > 0 ? Theme.AMBER_WARNING : Theme.TEXT_MUTED);
        left.add(name);
        left.add(Box.createVerticalStrut(6));
        left.add(meta);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        badges.setOpaque(false);
        badges.add(badge("Safe", green, Theme.STOCK_GREEN));
        badges.add(badge("Low", yellow, Theme.STOCK_YELLOW));
        badges.add(badge("Critical", red, Theme.STOCK_RED));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton open = Theme.primaryButton("Open →");
        open.addActionListener(e -> mainFrame.showInventory(db));
        JButton delete = Theme.secondaryButton("Delete");
        delete.addActionListener(e -> deleteDatabase(db));
        actions.add(open);
        actions.add(delete);

        card.add(left, BorderLayout.WEST);
        card.add(badges, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);
        return card;
    }

    private JPanel badge(String label, int count, Color color) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(Theme.FONT_MONO_NUMBER);
        countLabel.setForeground(color);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(Theme.FONT_SMALL);
        textLabel.setForeground(Theme.TEXT_MUTED);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(countLabel);
        p.add(textLabel);
        return p;
    }

    private void deleteDatabase(Database db) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete database \"" + db.getName() + "\" and its saved CSV files? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            DatabaseManager.deleteDatabase(db.getName());
            refresh();
        }
    }
}
