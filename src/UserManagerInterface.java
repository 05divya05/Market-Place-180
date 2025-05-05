/**
 * UserManagerInterface
 *
 * Defines user account operations backed by users.txt.
 * Supports registration, login, deletion, and balance queries/updates.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface UserManagerInterface {
    /**
     * Register a new user. Returns true if successful.
     * @param username chosen username (must be non-blank)
     * @param email    user email (must match standard pattern)
     * @param password password string (min length 6)
     */
    boolean register(String username, String email, String password);

    /**
     * Authenticate a user by matching username, email, and password.
     * @return true if credentials match an existing record
     */
    boolean login(String username, String email, String password);

    /**
     * Delete an account if credentials match. Returns true if deleted.
     */
    boolean deleteAccount(String username, String email, String password);

    /**
     * Get the balance for the given username, or 0 if not found.
     */
    double getBalance(String username);

    /**
     * Update the balance for the given user. Returns true if successful.
     */
    boolean setBalance(String username, double newBal);

    /**
     * Check if a username is already taken.
     */
    boolean userExists(String username);

    /**
     * Check if an email is already registered.
     */
    boolean emailExists(String email);
}