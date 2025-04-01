public interface ItemInterface {
    void createItem(String name,
                    String description,
                    double price,
                    String category);
    boolean deleteItem(String itemID);
    Item getItem(String itemID);
}