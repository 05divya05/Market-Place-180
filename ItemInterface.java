/**
 * The ItemInterface is used for creating item.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface ItemInterface {

    /**
     * Gets the ID of the item.
     *
     * @return The item's ID.
     */
    String getItemID();

    /**
     * Gets the name of the item.
     *
     * @return The item's name.
     */
    String getName();

    /**
     * Sets the name of the item.
     *
     * @param name The new name of the item.
     */
    void setName(String name);

    /**
     * Gets the description of the item.
     *
     * @return The item's description.
     */
    String getDescription();

    /**
     * Sets the description of the item.
     *
     * @param description The new description of the item.
     */
    void setDescription(String description);

    /**
     * Gets the price of the item.
     *
     * @return The item's price.
     */
    double getPrice();

    /**
     * Sets the price of the item.
     *
     * @param price The new price of the item.
     */
    void setPrice(double price);

    /**
     * Gets the category of the item.
     *
     * @return The item's category.
     */
    String getCategory();

    /**
     * Sets the category of the item.
     *
     * @param category The new category of the item.
     */
    void setCategory(String category);

    /**
     * Gets the ID of the seller who listed the item.
     *
     * @return The seller's ID.
     */
    String getSellerID();
}