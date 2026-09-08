// InventoryManager.java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<WarehouseItem> inventory = new ArrayList<>();
    private final String DATA_FILE = "warehouse_inventory.dat";

    public synchronized void addItem(WarehouseItem item) {
        inventory.add(item);
    }

    public synchronized List<WarehouseItem> getInventory() {
        return new ArrayList<>(inventory); // Return copy for thread safety
    }

    public synchronized WarehouseItem findItem(String id) {
        for (WarehouseItem item : inventory) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    // Save state using Byte/Object Output Streams
    public synchronized void saveInventoryToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(inventory);
            System.out.println("💾 Inventory state saved successfully to file.");
        } catch (IOException e) {
            System.err.println("❌ Error saving inventory to file: " + e.getMessage());
        }
    }

    // Load state using Byte/Object Input Streams
    @SuppressWarnings("unchecked")
    public synchronized void loadInventoryFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("ℹ️ No previous inventory file found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            inventory = (List<WarehouseItem>) ois.readObject();
            System.out.println("📂 Inventory state loaded successfully from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error loading inventory file: " + e.getMessage());
        }
    }
}
