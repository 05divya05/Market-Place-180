package user;

import java.util.*;
/**
*Market Place - Team Project
*
*<p>
*
*Allows users to create a password protected profile and have the ability to login, you can search for other users, view
*their accounts and be able to add them as friends. With this ability you are also able to block or remove them.
*
*@version April 6th, 2025
*
*@author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
*
*/


public interface Users {


    String getUsername();

    void setUsername(String newUsername);

    ArrayList<String> getFriends();

    void setFriends(ArrayList<String> newFriends);

    String getEmail();

    void setEmail(String newEmail);

    String getPassword();

    void setPassword(String newPassword);

    boolean addFriend(String userToAdd);

    void removeFriend(String userToRemove);

    void blockUser(String userToBlock);

    String toFileFormat();

    void saveToFile();

    ArrayList<ItemListing> getUserPosts();

    Object getBlockedFriends();

    void deleteAccount();

    double getBalance();

}
