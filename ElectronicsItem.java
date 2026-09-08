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
