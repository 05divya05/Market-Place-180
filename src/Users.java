import java.util.ArrayList;

public interface Users {

    String getUsername();
    void setUsername(String newUsername);

    String getPassword();
    void setPassword(String newPassword);

    String getEmail();
    void setEmail(String newEmail);

    void saveToFile();

    boolean checkPassword(String password);

    ArrayList<Item> getUserItems(); // 获取用户发布的商品

}
