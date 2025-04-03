import java.util.List;

public interface MessagingInterface {

    String sendMessage(String senderID, String receiverID, String message);

    boolean markAsRead(String messageID);

    boolean isMessageRead(String messageID);

    String getMessage(String messageID);

    List<Message> getMessageHistory(String userID1, String userID2);

    void saveMessageToFile (String messageID);
}
