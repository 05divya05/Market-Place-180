package password;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
*this class allows users to create accounts with password protection login which means they can securly enter their email and other username and password details to be able to enter the market place and their account.
*
* @version April 6th 2025
*
* @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
*/
public class PasswordProtection implements PasswordProtectionInterface {
    private static final String FILENAME = "users.txt";  //file name of users.txt
    private static ArrayList<String> users = new ArrayList<>();  //list of users
    private static ArrayList<String> passes = new ArrayList<>();  //lis of passes

    public PasswordProtection() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;  //temp line of each line in users.txt
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(parts[0]);
                passes.add(parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading users from file: " + e.getMessage());
        }
    }


    public static ArrayList<String> getUsers() {
        return PasswordProtection.users;
    }


    public static ArrayList<String> getPasses() {
        return PasswordProtection.passes;
    }


    public boolean authenticate(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(username) && passes.get(i).equals(password)) {
                return true;
            }
        }
        return false;
    }
}