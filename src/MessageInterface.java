import java.util.List;

public interface MessageInterface {

    String sendMessage(String senderID, String receiverID, String message);

    String getMessageHistory(String userID1, String userID2);

    void appendNewMessage(String userID1, String userID2);

    String getSenderID();

    String getReceiverID();
}
