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
