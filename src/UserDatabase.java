
import java.sql.*;
/**
 * The UserDatabase class handles SQL operations for user accounts.
 * Allows saving, loading, deleting, and updating users in the database.
 * Uses JDBC and assumes a valid SQL connection is provided.
 *
 * @author Sultan AlQahtani
 * @version 4/5/2025
 */
public class UserDatabase implements DatabaseInterface {

    private Connection conn;
    /**
     * Constructs a UserDatabase using the given SQL connection.
     *
     * @param conn the SQL database connection
     */
    public UserDatabase(Connection conn) {
        this.conn = conn;
    }
    /**
     * Saves a new user to the database.
     *
     * @param obj the user object to save (must be a UserProfile)
     */
    @Override
    public void save(Object obj) {
        UserProfile user = (UserProfile) obj;
        String sql = "INSERT INTO users (userID, username, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUserID());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
            ;
        }
    }
    /**
     * Loads a user from the database using their username.
     *
     * @param username the username to search for
     * @return the UserProfile if found, or null if not
     */
    @Override
    public Object load(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Deletes a user from the database by username.
     *
     * @param username the username of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Updates a user's email and password in the database.
     *
     * @param obj the updated user object (must be a UserProfile)
     */
    @Override
    public void update(Object obj) {
        UserProfile user = (UserProfile) obj;
        String sql = "UPDATE users SET email = ?, password = ? WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getUsername());
             stmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
            ;
        }
    }
}
