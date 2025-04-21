import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * The Rating class handles adding ratings and retrieving average ratings for sellers.
 * It uses the RatingDatabase to interact with the database.
 * Example:
 * Rating rating = new Rating(connection);
 * rating.addRating("seller1", "buyer1", 4.5);
 * double average = rating.getAverageRating("seller1");
 *
 *
 * @author Yihang Li
 * @version 4/6/2025
 */
public class Rating implements RatingInterface {

    private RatingDatabase ratingDatabase;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0");

    /**
     * Initializes a Rating object with a given SQL connection.
     *
     * @param conn The SQL connection to the ratingdatabase.
     */
    public Rating(Connection conn) {
        this.ratingDatabase = new RatingDatabase(conn);
    }

    /**
     * Adds a new rating for a seller from a buyer.
     *
     * @param sellerID The ID of the seller.
     * @param buyerID The ID of the buyer.
     * @param score The rating score (0.0 to 5.0).
     */
    @Override
    public void addRating(String sellerID, String buyerID, double score) {
        if (score < 0.0 || score > 5.0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 5.0.");
        }

        RatingEntry entry = new RatingEntry(sellerID, buyerID, score);
        ratingDatabase.save(entry);
    }

    /**
     * Gets the average rating for a specific seller, formatted to one decimal place.
     *
     * @param sellerID The ID of the seller.
     * @return The average rating of the seller formatted to one decimal place
     */
    @Override
    public double getAverageRating(String sellerID) {
        double average = ratingDatabase.load(sellerID);
        return Double.parseDouble(String.format("%.1f", average));
    }
}