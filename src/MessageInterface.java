import java.io.IOException;
import java.util.List;

public interface MessageInterface {
    void send(String from,String to,String content) throws IOException;
    List<String> loadHistory(String user1,String user2) throws IOException;
    List<String> listChats(String user) throws IOException;
}
