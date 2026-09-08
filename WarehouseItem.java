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
