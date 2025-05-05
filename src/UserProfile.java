import java.io.*;
import java.util.ArrayList;

public class UserProfile implements Users {

    private String username;
    private String password;
    private String email;

    public UserProfile(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt", true))) {
            pw.println(username + "," + password + "," + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Item> getUserItems() {
        ArrayList<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[4].equals(this.username)) {  // parts[4] 是 seller
                    Item item = new Item(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3], parts[4], parts[5]);
                    items.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}
