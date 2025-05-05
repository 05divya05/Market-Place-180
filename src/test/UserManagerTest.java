import org.junit.jupiter.api.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private static final String USER_FILE = "users.txt";
    private UserManager mgr;

    @BeforeEach
    void setup() {
        // 删除残留文件
        new File(USER_FILE).delete();
        mgr = new UserManager();
    }

    @Test
    void testRegisterAndLoginSuccess() {
        assertTrue(mgr.register("alice", "a@b.com", "secret6"));
        assertTrue(mgr.login("alice", "a@b.com", "secret6"));
    }

    @Test
    void testRegisterFailsOnBadEmailOrPwd() {
        assertFalse(mgr.register("bob", "not-an-email", "123456"));
        assertFalse(mgr.register("bob", "b@c.com", "123")); // too short
    }

    @Test
    void testDuplicateRegister() {
        assertTrue(mgr.register("carol", "c@d.com", "password"));
        // same username
        assertFalse(mgr.register("carol", "new@e.com", "password"));
        // same email
        assertFalse(mgr.register("newname", "c@d.com", "password"));
    }

    @Test
    void testBalanceSetAndGet() {
        mgr.register("dave", "d@e.com", "mypwd6");
        assertEquals(100.0, mgr.getBalance("dave"), 1e-6);
        assertTrue(mgr.setBalance("dave", 42.5));
        assertEquals(42.50, mgr.getBalance("dave"), 1e-6);
    }

    @Test
    void testDeleteAccount() {
        mgr.register("eve", "e@f.com", "pass66");
        assertTrue(mgr.deleteAccount("eve", "e@f.com", "pass66"));
        assertFalse(mgr.login("eve", "e@f.com", "pass66"));
    }
}
