package rating;

/**
 * The rating.RatingInterface handles ratings for sellers.
 * Buyers can provide ratings for sellers, and the system displays average scores for sellers.
 *
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 * @version 4/1/2025
 */
public interface RatingInterface {

    /**
     * Adds a rating for a specified seller.
     *
     * @param sellerID The ID of the seller.
     * @param buyerID The ID of the buyer.
     * @param score The numerical score of the rating (e.g. 0, 0.5, 1, 1.5 similar to the star system).
     */
    void addRating(String sellerID, String buyerID, double score);

    /**
     * Retrieves the average rating for a specified seller.
     *
     * @param sellerID The ID of the seller.
     * @return The average rating score.
     */
    double getAverageRating(String sellerID);
}