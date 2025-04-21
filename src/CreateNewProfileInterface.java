import java.util.ArrayList;

public interface CreateNewProfileInterface {
    ArrayList<String> getFriends();
    void setFriends(ArrayList<String> friends);
    ArrayList <String> getBlockedFriends();
    void setBlockedFriends(ArrayList<String> blockedFriends);
    String getEmail();
    void setEmail(String email);
    String toFileFormat();
    void saveToFile();
}
