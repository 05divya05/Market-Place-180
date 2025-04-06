package src;
import java.sql.*;

public class RatingDatabase implements DatabaseInterface {

    private Connection conn;

    public RatingDatabase(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Object obj) {
        RatingEntry entry = (RatingEntry) obj;
        String sql = "INSERT INTO ratings (sellerID, buyerID, score) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entry.getSellerID());
            stmt.setString(2, entry.getBuyerID());
            stmt.setDouble(3, entry.getScore());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object load(String sellerID) {
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

    @Override
    public boolean update(Object obj) {
        return false;
    }
}
