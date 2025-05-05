import java.io.*;
import java.util.ArrayList;

/**
 * UserProfile
 *
 * Represents a user's profile with basic credentials and
 * allows saving to file and loading items sold by the user.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class UserProfile implements Users {

    private String username;
    private String password;
    private String email;

    /**
     * Initialize a user profile in memory.
     * @param username login name
     * @param password login password
     * @param email    user email address
     */
    public UserProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    @Override
    public String getUsername() {
        // return the current username
        return username;
    }

    @Override
    public void setUsername(String newUsername) {
        // update username value
        this.username = newUsername;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String newPassword) {
        // update stored password
        this.password = newPassword;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String newEmail) {
        // update email address
        this.email = newEmail;
    }

    @Override
    public boolean checkPassword(String password) {
        // explanation: compare stored password with input
        return this.password != null && this.password.equals(password);
    }

    @Override
    public void saveToFile() {
        // append this profile to users.txt
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt", true))) {
            pw.println(username + "," + password + "," + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Item> getUserItems() {
        ArrayList<Item> items = new ArrayList<>();
        // explanation: read items.txt and pick lines where seller matches username
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue; // invalid record
                String seller = parts[4];
                if (username.equals(seller)) {
                    // create Item with at least 6 fields
                    String title = parts[0];
                    String desc  = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String cat   = parts[3];
                    String img   = parts.length > 5 ? parts[5] : "";
                    items.add(new Item(title, desc, price, cat, seller, img));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}
