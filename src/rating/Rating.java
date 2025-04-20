package rating;

import database.RatingDatabase;

import java.sql.*;

/**
 * The rating.Rating class implements the rating.RatingInterface to handle seller ratings.
 * Uses database.RatingDatabase for database operations.
 *
 *
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 * @version 4/5/2025
 */
public class Rating implements RatingInterface {

    private RatingDatabase ratingDatabase;

    /**
     * Constructs a rating.Rating object with a specified database file path.
     *
     * @param dbPath The path to the database file.
     * @throws SQLException If a database access error occurs.
     */
    public Rating(String dbPath) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        ratingDatabase = new RatingDatabase(conn);
    }

    /**
     * Adds a rating for a specified seller.
     *
     * @param sellerID The ID of the seller.
     * @param buyerID The ID of the buyer.
     * @param score The numerical score of the rating (0.0 to 5.0).
     */
    @Override
    public void addRating(String sellerID, String buyerID, double score) {
        if (score < 0.0 || score > 5.0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 5.0.");
        }

        RatingEntry entry = new RatingEntry(sellerID, buyerID, score);
        ratingDatabase.save(entry);  // Using database.RatingDatabase's save method
    }

    /**
     * Retrieves the average rating.
     *
     * @param sellerID The ID of the seller.
     * @return The average rating score.
     */
    @Override
    public double getAverageRating(String sellerID) {
        return ratingDatabase.load(sellerID);
    }
}