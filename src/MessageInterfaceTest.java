import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class MessageInterfaceTest {

    private static final String TEST_FILE_NAME = "TestDirectMessages.txt";

    private Message createMessageInstance() throws IOException {
        // Delete the test file if it already exists
        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists()) {
            testFile.delete();
        }
        return new Message();
    }

    @Test
    public void testSendMessage() throws Exception {
        Message message = createMessageInstance();

        message.sendMessage("UUID1", "UUID2", "Alice", "Good Morning, how r u");
        message.sendMessage("UUID2", "UUID1", "Bob", "Good, thank u");

        String chatHistory = message.loadChatHistory("UUID1", "UUID2");

        assertTrue(chatHistory.contains("Alice:"));
        assertTrue(chatHistory.contains("Good Morning, how r u"));
        assertTrue(chatHistory.contains("Bob:"));
        assertTrue(chatHistory.contains("Good, thank u"));
    }

    @Test
    public void testLoadChatHistory_NoHistory() throws Exception {
        Message message = createMessageInstance();

        String chatHistory = message.loadChatHistory("UUID3", "UUID4");
        assertEquals("No chat history.", chatHistory);
    }

    @Test
    public void testSaveAndLoadChats() throws Exception {
        Message message = createMessageInstance();

        message.sendMessage("UUID1", "UUID2", "Alice", "Hello!");
        message.saveAllChatsToFile();

        Message newMessage = createMessageInstance();
        String chatHistory = newMessage.loadChatHistory("UUID1", "UUID2");

        assertTrue(chatHistory.contains("Hello!"));
    }

    @Test
    public void testGetWhichChat() throws Exception {
        Message message = createMessageInstance();

        String chatKey = message.getWhichChat("UUID1", "UUID2");
        assertEquals("UUID1,UUID2", chatKey);

        String chatKeyReversed = message.getWhichChat("UUID2", "UUID1");
        assertEquals("UUID1,UUID2", chatKeyReversed);
    }

    @Test
    public void testMultipleConversations() throws Exception {
        Message message = createMessageInstance();

        message.sendMessage("UUID1", "UUID2", "Alice", "Hello Bob!");
        message.sendMessage("UUID3", "UUID4", "Charlie", "Hi Dave!");

        String chatHistory1 = message.loadChatHistory("UUID1", "UUID2");
        String chatHistory2 = message.loadChatHistory("UUID3", "UUID4");

        assertTrue(chatHistory1.contains("Hello Bob!"));
        assertTrue(chatHistory2.contains("Hi Dave!"));
    }
}