import java.util.ArrayList;

public interface NewsFeedInterface {

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

    ArrayList<NewsComment> getComments();

    void addComment(NewsComment comment);

    void setComments(ArrayList<NewsComment> comments);

    String toString();

    static void deletePost(String caption) {}

    static ArrayList<NewsComment> findComments(String captionOfPost) {
        return null;
    }

    static ArrayList<NewsComment> findCommentsForUser(String user1) {
        return null;
    }
}
