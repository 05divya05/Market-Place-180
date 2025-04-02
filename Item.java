import java.util.UUID;

/**
 * This class is for creating, editing and retrieving items.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public class Item implements ItemInterface {

    private final String itemID;
    private String name;
    private String description;
    private double price;
    private String category;
    private final String sellerID;

    public Item(String name, String description, double price, String category, String sellerID) {
        this.itemID = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerID = sellerID;
    }

    @Override
    public String getItemID() {
        return itemID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getSellerID() {
        return sellerID;
    }
}