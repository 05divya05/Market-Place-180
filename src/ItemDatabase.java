

public class ItemDatabase implements DatabaseInterface
{
    @Override
    public void save(Item item) {
        String sql = "INSERT INTO items (itemID, name, description, price, category, sellerID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemID());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getCategory());
            pstmt.setString(6, item.getSellerID());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
           // return false;
        }
    }

    @Override
    public Object load(String id) {
        String sql = "SELECT * FROM items WHERE itemID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Item(
                        rs.getString("itemID"),
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

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM items WHERE itemID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void update(Object item) {
        String sql = "UPDATE items SET name=?, description=?, price=?, category=? WHERE itemID=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());

            pstmt.setString(2, item.getDescription());

            pstmt.setDouble(3, item.getPrice());

            pstmt.setString(4, item.getCategory());

            pstmt.setString(5, item.getItemID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }


    }
}
