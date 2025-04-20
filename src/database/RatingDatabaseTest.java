package database;

import org.junit.Test;
import rating.RatingEntry;

import static org.junit.Assert.*;
import java.sql.*;

public class RatingDatabaseTest {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    private void setupSchema(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE ratings (sellerID TEXT, buyerID TEXT, score REAL)");
    }

    @Test
    public void testAverageRating() throws Exception {
        Connection conn = getConnection();
        setupSchema(conn);
        RatingDatabase db = new RatingDatabase(conn);

        db.save(new RatingEntry("s1", "b1", 4.0));
        db.save(new RatingEntry("s1", "b2", 5.0));
        db.save(new RatingEntry("s1", "b3", 3.0));

        double avg = (double) db.load("s1");
        assertEquals(4.0, avg, 0.01);
    }

    @Test
    public void testDeleteRating() throws Exception {
        Connection conn = getConnection();
        setupSchema(conn);
        RatingDatabase db = new RatingDatabase(conn);

        db.save(new RatingEntry("s2", "b1", 4.5));
        assertTrue(db.delete("s2"));

        double avg = (double) db.load("s2");
        assertEquals(0.0, avg, 0.01);
    }
}
