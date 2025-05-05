import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    private static final String MSG_FILE = "messages.txt";
    private Message msg;

    @BeforeEach
    void setup() {
        new File(MSG_FILE).delete();
        msg = new Message();
    }

    @Test
    void testSendAndLoadHistory() throws Exception {
        msg.send("u1","u2","hello");
        msg.send("u2","u1","hi");
        List<String> hist = msg.loadHistory("u1","u2");
        assertEquals(2, hist.size());
        assertTrue(hist.get(0).startsWith("u1: hello"));
    }

    @Test
    void testListChats() throws Exception {
        msg.send("a","b","m1");
        msg.send("c","a","m2");
        List<String> chats = msg.listChats("a");
        assertEquals(2, chats.size());
        // sorted key always same order
        assertTrue(chats.contains("a,b"));
        assertTrue(chats.contains("a,c"));
    }
}
