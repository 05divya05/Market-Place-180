package client;

import java.util.ArrayList;

/**
 * CreateNew ProfileInterface
 *
 * <p>
 *     Showcases the basic operations for the creation of new profiles and comparing for existing
 *     usernames.
 * </p>
 *
 * @version April 6th, 2025
 *
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface CreateNewProfileInterface {
    ArrayList<String> getFriends();
    void setFriends(ArrayList<String> friends);
    ArrayList <String> getBlockedFriends;
    void setBlockedFriends(ArrayList<String> blockedFriends);
    String getEmail();
    void setEmail(String email);
    String toFileFormat();
    void saveToFile;
}
