import java.util.*;

public interface CreateNewUserInterface {

    ArrayList<String> getFriends();

    void setFriends(ArrayList<String> friends);

    ArrayList<String> getBlockedFriends();

    void setBlockedFriends(ArrayList<String> blockedFriends);

    String getEmail();

    void setEmail(String email);

    String toFileFormat();

    void saveToFile();
}
