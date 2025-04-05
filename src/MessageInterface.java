package src;

import java.io.IOException;
import java.util.List;

public interface MessageInterface {

    void sendMessage(String senderUUID, String receiverUUID, String senderUserName, String message) throws IOException;

    String loadChatHistory(String user1UUID, String user2UUID);

    void loadAllChatsFromFile() throws IOException;

    void saveAllChatsToFile() throws IOException;

    String getWhichChat(String user1UUID, String user2UUID);
}
