import java.util.ArrayList;
import java.util.*;


public class UserProfile implements Users ,Serializable {
    private String username;
    private ArrayList<String> friends;
    private ArrayList<String> blockedFriends;
    private String email;
    private String password;
    private final String userID;

    public UserProfile(String username, String email, String password) {

        this.username = username;
        this.friends = new ArrayList<String>();
        this.blockedFriends = new ArrayList<String>();
        this.email = email;
        this.password = password;
        this.userID = UUID.randomUUID().toString();

    }
    
    @Override
    public String getUserID() {return userID;}

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public void setUsername(String newUsername) {

    }

    @Override
    public ArrayList<String> getFriends() {

        ArrayList<String> friendsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(this.username)) {
                    if (userDetails[3].equals("EmptyFriendsList")) {
                        break;
                    } else if (!(userDetails[3].equals("EmptyFriendsList")) && !(userDetails[3].contains(";"))) {
                        friendsList.add(userDetails[3]);
                    } else {
                        String[] friendsListArray = userDetails[3].split(";");
                        for (String friend : friendsListArray) {
                            friendsList.add(friend);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (friendsList.isEmpty()) {
            friendsList.add("EmptyFriendsList");
        }
        return friendsList;
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
