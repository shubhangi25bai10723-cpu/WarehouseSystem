// Main.java (Modified for automated GitHub Actions runs)
public class Main {
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING AUTOMATED GITHUB ACTIONS RUN ===\n");
        
        InventoryManager manager = new InventoryManager();
        manager.loadInventoryFromFile(); // Verifies I/O Streams read block

        // 1. Seed initial data (OOP Inheritance verification)
        System.out.println("▶️ Seeding inventory database items...");
        manager.addItem(new ElectronicsItem("E101", "Developer Laptop", 10, 24));
        manager.addItem(new PerishableItem("P202", "Server Room Energy Drinks", 50, "2027-12-31"));

        // 2. Display Polymorphism output
        System.out.println("\n▶️ Displaying Polymorphic Inventory Details:");
        for (WarehouseItem item : manager.getInventory()) {
            System.out.println(item.getItemDetails());
        }

        // 3. Exception Handling Demonstration
        System.out.println("\n▶️ Testing Custom Exception Handling (Attempting illegal negative restock)...");
        try {
            WarehouseItem laptop = manager.findItem("E101");
            if (laptop != null) {
                laptop.restock(-5); // Will trigger the exception
            }
        } catch (InvalidStockException e) {
            System.out.println("✨ Success: Caught expected custom exception -> " + e.getMessage());
        }

        // 4. Multithreading Race-Condition Simulation
        System.out.println("\n▶️ Spawning Multithreaded Workers concurrently processing Item E101...");
        Thread worker1 = new Thread(new OrderProcessor(manager, "E101", 4, "GitHub-Runner-Alpha"));
        Thread worker2 = new Thread(new OrderProcessor(manager, "E101", 4, "GitHub-Runner-Beta"));
        
        worker1.start();
        worker2.start();

        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException e) {
            System.err.println("Main pipeline thread interrupted.");
        }

        // 5. Save State via Outbound I/O Data Streams
        System.out.println("\n▶️ Saving state back to structural disk storage stream...");
        manager.saveInventoryToFile();

        System.out.println("\n=== SYSTEM AUTOMATION TEST CONCLUDED SUCCESSFULLY ===");
    }
}
