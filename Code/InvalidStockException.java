// InvalidStockException.java
public class InvalidStockException extends Exception {
    public InvalidStockException(String message) {
        super(message);
    }
}


// StockOperations.java
public interface StockOperations {
    void restock(int amount) throws InvalidStockException;
    void reduceStock(int amount) throws InvalidStockException;
}


// WarehouseItem.java
import java.io.Serializable;

public abstract class WarehouseItem implements StockOperations, Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int quantity;

    public WarehouseItem(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }

    @Override
    public void restock(int amount) throws InvalidStockException {
        if (amount <= 0) {
            throw new InvalidStockException("Restock amount must be positive. Given: " + amount);
        }
        this.quantity += amount;
    }

    @Override
    public void reduceStock(int amount) throws InvalidStockException {
        if (amount <= 0) {
            throw new InvalidStockException("Deduction amount must be positive. Given: " + amount);
        }
        if (this.quantity < amount) {
            throw new InvalidStockException("Insufficient stock for " + name + ". Available: " + this.quantity);
        }
        this.quantity -= amount;
    }
    // Abstract method for polymorphic string representations
    public abstract String getItemDetails();
}


// ElectronicsItem.java
public class ElectronicsItem extends WarehouseItem {
    private int warrantyMonths;

    public ElectronicsItem(String id, String name, int quantity, int warrantyMonths) {
        super(id, name, quantity);
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getItemDetails() {
        return String.format("[Electronics] ID: %s | Name: %s | Qty: %d | Warranty: %d months", 
                getId(), getName(), getQuantity(), getWarrantyMonths());
    }

    public int getWarrantyMonths() { return warrantyMonths; }
}


// PerishableItem.java
public class PerishableItem extends WarehouseItem {
    private String expiryDate;

    public PerishableItem(String id, String name, int quantity, String expiryDate) {
        super(id, name, quantity);
        this.expiryDate = expiryDate;
    }

    @Override
    public String getItemDetails() {
        return String.format("[Perishable] ID: %s | Name: %s | Qty: %d | Expiry: %s", 
                getId(), getName(), getQuantity(), expiryDate);
    }
}


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
        return new ArrayList<>(inventory); 
        // Return copy for thread safety
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


// OrderProcessor.java
public class OrderProcessor implements Runnable {
    private InventoryManager manager;
    private String itemId;
    private int orderQuantity;
    private String workerName;

    public OrderProcessor(InventoryManager manager, String itemId, int orderQuantity, String workerName) {
        this.manager = manager;
        this.itemId = itemId;
        this.orderQuantity = orderQuantity;
        this.workerName = workerName;
    }

    @Override
    public void run() {
        System.out.println("⚙️ [Thread-" + workerName + "] Processing order for Item ID: " + itemId);
        try {
            // Artificial processing delay simulating network lag
            Thread.sleep(1500); 

            WarehouseItem item = manager.findItem(itemId);
            if (item == null) {
                throw new InvalidStockException("Item ID " + itemId + " does not exist in inventory.");
            }

            // Target processing logic
            item.reduceStock(orderQuantity);
            System.out.println("✅ [Thread-" + workerName + "] Successfully fulfilled " + orderQuantity + " units of " + item.getName());
        } catch (InvalidStockException e) {
            System.err.println("❌ [Thread-" + workerName + "] Order Denied: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("❌ [Thread-" + workerName + "] Process interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}


// Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        manager.loadInventoryFromFile(); // Load disk state upon initialization
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to the Advanced Java Warehouse System ===");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add Electronics Item");
            System.out.println("2. Add Perishable Item");
            System.out.println("3. Display Current Inventory");
            System.out.println("4. Process Restock (Exception handling test)");
            System.out.println("5. Dispatch Concurrent Order Batch (Multithreading test)");
            System.out.println("6. Save & Exit");
            System.print("Select an option: ");

            String choice = scanner.nextLine();

            // Java Flow Control structure
            switch (choice) {
                case "1":
                    System.out.print("Enter ID: "); String eId = scanner.nextLine();
                    System.out.print("Enter Name: "); String eName = scanner.nextLine();
                    System.out.print("Enter Quantity: "); int eQty = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Warranty (Months): "); int eWarranty = Integer.parseInt(scanner.nextLine());
                    manager.addItem(new ElectronicsItem(eId, eName, eQty, eWarranty));
                    System.out.println("Item added successfully.");
                    break;

                case "2":
                    System.out.print("Enter ID: "); String pId = scanner.nextLine();
                    System.out.print("Enter Name: "); String pName = scanner.nextLine();
                    System.out.print("Enter Quantity: "); int pQty = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Expiry Date (YYYY-MM-DD): "); String pExpiry = scanner.nextLine();
                    manager.addItem(new PerishableItem(pId, pName, pQty, pExpiry));
                    System.out.println("Item added successfully.");
                    break;

                case "3":
                    System.out.println("\n--- CURRENT INVENTORY LIST ---");
                    if (manager.getInventory().isEmpty()) {
                        System.out.println("[Inventory is currently empty]");
                    } else {
                        // Dynamic Polymorphism at runtime inside loop block
                        for (WarehouseItem item : manager.getInventory()) {
                            System.out.println(item.getItemDetails());
                        }
                    }
                    break;

                case "4":
                    System.out.print("Enter Item ID to restock: ");
                    String rId = scanner.nextLine();
                    WarehouseItem rItem = manager.findItem(rId);
                    if (rItem != null) {
                        System.out.print("Enter amount to add (Try negative to trigger exception): ");
                        int amount = Integer.parseInt(scanner.nextLine());
                        try {
                            rItem.restock(amount);
                            System.out.println("Restock successful. New total: " + rItem.getQuantity());
                        } catch (InvalidStockException e) {
                            // Java Exception Handling demonstration
                            System.err.println("Caught Custom Exception: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;

                case "5":
                    System.out.print("Enter Item ID for parallel processing: ");
                    String oId = scanner.nextLine();
                    System.out.println("Spawning 2 concurrent worker threads targeting the same stock...");
                    
                    // Create distinct runnable instances running concurrently
                    Thread worker1 = new Thread(new OrderProcessor(manager, oId, 5, "Worker-Alpha"));
                    Thread worker2 = new Thread(new OrderProcessor(manager, oId, 8, "Worker-Beta"));
                    
                    worker1.start();
                    worker2.start();

                    // Main execution context waits for tasks to complete
                    try {
                        worker1.join();
                        worker2.join();
                    } catch (InterruptedException e) {
                        System.err.println("Main synchronization thread interrupted.");
                    }
                    break;

                case "6":
                    manager.saveInventoryToFile(); // Write contents out to raw storage stream
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid selection option. Please try again.");
            }
        }
        scanner.close();
    }
}
