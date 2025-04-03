import java.time.format.DateTimeFormatter;

public class Message implements MessageInterface {

    private static final String FILE_NAME = "messages.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private String senderID;
    private String receiverID;

    @Override
    public String sendMessage(String senderID, String receiverID, String message) {

    }

    @Override
    public String getMessageHistory(String userID1, String userID2) {

    }

    @Override
    public String getSenderID() {
        return senderID;
    }

    @Override
    public String getReceiverID() {
        return receiverID;
    }


}
