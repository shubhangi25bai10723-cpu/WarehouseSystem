// StockOperations.java
public interface StockOperations {
    void restock(int amount) throws InvalidStockException;
    void reduceStock(int amount) throws InvalidStockException;
}
