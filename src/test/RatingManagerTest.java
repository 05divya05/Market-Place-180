import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class RatingManagerTest {
    private static final String RAT_FILE = "ratings.txt";
    private RatingManager rm;
    private Client dummy;

    @BeforeEach
    void setup() {
        new File(RAT_FILE).delete();
        // 用一个 Client stub，只实现 send/read，直接操作文件
        dummy = new Client("localhost", 4242) {
            @Override public void send(String s) {
                // reuse Message logic: write to ratings.txt
                try (PrintWriter pw = new PrintWriter(new FileWriter(RAT_FILE, true))) {
                    String[] p = s.split("\\|");
                    if ("ADD_RATING".equals(p[0]) && p.length==4) {
                        pw.println(p[2] + "," + p[1] + "," + p[3]);
                    }
                } catch (Exception ignore) {}
            }
            @Override public String read() { return null; }
            @Override public java.util.List<String> readBlock() { return null; }
        };
        rm = new RatingManager(dummy);
    }

    @Test
    void testAddAndGetAverage() {
        assertTrue(rm.addRating("x","y",4.0));
        assertTrue(rm.addRating("z","y",2.0));
        // GET_RATING: simulate by reading file directly
        double avg = rm.getAverage("y");
        assertEquals(3.0, avg, 1e-6);
    }

    @Test
    void testGetAverageNone() {
        assertEquals(-1, rm.getAverage("unknown"), 1e-6);
    }
}
