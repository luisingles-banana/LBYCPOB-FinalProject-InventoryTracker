package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {
    private static final String DATA_DIR = "data";
    private static List<Database> databases = new ArrayList<>();

    public static List<Database> listDatabases() {
        return databases;
    }

    public static void createDatabase(String name) {
        Database db = new Database(name);
        db.createCsvFile();
        databases.add(db);
    }

    /** Looks up a previously loaded/created database by name (case-insensitive). */
    public static Optional<Database> findDatabase(String name) {
        return databases.stream()
                .filter(db -> db.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /** Removes a database's CSV files from disk and drops it from the in-memory list. */
    public static boolean deleteDatabase(String name) {
        Optional<Database> found = findDatabase(name);
        if (found.isEmpty()) return false;

        Database db = found.get();
        db.deleteFiles();
        databases.remove(db);
        return true;
    }

    /**
     * Discovers every inventory database CSV in the data directory and loads it
     * into memory. Called once on startup so previously saved data is available.
     */
    public static void loadAll() {
        databases.clear();

        File dir = new File(DATA_DIR);
        File[] files = dir.listFiles((d, filename) -> filename.endsWith(".csv"));
        if (files == null) return;

        for (File file : files) {
            String fileName = file.getName();
            // Skip donation logs and non-inventory data files (e.g. emergency contacts).
            if (fileName.endsWith("_donations.csv")) continue;
            if (fileName.equalsIgnoreCase("emergency_contact.csv")
                    || fileName.equalsIgnoreCase("emergency_contacts.csv")) continue;

            String name = fileName.substring(0, fileName.length() - ".csv".length());
            Database db = new Database(name);
            db.loadFromCsv();
            databases.add(db);
        }
    }
}
