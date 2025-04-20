package item;

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
    private boolean isDeleted; //added by steven

    public Item(String name, String description, double price, String category, String sellerID) {
        this.itemID = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sellerID = sellerID;
        this.isDeleted = false;
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
        if (name == null) {
            throw new IllegalArgumentException("item.Item name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("item.Item name cannot be empty");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("item.Item name cannot exceed 30 characters");
        }
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (description.length() > 1500) {
            throw new IllegalArgumentException("Description cannot exceed 1500 characters");
        }
        this.description = description;
    }

    @Override
    public double getPrice() {
        return Math.round(price * 100.0) / 100.0; //ensure 2 decimal places
    }

    @Override
    public void setPrice(double price) {
        //ensure price is not negative
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = Math.round(price * 100.0) / 100.0; //ensure 2 decimal places
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    @Override
    public String getSellerID() {
        return sellerID;
    }

    @Override
    public void deleteItem() {
        this.isDeleted = true;
    }

    /**
     * checks if item has been marked as deleted
     *
     * @return true if the item has been deleted, else false
     */
    public boolean isDeleted() {
        return isDeleted;
    }
}