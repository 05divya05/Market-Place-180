package src;

public class ItemDatabaseTest {
    @Test
    public void testInsertAndLoad() {
        ItemDatabase db = new ItemDatabase();
        Item item = new Item("item101", "Mouse", "Wireless mouse", 25.0, "Electronics", "seller001");

        assertTrue(db.save(item));

        Item loaded = db.load("item101");
        assertNotNull(loaded);
        assertEquals("Mouse", loaded.getName());
    }

    @Test
    public void testUpdate() {
        ItemDatabase db = new ItemDatabase();
        Item item = new Item("item102", "Keyboard", "Standard keyboard", 30.0, "Electronics", "seller002");

        db.insert(item);
        item.setPrice(35.0);
        item.setName("Mechanical Keyboard");

        assertTrue(db.update(item));
        assertEquals("Mechanical Keyboard", db.load("item102").getName());
    }

    @Test
    public void testDelete() {
        ItemDatabase db = new ItemDatabase();
        Item item = new Item("item103", "Monitor", "HD monitor", 100.0, "Electronics", "seller003");

        db.insert(item);
        assertTrue(db.delete("item103"));
        assertNull(db.load("item103"));
    }
}
