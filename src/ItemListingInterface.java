import java.util.ArrayList
/**
 * ItemListingInterface is for liking, favoriting, searching items, etc.
 * Not for creating item!
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface ItemListingInterface {

    void incrementUpvotes();

    void incrementDownvotes();

    String getAuthor();

    void setAuthor(String author);

    String getCaption();

    void setCaption(String caption);

    String getImagePath();

    void setImagePath(String imagePath);

    String getDate();

    void setDate(String date);

    int getUpvotes();

    void setUpvotes(int upvotes);

    int getDownvotes();

    void setDownvotes(int downvotes);

    ArrayList<Commentable> getComments();

    void addComment(Commentable comment);

    void setComments(ArrayList<Commentable> comments);

    String toString();

    static void deletePost(String caption) {}

    static ArrayList<Commentable> findComments(String captionOfPost) {
        return null;
    }

    static ArrayList<Commentable> findCommentsForUser(String user1) {
        return null;
    }
}
