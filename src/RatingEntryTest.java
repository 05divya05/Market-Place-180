import org.junit.Test;
import static org.junit.Assert.*;

public class RatingEntryTest {

    @Test
    public void testRatingEntryCreation() {
        RatingEntry ratingEntry = new RatingEntry("seller123", "buyer456", 4.5);

        assertEquals("seller123", ratingEntry.getSellerID());
        assertEquals("buyer456", ratingEntry.getBuyerID());
        assertEquals(4.5, ratingEntry.getScore(), 0.0);
    }

    @Test
    public void testScoreBoundaryValues() {
        RatingEntry minScoreEntry = new RatingEntry("seller001", "buyer001", 0.0);
        RatingEntry maxScoreEntry = new RatingEntry("seller002", "buyer002", 5.0);

        assertEquals(0.0, minScoreEntry.getScore(), 0.0);
        assertEquals(5.0, maxScoreEntry.getScore(), 0.0);
    }

    @Test
    public void testInvalidScore() {
        try {
            RatingEntry invalidScoreEntry = new RatingEntry("seller003", "buyer003", -1.0);
            fail("Creating a RatingEntry with a negative score should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Score must be between 0.0 and 5.0.", e.getMessage());
        }

        try {
            RatingEntry invalidScoreEntry = new RatingEntry("seller004", "buyer004", 6.0);
            fail("Creating a RatingEntry with a score above 5.0 should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            assertEquals("Score must be between 0.0 and 5.0.", e.getMessage());
        }
    }

    @Test
    public void testValidScoreRange() {
        RatingEntry validEntry = new RatingEntry("seller005", "buyer005", 3.5);

        assertTrue(validEntry.getScore() >= 0.0 && validEntry.getScore() <= 5.0);
    }
}
