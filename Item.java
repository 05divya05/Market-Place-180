public class Item {
    //Item info
    private final String itemID;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean sold;

    public Item(String name, String description, double price, String category) {
        //ceate ID
        //itemID = ;

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.sold = false;
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }
}
}