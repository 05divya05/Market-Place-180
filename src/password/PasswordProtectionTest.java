package password;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

public class PasswordProtectionTest {

    // Test case for loading users from file
    @Test
    public void testLoadUsersFromFile() throws IOException {
        // Create a new Password.PasswordProtection object to load users from the file
        PasswordProtection passwordProtection = new PasswordProtection();

        // Verify that users are loaded from the file and added to the users list
        List<String> users = PasswordProtection.getUsers();
        List<String> passes = PasswordProtection.getPasses();

        // Check that users and passwords lists are not empty
        assertFalse("user.Users list is empty", users.isEmpty());
        assertFalse("Passwords list is empty", passes.isEmpty());

        // Verify that the first user in the list is as expected (test for an actual user in the file)
        assertEquals("TestUser", users.get(0));  // Replace "TestUser" with an actual user in your users.txt file
        assertEquals("TestPassword", passes.get(0));  // Replace "TestPassword" with the actual password
    }

    // Test case for successful authentication
    @Test
    public void testAuthenticateSuccess() {
        // Create a new Password.PasswordProtection object
        PasswordProtection passwordProtection = new PasswordProtection();

        // Assume these values exist in the users.txt file
        String username = "TestUser";
        String password = "TestPassword";

        // Call authenticate method and assert that it returns true for correct credentials
        boolean result = passwordProtection.authenticate(username, password);
        assertTrue("Authentication failed with valid credentials", result);
    }

    // Test case for unsuccessful authentication due to incorrect password
    @Test
    public void testAuthenticateIncorrectPassword() {
        // Create a new Password.PasswordProtection object
        PasswordProtection passwordProtection = new PasswordProtection();

        // Assume these values exist in the users.txt file, but use an incorrect password
        String username = "TestUser";
        String incorrectPassword = "WrongPassword";

        // Call authenticate method and assert that it returns false for incorrect password
        boolean result = passwordProtection.authenticate(username, incorrectPassword);
        assertFalse("Authentication passed with incorrect password", result);
    }

    // Test case for unsuccessful authentication due to non-existent user
    @Test
    public void testAuthenticateNonExistentUser() {
        // Create a new Password.PasswordProtection object
        PasswordProtection passwordProtection = new PasswordProtection();

        // Use a username that does not exist in the users.txt file
        String nonExistentUser = "NonExistentUser";
        String password = "AnyPassword";

        // Call authenticate method and assert that it returns false for a non-existent user
        boolean result = passwordProtection.authenticate(nonExistentUser, password);
        assertFalse("Authentication passed for non-existent user", result);
    }

    // Test case for ensuring the users.txt file exists and can be read
    @Test
    public void testUsersFileExists() {
        File usersFile = new File("users.txt");

        // Verify that the users.txt file exists
        assertTrue("users.txt file does not exist", usersFile.exists());
    }
}
