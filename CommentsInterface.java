import java.util.List;

/**
 * The CommentsInterface allows buyers to leave comments on item and sellers to reply to those comments.
 * (All IDs can be changed into int, depends on what we want)
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface CommentsInterface {

    /**
     * Allows a buyer to post a comment under an item.
     *
     * @param itemID The ID of the item.
     * @param buyerID The ID of the buyer.
     * @param content The content of the comment.
     */
    void postComment(String itemID, String buyerID, String content);

    /**
     * Allows a seller to reply to a comment made by a buyer.
     *
     * @param commentID The ID of the comment being replied to.
     * @param sellerID The ID of the seller.
     * @param content The content of the reply.
     */
    void replyToComment(String commentID, String sellerID, String content);

    /**
     * Retrieves all comments and replies for a specified item.
     *
     * @param itemID The ID of the item.
     * @return A list of Comment objects related to the item.
     */
    List<Comment> getComments(String itemID);
}