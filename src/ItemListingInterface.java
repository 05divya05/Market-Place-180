/**
 * The NewsFeedInterface provides methods for displaying items based on various criteria.
 * It supports operations like fetching the latest items, popular items, searching for items,
 * and sorting items by price in ascending or descending order.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface ItemListingInterface {
    void incrementUpvotes();
    void incrementDownvotes();
    String getAuthor();
    void setAuthor(String author);
    String getTitle();
    void setTitle(String title);
    String getImagePath();
    void setImagePath(String imagePath);
    String getDate();
    void setDate(String date);
    int getDownvotes();
    void setDownvotes(int downvotes);
    String toString();

    static void deletePost(String title) {

    }
}
