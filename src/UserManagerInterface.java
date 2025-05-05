public interface UserManagerInterface {
    boolean register(String username, String email, String password);
    boolean login(String username, String email, String password);
    boolean deleteAccount(String username, String email, String password);
    double  getBalance(String username);
    boolean setBalance(String username, double newBal);
    boolean userExists(String username);
    boolean emailExists(String email);
}
