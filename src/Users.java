import java.util.ArrayList;

/**
 * Users(Interface)
 *
 * Defines profile operations: get/set credentials, save profile, check password, and list items this user is selling.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface Users {

    /**
     * Get the stored username.
     */
    String getUsername();

    /**
     * Update the username in memory (does not alter file storage).
     * @param newUsername the new username
     */
    void setUsername(String newUsername);

    /**
     * Get the stored password.
     */
    String getPassword();

    /**
     * Update the stored password (in memory).
     * @param newPassword the new password
     */
    void setPassword(String newPassword);

    /**
     * Get the stored email address.
     */
    String getEmail();

    /**
     * Update the stored email address (in memory).
     * @param newEmail the new email address
     */
    void setEmail(String newEmail);

    /**
     * Append this user's profile to the users file.
     */
    void saveToFile();

    /**
     * Check if the provided password matches this user's password.
     * @param password the password to check
     * @return true if it matches, false otherwise
     */
    boolean checkPassword(String password);

    /**
     * Load all items sold by this user from items.txt.
     * @return a list of Item objects for which this user is the seller
     */
    ArrayList<Item> getUserItems();

}
