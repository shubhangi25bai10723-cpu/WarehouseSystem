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
