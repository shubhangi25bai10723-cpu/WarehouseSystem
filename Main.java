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
