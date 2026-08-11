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
