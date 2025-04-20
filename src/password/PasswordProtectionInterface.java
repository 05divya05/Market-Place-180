package password;

/**
*This interface just defines what the user needs to input to be able to login into their account
*
*
* @version April 6th 2025
* @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
*/

public interface PasswordProtectionInterface {
    boolean authenticate(String username, String password);
}
