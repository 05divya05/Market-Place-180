package rating;

import static org.mockito.Mockito.*;

import database.RatingDatabase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

public class RatingTest {

    private Rating rating;
    private RatingDatabase ratingDatabase;

    @Before
    public void setUp() throws SQLException {
        // Mock database.RatingDatabase to simulate database operations
        ratingDatabase = mock(RatingDatabase.class);
        rating = new Rating("test.db");

        // Inject the mocked database.RatingDatabase into the rating.Rating object
        ratingDatabase = mock(RatingDatabase.class);
        rating = new Rating("test.db");
    }

    // Test case for adding a valid rating
    @Test
    public void testAddRating() {
        try {
            // Test data
            String sellerID = "seller1";
            String buyerID = "buyer1";
            double score = 4.5;

            // Perform the operation
            rating.addRating(sellerID, buyerID, score);

            // Verify that the save method was called with the correct rating.RatingEntry
            verify(ratingDatabase).save(any(RatingEntry.class));
        } catch (Exception e) {
            fail("Exception should not be thrown for valid rating");
        }
    }

    // Test case for adding an invalid rating (score out of range)
    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidRating() {
        // Test data
        String sellerID = "seller1";
        String buyerID = "buyer1";
        double score = 6.0;  // Invalid score (should be between 0 and 5)

        // Perform the operation (should throw IllegalArgumentException)
        rating.addRating(sellerID, buyerID, score);
    }

    // Test case for retrieving the average rating for a seller
    @Test
    public void testGetAverageRating() {
        try {
            // Test data
            String sellerID = "seller1";
            double expectedAverage = 4.0;

            // Mock the load method to return a predefined average rating
            when(ratingDatabase.load(sellerID)).thenReturn(expectedAverage);

            // Call the method
            double actualAverage = rating.getAverageRating(sellerID);

            // Verify that the correct average rating is returned
            assertEquals("Average rating should match", expectedAverage, actualAverage, 0.01);
        } catch (SQLException e) {
            fail("SQLException should not be thrown during test.");
        }
    }

    // Test case for the case where the seller has no ratings
    @Test
    public void testGetAverageRatingNoRatings() {
        try {
            String sellerID = "seller2";
            when(ratingDatabase.load(sellerID)).thenReturn(0.0);  // No ratings, should return 0.0

            double actualAverage = rating.getAverageRating(sellerID);

            // Check if the average is 0.0 for sellers with no ratings
            assertEquals("Average rating should be 0.0 for sellers with no ratings", 0.0, actualAverage, 0.01);
        } catch (SQLException e) {
            fail("SQLException should not be thrown during test.");
        }
    }
}
