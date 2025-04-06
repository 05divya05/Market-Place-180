
import java.sql.*;

public class UserDatabase implements DatabaseInterface {

    private Connection conn;

    public UserDatabase(Connection conn) {
        this.conn = conn;
    }

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
