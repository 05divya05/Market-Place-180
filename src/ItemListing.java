import java.io.Serializable;

/**
 * ItemListing
 *
 * Provides simple getters/setters plus two helper methods to convert
 * to/from the CSV line format stored in items.txt.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class ItemListing implements Serializable {
    private final String seller;
    private String title;
    private String description;
    private String category;
    private String imagePath;
    private double price;
    private int quantity;

    public ItemListing(String seller,String title,String desc,double price,
                       String cat,String img,int qty){
        this.seller=seller;
        this.title=title;
        this.description=desc;
        this.price=price;
        this.category=cat;
        this.imagePath=img;
        this.quantity=qty;
    }

    public String getSeller() {
        return seller;
    }

    public String getTitle() {
        return title;
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

    public String getImagePath() {
        return imagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setDescription(String d) {
        description=d;
    }

    public void setPrice(double p) {
        price=p;
    }

    public void setCategory(String c) {
        category=c;
    }

    public void setImagePath(String p) {
        imagePath=p;
    }

    public void setQuantity(int q) {
        quantity=q;
    }

    public String toFile() {
        return title+","+description+","+price+","+seller+","+imagePath+","+category+","+quantity;
    }

    /**
     * Creates an ItemListing from each line. Returns null if the line is malformed.
     */
    public static ItemListing fromFile(String l) {
        String[] f = l.split(",",7);
        if(f.length!=7) return null;
        return new ItemListing(f[3],  // seller
                f[0], // title
                f[1], // description
                Double.parseDouble(f[2]),// price
                f[5], // category
                f[4], // image path
                Integer.parseInt(f[6]));// quantity
    }
}
