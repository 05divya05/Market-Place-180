import java.io.IOException;
import java.util.List;

/**
 * MessageInterface
 *
 * Defines methods for sending messages and retrieving chat history from a file.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface MessageInterface {
    /**
     * Sends a message from <code>from</code> to <code>to</code> with the given text.
     * @param from    sender username
     * @param to      recipient username
     * @param content message text
     * @throws IOException on file I/O errors
     */
    void send(String from, String to, String content) throws IOException;

    /**
     * Loads all messages exchanged between <code>user1</code> and <code>user2</code>.
     * @param user1 first username
     * @param user2 second username
     * @return a list of message lines in chronological order
     * @throws IOException on file I/O errors
     */
    List<String> loadHistory(String user1, String user2) throws IOException;

    /**
     * Lists all conversation keys (e.g. "userA,userB") that involve <code>user</code>.
     * @param user the username to search for
     * @return a list of chat keys in the order they appear in the file
     * @throws IOException on file I/O errors
     */
    List<String> listChats(String user) throws IOException;
}
