import java.util.ArrayList;
import java.util.List;

public class ItemManager implements ItemInterface {

    //Storing all items
    private final List<Item> items;

    public ItemManager() {
        items = new ArrayList<>();
    }

    @Override
    public void createItem(String name, String description, double price, String category) {
        Item item = new Item(name, description, price, category);
        items.add(item);
        System.out.println("Item created successfully: " + item.getName());
    }

    @Override
    public boolean deleteItem(String itemID) {
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                items.remove(item);
                System.out.println("Item deleted successfully: " + item.getName());
                return true;
            }
        }
        System.out.println("Item not found with ID: " + itemID);
        return false;
    }

    @Override
    public Item getItem(String itemID) {
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return items;
    }
}