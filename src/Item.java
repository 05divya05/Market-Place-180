import java.io.*;

/**
 * Item
 *
 * Simple data class that represents a marketplace listing and is able to
 * persist itself by appending a line to items.txt.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class Item implements ItemInterface {

    //Basic Attributes
    private String name;
    private String description;
    private double price;
    private String category;
    private String seller;
    private String imagePath;

    public Item(String name, String description, double price, String category, String seller, String imagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.seller = seller;
        this.imagePath = imagePath;
    }


    /**Getters & Setters*/
    @Override
    public String getName() { return name; }

    @Override
    public void setName(String name) { this.name = name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public void setDescription(String description) { this.description = description; }

    @Override
    public double getPrice() { return price; }

    @Override
    public void setPrice(double price) { this.price = price; }

    @Override
    public String getCategory() { return category; }

    @Override
    public void setCategory(String category) { this.category = category; }

    @Override
    public String getSeller() { return seller; }

    @Override
    public String getImagePath() { return imagePath; }

    @Override
    public void setImagePath(String path) { this.imagePath = path; }

    //Save Item info into files in expected format.
    @Override
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("items.txt", true))) {
            pw.println(name + "," + description + "," + price + "," + category + "," + seller + "," + imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
