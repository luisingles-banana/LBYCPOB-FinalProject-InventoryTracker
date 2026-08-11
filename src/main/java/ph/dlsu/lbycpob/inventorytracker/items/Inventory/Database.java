package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Database {
    private static final String DATA_DIR = "data";

    private String name;
    private String filePath;
    private String donationsFilePath;

    private final List<Item> items = new ArrayList<>();
    private final List<Donation> donations = new ArrayList<>();

    public Database(String name) {
        this.name = name;
        this.filePath = DATA_DIR + "/" + name + ".csv";
        this.donationsFilePath = DATA_DIR + "/" + name + "_donations.csv";
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void createCsvFile() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(filePath);
            writer.write("Name,Category,Quantity,ThresholdLow,ThresholdCritical,ExpirationDate\n");
            writer.close();
            IO.println("Database file created at " + filePath);
        } catch (IOException e) {
            IO.println("Error creating database file: " + e.getMessage());
        }
    }

    public void addItem(Item item) {
        items.add(item);
        saveItems();
    }

    public Optional<Item> findItem(String itemName) {
        return items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(itemName))
                .findFirst();
    }
    public boolean dispatchItem(String itemName, int amount) {
        Optional<Item> found = findItem(itemName);
        if (found.isEmpty()) return false;
        boolean ok = found.get().dispatch(amount);
        if (ok) saveItems();
        return ok;
    }

    public void logDonation(Donation donation) {
        donations.add(donation);
        saveDonations();
    }

    /** First-In, First-Out view: perishable items closest to expiring come first. */
    public List<Item> getItemsFifo() {
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(
                (Item i) -> i.isPerishable() ? i.getExpirationDate() : LocalDate.MAX));
        return sorted;
    }

    public List<Item> getExpiringSoon(int daysThreshold) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            if (item.isNearingExpiration(daysThreshold) || item.isExpired()) {
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(Item::getExpirationDate));
        return result;
    }

    /** Human readable dashboard summary: counts per alert status + expiring items. */
    public String getDashboardSummary() {
        int green = 0, yellow = 0, red = 0;
        for (Item item : items) {
            switch (item.getStockStatus()) {
                case GREEN -> green++;
                case YELLOW -> yellow++;
                case RED -> red++;
            }
        }
        List<Item> expiringSoon = getExpiringSoon(7);
        StringBuilder sb = new StringBuilder();
        sb.append("Database: ").append(name).append(" | Items: ").append(items.size()).append("\n");
        sb.append("  Green (Safe): ").append(green)
                .append("  |  Yellow (Low): ").append(yellow)
                .append("  |  Red (Critical): ").append(red).append("\n");
        if (!expiringSoon.isEmpty()) {
            sb.append("  Expiring within 7 days: ").append(expiringSoon.size())
                    .append(" item(s) - deploy these first (FIFO)!\n");
        }
        return sb.toString();
    }

    public void saveItems() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(filePath);
            writer.write("Name,Category,Quantity,ThresholdLow,ThresholdCritical,ExpirationDate\n");
            for (Item item : items) {
                writer.write(item.toCsvRow() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            IO.println("Error saving database file: " + e.getMessage());
        }
    }