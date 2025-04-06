import java.sql.*;
/**
 * The ItemDatabase class handles all SQL operations related to item listings.
 * Implements save, load, delete, and update functionality for items.
 *
 * @author Sultan AlQahtani
 * @version 4/5/2025
 */


public class ItemDatabase implements DatabaseInterface {

    private Connection conn;
    /**
     * Constructs a new ItemDatabase using the given SQL connection.
     *
     * @param conn the connection to the SQL database
     */
    public ItemDatabase(Connection conn) {
        this.conn = conn;
    }
    /**
     * Saves a new item to the database.
     *
     * @param obj the item object to save (cast from Object)
     */
    @Override
    public void save(Object obj) {
        Item item = (Item) obj;
        String sql = "INSERT INTO items (itemID, name, description, price, category, sellerID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemID());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getCategory());
            pstmt.setString(6, item.getSellerID());
             pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
    }
    /**
     * Loads an item from the database using its itemID.
     *
     * @param id the unique itemID
     * @return the loaded Item object, or null if not found
     */
    @Override
    public Item load(String id) {
        String sql = "SELECT * FROM items WHERE itemID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Item(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("sellerID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Deletes an item from the database based on its itemID.
     *
     * @param id the unique itemID of the item to delete
     * @return true if the item was deleted, false otherwise
     */
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM items WHERE itemID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Updates an existing item in the database.
     *
     * @param obj the updated Item object (cast from Object)
     */
    @Override
    public void update(Object obj) {
        Item item = (Item) obj;
        String sql = "UPDATE items SET name=?, description=?, price=?, category=? WHERE itemID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getCategory());
            pstmt.setString(5, item.getItemID());
             pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ;
    }
}

