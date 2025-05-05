import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ItemManagerTest {
    private static final String ITEM_FILE = "itemListing.txt";
    private ItemManager mgr;

    @BeforeEach
    void setup() throws Exception {
        new File(ITEM_FILE).delete();
        mgr = new ItemManager();
    }

    @Test
    void testAddAndGetAll() throws Exception {
        ItemListing it = new ItemListing("s1","t1","d",10.0,"cat","img",5);
        assertTrue(mgr.addItem(it));
        List<ItemListing> all = mgr.getAll();
        assertEquals(1, all.size());
        assertEquals("t1", all.get(0).getTitle());
    }

    @Test
    void testEditItem() throws Exception {
        ItemListing it = new ItemListing("s2","t2","d",20.0,"cat","img",3);
        mgr.addItem(it);
        ItemListing updated = new ItemListing("s2","t2","newdesc",25.0,"cat2","img2",2);
        assertTrue(mgr.editItem("s2","t2", updated));
        ItemListing found = mgr.find("s2","t2");
        assertEquals("newdesc", found.getDescription());
        assertEquals(25.0, found.getPrice(), 1e-6);
    }

    @Test
    void testDeleteItem() throws Exception {
        ItemListing it = new ItemListing("s3","t3","d",5.0,"cat","img",1);
        mgr.addItem(it);
        assertTrue(mgr.deleteItem("s3","t3"));
        assertNull(mgr.find("s3","t3"));
    }
}
