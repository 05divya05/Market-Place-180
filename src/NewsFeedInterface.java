import java.util.List;

/**
 * The NewsFeedInterface provides methods for displaying items based on various criteria.
 * It supports operations like fetching the latest items, popular items, searching for items,
 * and sorting items by price in ascending or descending order.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface NewsFeedInterface {

    /**
     * Retrieves the most recently posted items.
     *
     * @return A list of Item objects representing the latest items.
     */
    List<Item> getLatestItems();

    /**
     * Retrieves the most popular items based on likes or views.
     *
     * @return A list of Item objects representing the most popular items.
     */
    List<Item> getPopularItems();

    /**
     * Searches for items matching a given keyword.
     *
     * @param keyword The keyword to search for.
     * @return A list of Item objects that match the search criteria.
     */
    List<Item> searchItems(String keyword);

    /**
     * Retrieves items sorted by price in ascending order.
     *
     * @return A list of Item objects sorted by price from low to high.
     */
    List<Item> sortByPriceAscending();

    /**
     * Retrieves items sorted by price in descending order.
     *
     * @return A list of Item objects sorted by price from high to low.
     */
    List<Item> sortByPriceDescending();
}
