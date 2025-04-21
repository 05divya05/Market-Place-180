import org.junit.Test;
import static org.junit.Assert.*;

public class ItemDatabaseTest {
    @Test
    public void testInsertAndLoad() {
        ItemDatabase db = new ItemDatabase(null);
        Item item = new Item( "Mouse", "Wireless mouse", 25.0, "Electronics", "seller001");
        db.save(item);
        assertTrue(true);

        Item loaded = db.load("item101");
        assertNotNull(loaded);
        assertEquals("Mouse", loaded.getName());
    }

    @Test
    public void testUpdate() {
        ItemDatabase db = new ItemDatabase(null);
        Item item = new Item( "Keyboard", "Standard keyboard", 30.0, "Electronics", "seller002");

        db.save(item);
        item.setPrice(35.0);
        item.setName("Mechanical Keyboard");
        db.update(item);
        assertTrue(true);
        assertEquals("Mechanical Keyboard", db.load("item102").getName());
    }

    @Test
    public void testDelete() {
        ItemDatabase db = new ItemDatabase(null);
        Item item = new Item( "Monitor", "HD monitor", 100.0, "Electronics", "seller003");

        db.save(item);
        assertTrue(db.delete("item103"));
        assertNull(db.load("item103"));
    }
}
