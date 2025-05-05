
public interface NewsCommentInterface {

    /**
     * Retrieves the number of upvotes for the comment.
     *
     * @return the number of upvotes
     */
    int getUpvotes();

    /**
     * Retrieves the number of downvotes for the comment.
     *
     * @return the number of downvotes
     */
    int getDownvotes();

    /**
     * Retrieves the text content of the comment.
     *
     * @return the content of the comment
     */
    String getContent();


    String getAuthor();

    void incrementUpvotes();


    void incrementDownvotes();

    /**
     * Saves the comment to a file in a persistent storage.
     */
    void saveToFile();
}
