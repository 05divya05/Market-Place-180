package database;

import org.junit.Test;
import user.UserProfile;

import static org.junit.Assert.*;
import java.sql.*;

public class UserDatabaseTest {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    private void setupSchema(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE users (userID TEXT, username TEXT PRIMARY KEY, email TEXT, password TEXT)");
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        Connection conn = getConnection();
        setupSchema(conn);
        UserDatabase db = new UserDatabase(conn);

        UserProfile user = new UserProfile("sam", "sam@mail.com", "pass123");
        db.save(user);

        UserProfile loaded = (UserProfile) db.load("sam");
        assertNotNull(loaded);
        assertEquals("sam@mail.com", loaded.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        Connection conn = getConnection();
        setupSchema(conn);
        UserDatabase db = new UserDatabase(conn);

        UserProfile user = new UserProfile("lisa", "lisa@mail.com", "oldpass");
        db.save(user);

        user.setEmail("lisa@new.com");
        user.setPassword("newpass");
        db.update(user);

        UserProfile updated = (UserProfile) db.load("lisa");
        assertEquals("lisa@new.com", updated.getEmail());
        assertEquals("newpass", updated.getPassword());
    }

    @Test
    public void testDelete() throws Exception {
        Connection conn = getConnection();
        setupSchema(conn);
        UserDatabase db = new UserDatabase(conn);

        UserProfile user = new UserProfile("mike", "mike@mail.com", "abc");
        db.save(user);
        assertTrue(db.delete("mike"));
        assertNull(db.load("mike"));
    }
}

