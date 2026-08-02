package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.io.FileWriter;
import java.io.IOException;

public class Database {
    private String name;
    private String filePath;

    public Database(String name) {
        this.name = name;
        this.filePath = "src/main/resources/inventorytracker/" + name + ".csv";
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void createCsvFile() {
        try {
            java.io.File dir = new java.io.File("data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(filePath);
            writer.write("Index\n");
            writer.close();
            IO.println("Database file created at " + filePath);
        } catch (IOException e) {
            IO.println("Error creating database file: " + e.getMessage());
        }
    }
}