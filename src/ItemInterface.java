/**
 * ItemInterface
 *
 * Provide basic setters and getters for Item.java
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface ItemInterface {

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    double getPrice();
    void setPrice(double price);

    String getCategory();
    void setCategory(String category);

    String getSeller();

    String getImagePath();
    void setImagePath(String path);
    void saveToFile();
}
