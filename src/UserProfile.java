import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SequencedCollection;

public class UserProfile implements User, Serializable {

    private String username;
    private ArrayList<String> friends;
    private ArrayList<String> blockedFriends;
    private String email;
    private String password;

    private boolean isBuyer = true;
    private boolean isSeller = true;
    private double balance = 0.0;
    private ArrayList<Double> ratings = new ArrayList<>();
    private ArrayList<String> soldItems = new ArrayList<>();
    private HashMap<String, ArrayList<String>> messages = new HashMap<>();
    private ArrayList<String> itemListings = new ArrayList<>();

    public UserProfile(String username, String email, String password) {
        this.username = username;
        this.friends = new ArrayList<>();
        this.blockedFriends = new ArrayList<>();
        this.email = email;
        this.password = password;
    }

    public UserProfile() {}

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public ArrayList<String> getFriends() {
        return this.friends;
    }

    public void setFriends(ArrayList<String> newFriends) {
        this.friends = newFriends;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean addFriend(String userToAdd) {
        if (!friends.contains(userToAdd)) {
            friends.add(userToAdd);
            return true;
        }
        return false;
    }

    public void removeFriend(String userToRemove) {
        friends.remove(userToRemove);
    }

    public void blockUser(String userToBlock) {
        blockedFriends.add(userToBlock);
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }

    public void addRating(double rating) {
        this.ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        double sum = 0;
        for (double r : ratings) sum += r;
        return sum / ratings.size();
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

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public void setBuyer(boolean isBuyer) {
        this.isBuyer = isBuyer;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean isSeller) {
        this.isSeller = isSeller;
    }

    public ArrayList<String> getSoldItems() {
        return soldItems;
    }

    public void addSoldItem(String itemId) {
        soldItems.add(itemId);
    }

    public HashMap<String, ArrayList<String>> getMessages() {
        return messages;
    }

    public void sendMessage(String toUser, String message) {
        messages.putIfAbsent(toUser, new ArrayList<>());
        messages.get(toUser).add("You: " + message);
    }

    public void receiveMessage(String fromUser, String message) {
        messages.putIfAbsent(fromUser, new ArrayList<>());
        messages.get(fromUser).add("Them: " + message);
    }
    public ArrayList<String> getBlockedFriends() {
        return blockedFriends;
    }
    public ArrayList<String> getItemListings() {
        return itemListings;
    }

    public void addItemListing(String itemId) {
        itemListings.add(itemId);
    }

    public void removeItemListing(String itemId) {
        itemListings.remove(itemId);
    }

    public ArrayList<NewsPost> getUserPosts() {
        return new ArrayList<>();
    }

    public String toFileFormat() {
        return username + "," + email + "," + password;
    }

    public void saveToFile() {
    }

}
