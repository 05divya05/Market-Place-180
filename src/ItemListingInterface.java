import java.util.List;

/**
 * ItemListingInterface is for liking, favoriting, searching items, etc.
 * Not for creating item!
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface ItemListingInterface {


    /**
     * Allows a buyer to like an item. The total like count will be updated if the operation is successful.
     *
     * @param itemID  The ID of the item.
     * @param buyerID The ID of the buyer.
     * @return true if the like is successful, false if the buyer has already liked the item before.
     */
    boolean likeItem(String itemID, String buyerID);

    /**
     * Retrieves the total number of likes for a specific item.
     *
     * @param itemID The ID of the item.
     * @return The total number of likes.
     */
    int getTotalLikes(String itemID);

    /**
     * Allows a buyer to add an item to their favorites list.
     *
     * @param userID The ID of the buyer who wants to favorite the item.
     * @param itemID The ID of the item to be favorited.
     * @return true if the item is successfully added to the favorites, false if it is already favorited.
     */
    boolean favoriteItem(String userID, String itemID);

    /**
     * Retrieves all items that a specific buyer has favorited.
     *
     * @param userID The ID of the buyer.
     * @return A list of items that the buyer has favorited.
     */
    List<Item> getFavoritesByUser(String userID);

    /**
     * Retrieves all items created by a specific seller.
     *
     * @param sellerID The ID of the seller.
     * @return A list of items published by the seller.
     */
    List<Item> getItemsBySeller(String sellerID);

    /**
     * Retrieves a specific item by its unique ID.
     *
     * @param itemID The unique ID of the item.
     * @return The Item object if found, otherwise null.
     */
    Item getItemByID(String itemID);
}