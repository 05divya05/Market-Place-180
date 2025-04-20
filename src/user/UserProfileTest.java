package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    private UserProfile userProfile;

    // This will run before each test
    @BeforeEach
    public void setUp() {
        // Create a new user.UserProfile object with test data
        userProfile = new UserProfile("testUser", "testuser@example.com", "testPassword123");
    }

    @Test
    public void testGetUsername() {
        // Test the getUsername() method
        assertEquals("testUser", userProfile.getUsername(), "Username should be 'testUser'");
    }

    @Test
    public void testSetUsername() {
        // Test the setUsername() method
        userProfile.setUsername("newUsername");
        assertEquals("newUsername", userProfile.getUsername(), "Username should be 'newUsername'");
    }

    @Test
    public void testAddFriend() {
        // Test adding a friend
        assertTrue(userProfile.addFriend("friend1"), "Friend 'friend1' should be added successfully");
        assertTrue(userProfile.getFriendsList().contains("friend1"), "Friends list should contain 'friend1'");
    }

    @Test
    public void testRemoveFriend() {
        // Test removing a friend
        userProfile.addFriend("friend1");
        userProfile.removeFriend("friend1");
        assertFalse(userProfile.getFriendsList().contains("friend1"), "Friends list should not contain 'friend1'");
    }

    @Test
    public void testBlockUser() {
        // Test blocking a user
        userProfile.addFriend("friend2");
        userProfile.blockUser("friend2");
        assertTrue(userProfile.getBlockedFriends().contains("friend2"), "Blocked friends list should contain 'friend2'");
        assertFalse(userProfile.getFriends
