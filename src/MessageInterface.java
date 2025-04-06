import java.io.IOException;
import java.util.List;

/**
 * The MessageInterface provides methods for sending messages,
 * saving and loading chat histories, and retrieving conversations
 * between two users.
 *
 *
 * @author Yihang Li
 * @version 4/5/2025
 */

public interface MessageInterface {

    /**
     * Sends a message between two users and saves it to the chat file.
     *
     * @param senderUUID The UUID of the message sender.
     * @param receiverUUID The UUID of the message receiver.
     * @param senderUserName The name of the sender.
     * @param message The message content to be sent.
     * @throws IOException If an IO error occurs while saving the message.
     */
    void sendMessage(String senderUUID, String receiverUUID, String senderUserName, String message) throws IOException;

    /**
     * Loads the chat history between two users.
     *
     * @param user1UUID The UUID of the first user.
     * @param user2UUID The UUID of the second user.
     * @return A string containing the formatted chat history.
     */
    String loadChatHistory(String user1UUID, String user2UUID);

    /**
     * Loads all chat data from the text file into memory.
     *
     * @throws IOException If an IO error occurs while loading the chat file.
     */
    void loadAllChatsFromFile() throws IOException;

    /**
     * Saves all current chat data from memory to the text file.
     *
     * @throws IOException If an IO error occurs while writing to the chat file.
     */
    void saveAllChatsToFile() throws IOException;

    /**
     * Generates a search key for a conversation between two users based on their UUIDs.
     *
     * @param user1UUID The UUID of the first user.
     * @param user2UUID The UUID of the second user.
     * @return A string representing the conversation key in the format "UUID1,UUID2".
     */
    String getWhichChat(String user1UUID, String user2UUID);
}
