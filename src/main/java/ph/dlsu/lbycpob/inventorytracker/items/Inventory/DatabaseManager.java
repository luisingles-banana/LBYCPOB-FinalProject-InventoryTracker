package ph.dlsu.lbycpob.inventorytracker.items.Inventory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static List<Database> databases = new ArrayList<>();

    public static List<Database> listDatabases() {
        return databases;
    }

    public static void createDatabase(String name) {
        Database db = new Database(name);
        db.createCsvFile();
        databases.add(db);
    }
}
