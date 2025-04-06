/**
 * The RatingEntry class represents a single rating given by a buyer to a seller.
 * Contains the seller ID, buyer ID, and the score (used in RatingDatabase).
 *
 * @author Sultan AlQahtani
 * @version 4/5/2025
 */
public class RatingEntry {
    private String sellerID;
    private String buyerID;
    private double score;
    /**
     * Constructs a new RatingEntry with the given seller, buyer, and score.
     *
     * @param sellerID the ID of the seller being rated
     * @param buyerID the ID of the buyer submitting the rating
     * @param score the score given (e.g., 0.0 to 5.0)
     */
    public RatingEntry(String sellerID, String buyerID, double score) {
        this.sellerID = sellerID;
        this.buyerID = buyerID;
        this.score = score;
    }
    /**
     * Gets the seller ID.
     *
     * @return the seller's ID
     */
    public String getSellerID() { return sellerID; }
    /**
     * Gets the buyer ID.
     *
     * @return the buyer's ID
     */
    public String getBuyerID() { return buyerID; }
    /**
     * Gets the rating score.
     *
     * @return the numerical score
     */
    public double getScore() { return score; }
}
