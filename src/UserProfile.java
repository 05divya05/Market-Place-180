import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Users ,Serializable {
    private String username;
    private ArrayList<String> friends;
    private ArrayList<String> blockedFriends;
    private String email;
    private String password;

    public UserProfile(String username, String email, String password) {

        this.username = username;
        this.friends = new ArrayList<String>();
        this.blockedFriends = new ArrayList<String>();
        this.email = email;
        this.password = password;

    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public void setUsername(String newUsername) {

    }

    @Override
    public ArrayList<String> getFriends() {
        return null;
    }

    @Override
    public void setFriends(ArrayList<String> newFriends) {

    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public void setEmail(String newEmail) {

    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void setPassword(String newPassword) {

    }

    @Override
    public boolean addFriend(String userToAdd) {
        return false;
    }

    @Override
    public void removeFriend(String userToRemove) {

    }

    @Override
    public void blockUser(String userToBlock) {

    }

    @Override
    public String toFileFormat() {
        return "";
    }

    @Override
    public void saveToFile() {

    }

    @Override
    public ArrayList<ItemPosts> getUserPosts() {
        return null;
    }
}
