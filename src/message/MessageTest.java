package message;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

public class MessageTest {

    // Test case for sending a message
    @Test
    public void testSendMessage() throws IOException {
        // Create a new Message.Message object and prepare necessary parameters
        Message message = new Message();
        String senderUUID = "user1";
        String receiverUUID = "user2";
        String senderUserName = "UserA";
        String messageContent = "Hello, how are you?";

        // Send the message
        message.sendMessage(senderUUID, receiverUUID, senderUserName, messageContent);

        // Load the chat history between the two users
        String chatHistory = message.loadChatHistory(senderUUID, receiverUUID);

        // Verify that the message appears in the chat history
        assertTrue(chatHistory.contains("UserA:"));
        assertTrue(chatHistory.contains("Hello, how are you?"));
    }

    // Test case for loading a chat history
    @Test
    public void testLoadChatHistory() throws IOException {
        // Create a new Message.Message object and send messages
        Message message = new Message();
        String senderUUID1 = "user1";
        String receiverUUID1 = "user2";
        String senderUserName1 = "UserA";
        String messageContent1 = "Hello, how are you?";

        String senderUUID2 = "user1";
        String receiverUUID2 = "user3";
        String senderUserName2 = "UserA";
        String messageContent2 = "Hi there!";

        // Send messages between different users
        message.sendMessage(senderUUID1, receiverUUID1, senderUserName1, messageContent1);
        message.sendMessage(senderUUID2, receiverUUID2, senderUserName2, messageContent2);

        // Load chat history between user1 and user2
        String chatHistory1 = message.loadChatHistory(senderUUID1, receiverUUID1);
        assertTrue(chatHistory1.contains("UserA:"));
        assertTrue(chatHistory1.contains("Hello, how are you?"));

        // Load chat history between user1 and user3
        String chatHistory2 = message.loadChatHistory(senderUUID2, receiverUUID2);
        assertTrue(chatHistory2.contains("UserA:"));
        assertTrue(chatHistory2.contains("Hi there!"));
    }

    // Test case for loading a non-existent chat history
    @Test
    public void testLoadNonExistentChatHistory() throws IOException {
        // Create a new Message.Message object
        Message message = new Message();

        // Load chat history between two users who haven't exchanged messages
        String chatHistory = message.loadChatHistory("user1", "user2");

        // Verify that the result is "No chat history."
        assertEquals("No chat history.", chatHistory);
    }

    // Test case for saving chats to a file
    @Test
    public void testSaveAllChatsToFile() throws IOException {
        // Create a new Message.Message object and send a message
        Message message = new Message();
        String senderUUID = "user1";
        String receiverUUID = "user2";
        String senderUserName = "UserA";
        String messageContent = "Hello, how are you?";

        message.sendMessage(senderUUID, receiverUUID, senderUserName, messageContent);

        // Verify that the chat is saved to the file
        File file = new File("messages.txt");
        assertTrue(file.exists());

        // Load the chat from the file to verify the content
        List<String> lines = Files.readAllLines(file.toPath());
        assertTrue(lines.contains("user1,user2"));
        assertTrue(lines.contains("UserA:"));
        assertTrue(lines.contains("Hello, how are you?"));
    }

    // Test case for loading all chats from a file
    @Test
    public void testLoadAllChatsFromFile() throws IOException {
        // Create a new Message.Message object
        Message message = new Message();

        // Send a message
        String senderUUID = "user1";
        String receiverUUID = "user2";
        String senderUserName = "UserA";
        String messageContent = "Hello, how are you?";
        message.sendMessage(senderUUID, receiverUUID, senderUserName, messageContent);

        // Load all chats from the file
        message.loadAllChatsFromFile();

        // Verify that the chat database contains the chat
        String chatHistory = message.loadChatHistory(senderUUID, receiverUUID);
        assertTrue(chatHistory.contains("UserA:"));
        assertTrue(chatHistory.contains("Hello, how are you?"));
    }

    // Test case for checking chat identifier generation
    @Test
    public void testGetWhichChat() {
        Message message = new Message();

        // Generate chat identifiers for different combinations of user UUIDs
        String chat1 = message.getWhichChat("user1", "user2");
        String chat2 = message.getWhichChat("user2", "user1");

        // Verify that both identifiers are the same regardless of the order of UUIDs
        assertEquals(chat1, chat2);
    }
}
