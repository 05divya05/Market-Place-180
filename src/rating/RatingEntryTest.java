package rating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RatingEntryTest {

    private RatingEntry ratingEntry;

    // This will run before each test
    @BeforeEach
    public void setUp() {
        // Setup: create a new rating.RatingEntry object with test data
        ratingEntry = new RatingEntry("seller123", "buyer456", 4.5);
    }

    @Test
    public void testGetSellerID() {
        // Check if the seller ID is correctly assigned
        assertEquals("seller123", ratingEntry.getSellerID(), "Seller ID should be 'seller123'");
    }

    @Test
    public void testGetBuyerID() {
        // Check if the buyer ID is correctly assigned
        assertEquals("buyer456", ratingEntry.getBuyerID(), "Buyer ID should be 'buyer456'");
    }

    @Test
    public void testGetScore() {
        // Check if the rating score is correctly assigned
        assertEquals(4.5, ratingEntry.getScore(), "Score should be 4.5");
    }

    @Test
    public void testScoreRange() {
        // Test invalid score assignment
        RatingEntry invalidRating = new RatingEntry("seller789", "buyer101", -1.0);
        assertTrue(invalidRating.getScore() < 0.0 || invalidRating.getScore() > 5.0,
                "Score should be between 0.0 and 5.0");

        invalidRating = new RatingEntry("seller789", "buyer101", 6.0);
        assertTrue(invalidRating.getScore() < 0.0 || invalidRating.getScore() > 5.0,
                "Score should be between 0.0 and 5.0");
    }

    // Additional test to verify rating.RatingEntry behavior when created with different parameters
    @Test
    public void testDifferentRatingEntry() {
        // Create another rating entry to ensure the class behaves as expected
        RatingEntry newRatingEntry = new RatingEntry("seller789", "buyer123", 3.0);
