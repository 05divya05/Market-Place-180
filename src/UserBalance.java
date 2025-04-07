import java.util.ArrayList;

public class UserBalance implements Users {

    private String userID;
    private String username;
    private double balance;

    public UserBalance(String userID, String username) {
        this.userID = userID;
        this.username = username;
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public boolean subtractBalance(double amount) {
        if (amount > balance) return false;
        this.balance -= amount;
        return true;
    }

    @Override
    public String getUserID() {
        return userID;
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
    public ArrayList<String> getFriends() {
        return new ArrayList<>();
    }

    @Override
    public void setFriends(ArrayList<String> newFriends) {}

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public void setEmail(String newEmail) {}

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void setPassword(String newPassword) {}

    @Override
    public boolean addFriend(String userToAdd) {
        return false;
    }

    @Override
    public void removeFriend(String userToRemove) {}

    @Override
    public void blockUser(String userToBlock) {}

    @Override
    public String toFileFormat() {
        return username + "," + balance;
    }

    @Override
    public void saveToFile() {

    }

    @Override
    public ArrayList<ItemPosts> getUserPosts() {
        return null;
    }
}
