
import java.sql.*;
/**
 * The RatingDatabase class handles storing and retrieving seller ratings.
 * Provides methods to save new ratings, load average ratings, and delete all ratings for a seller.
 * This class uses JDBC and a provided SQL connection.
 *
 * @author Sultan AlQahtani
 * @version 4/5/2025
 */
public class RatingDatabase implements DatabaseInterface {

    private Connection conn;

    /**
     * Constructs a RatingDatabase with the given SQL connection.
     *
     * @param conn the SQL database connection
     */

    public RatingDatabase(Connection conn) {
        this.conn = conn;
    }
    /**
     * Saves a new rating entry to the database.
     *
     * @param obj the rating to save (must be a RatingEntry object)
     */
    @Override
    public void save(Object obj) {
        RatingEntry entry = (RatingEntry) obj;
        String sql = "INSERT INTO ratings (sellerID, buyerID, score) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entry.getSellerID());
            stmt.setString(2, entry.getBuyerID());
            stmt.setDouble(3, entry.getScore());
             stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            ;
        }
    }
    /**
     * Loads the average rating score for a seller.
     *
     * @param sellerID the ID of the seller
     * @return the average rating score as a double, or 0.0 if no ratings found
     */
    @Override
    public Double load(String sellerID) {
        String sql = "SELECT AVG(score) AS average FROM ratings WHERE sellerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sellerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    /**
     * Deletes all ratings for a given seller.
     *
     * @param sellerID the ID of the seller
     * @return true if ratings were deleted, false otherwise
     */
    @Override
    public boolean delete(String sellerID) {
        String sql = "DELETE FROM ratings WHERE sellerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sellerID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Updates are not supported for ratings. Method is unused.
     *
     * @param obj ignored
     */
    @Override
    public void update(Object obj) {
        ;
    }
}
