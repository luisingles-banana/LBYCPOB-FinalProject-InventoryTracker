package ph.dlsu.lbycpob.inventorytracker.items.Emergency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class EmergencyContactRepository {
    private static final String DATA_DIR = "data";
    private static final String FILE_PATH = DATA_DIR + "/emergency_contacts.csv";

    private static final List<EmergencyContact> CONTACTS = new ArrayList<>();
    private static boolean loaded = false;

    private EmergencyContactRepository() {
    }

    public static List<EmergencyContact> getAll() {
        ensureLoaded();
        return CONTACTS;
    }

    public static void add(EmergencyContact contact) {
        ensureLoaded();
        CONTACTS.add(contact);
        save();
    }

    public static void remove(EmergencyContact contact) {
        ensureLoaded();
        CONTACTS.remove(contact);
        save();
    }

    public static void save() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) dir.mkdirs();

            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write("Name,Number,Category,Priority\n");
                for (EmergencyContact c : CONTACTS) {
                    writer.write(c.toCsvRow() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving emergency contacts: " + e.getMessage());
        }
    }

    private static void ensureLoaded() {
        if (loaded) return;
        loaded = true;

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            seedDefaults();
            save();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                CONTACTS.add(new EmergencyContact(parts[0], parts[1], parts[2],
                        Boolean.parseBoolean(parts[3].trim())));
            }
        } catch (IOException e) {
            System.err.println("Warning: could not load emergency contacts (" + e.getMessage() + ")");
        }

        if (CONTACTS.isEmpty()) {
            seedDefaults();
            save();
        }
    }

    private static void seedDefaults() {
        CONTACTS.add(new EmergencyContact("National Emergency Hotline", "911", "National", true));
        CONTACTS.add(new EmergencyContact("Philippine Red Cross", "143", "Medical / Rescue", true));
        CONTACTS.add(new EmergencyContact("DLSU Manila Trunk Line 1 (ask for Safety & Security)",
                "(632) 8524 4611", "Campus Security", true));
        CONTACTS.add(new EmergencyContact("DLSU Manila Trunk Line 2 (ask for Safety & Security)",
                "(632) 8465 8900", "Campus Security", false));
        CONTACTS.add(new EmergencyContact("National Disaster Risk Reduction and Management Council (NDRRMC)",
                "(02) 8911 1406", "National / Disaster", false));
    }
}
