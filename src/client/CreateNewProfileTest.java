package client;

import org.junit.Test;
import user.UserProfile;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class CreateNewProfileTest {

    // Test case for constructor with valid input
    @Test
    public void testConstructorValidInput() {
        // Set the input for a valid profile
        String username = "testUser";
        String email = "testUser@example.com";
        String password = "securePassword";

        // Create new profile
        CreateNewProfile profile = new CreateNewProfile(username, email, password);

        // Verify the profile is created and not already registered
        assertFalse(profile.isAlreadyRegistered());
    }

    // Test case for constructor when user already exists
    @Test
    public void testConstructorUserAlreadyExists() {
        // Set the input for an already registered user
        String username = "existingUser";
        String email = "existingUser@example.com";
        String password = "securePassword";

        // Simulate the user already existing by loading the users
        CreateNewProfile.loadUsersFromFile();

        // Create new profile
        CreateNewProfile profile = new CreateNewProfile(username, email, password);

        // Verify the profile creation fails due to registration
        assertTrue(profile.isAlreadyRegistered());
    }

    // Test case for loading users from file
    @Test
    public void testLoadUsersFromFile() {
        // Assuming the file "users.txt" exists with test data, e.g., "user1,email1,pass1"
        CreateNewProfile.loadUsersFromFile();

        // Verify that users are loaded correctly
        ArrayList<UserProfile> profiles = CreateNewProfile.getUserProfiles();
        assertNotNull(profiles);
        assertFalse(profiles.isEmpty());
        assertEquals("user1", profiles.get(0).getUsername());
    }

    // Test case for getUser() when no user exists
    @Test
    public void testGetUserNoUserExists() {
        // Create a profile without any user data loaded
        CreateNewProfile profile = new CreateNewProfile();

        // Attempt to get the last registered user
        UserProfile user = profile.getUser();

        // Assert that no user is returned when no profiles exist
        assertNull(user);
    }

    // Test case for saving user data to file
    @Test
    public void testSaveToFile() {
        // Set input for a valid profile
        String username = "testUser";
        String email = "testUser@example.com";
        String password = "securePassword";

        // Create new profile and save to file
        CreateNewProfile profile = new CreateNewProfile(username, email, password);
        profile.saveToFile(); // Assuming saveToFile exists and works as expected

        // Verify that file exists and data is written correctly
