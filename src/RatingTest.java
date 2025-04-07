import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RatingTest {

    private Connection createTestConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    @Test
    public void testAddRating() {
        try (Connection conn = createTestConnection()) {
            Rating rating = new Rating(conn);

            // Setting up the database table for testing
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS ratings ("
                    + "sellerID TEXT, "
                    + "buyerID TEXT, "
                    + "score REAL)");

            rating.addRating("seller1", "buyer1", 4.5);
            rating.addRating("seller1", "buyer2", 5.0);
            rating.addRating("seller1", "buyer3", 3.5);

            double average = rating.getAverageRating("seller1");
            assertEquals(4.3, average, 0.1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred during testAddRating.");
        }
    }

    @Test
    public void testAddInvalidRating() {
        try (Connection conn = createTestConnection()) {
            Rating rating = new Rating(conn);

            // Setting up the database table for testing
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS ratings ("
                    + "sellerID TEXT, "
                    + "buyerID TEXT, "
                    + "score REAL)");

            try {
                rating.addRating("seller1", "buyer1", 6.0); // Invalid score
                fail("Expected IllegalArgumentException for invalid score.");
            } catch (IllegalArgumentException e) {
                assertEquals("Score must be between 0.0 and 5.0.", e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred during testAddInvalidRating.");
        }
    }

    @Test
    public void testGetAverageRating_NoRatings() {
        try (Connection conn = createTestConnection()) {
            Rating rating = new Rating(conn);

            // Setting up the database table for testing
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS ratings ("
                    + "sellerID TEXT, "
                    + "buyerID TEXT, "
                    + "score REAL)");

            double average = rating.getAverageRating("nonexistentSeller");
            assertEquals(0.0, average, 0.0);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred during testGetAverageRating_NoRatings.");
        }
    }
}