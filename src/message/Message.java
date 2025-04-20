package message;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The Message.Message class provides functionality for sending messages, saving conversations to a file,
 * and loading previous chat histories between any two users.
 *
 * Example chat history format:
 *
 * UUID1,UUID2
 * UserA:
 * 2025-04-05 10:00:00
 * Hi
 *
 * UserB:
 * 2025-04-05 10:02:00
 * Hello
 *
 * UUID3,UUID4
 * UserC:
 * 2025-04-05 10:05:00
 * Good morning
 *
 * UserD:
 * 2025-04-05 10:07:00
 * Morning
 *
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 * @version 4/5/2025
 */
public class Message implements MessageInterface {

    private static final String FILE_NAME = "messages.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Map<String, List<String>> chatDatabase = new HashMap<>();

    public Message() throws IOException {
        loadAllChatsFromFile();
    }

    /**
     * Loads all chat messages from the file into the chatDatabase.
     * Each conversation is identified by participants' UUIDs.
     *
     * @throws IOException if an IO error occurs while reading the file.
     */
    @Override
    public void loadAllChatsFromFile() throws IOException {
        if (!Files.exists(Paths.get(FILE_NAME))) return;

        List<String> messageHistory = Files.readAllLines(Paths.get(FILE_NAME));
        String targetChatID = null;
        List<String> currentMessages = new ArrayList<>();

        for (String line : messageHistory) {
            if (line.matches(".+,.+")) {
                if (targetChatID != null) {
                    chatDatabase.put(targetChatID, new ArrayList<>(currentMessages));
                }
                targetChatID = line;
                currentMessages.clear();
            } else if (!line.isBlank()) {
                currentMessages.add(line);
            }
        }

        if (targetChatID != null) {
            chatDatabase.put(targetChatID, new ArrayList<>(currentMessages));
        }
    }

    /**
     * Saves all chat messages to the file.
     *
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    @Override
    public void saveAllChatsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, List<String>> entry : chatDatabase.entrySet()) {
                writer.write(entry.getKey());
                writer.newLine();
                for (String message : entry.getValue()) {
                    writer.write(message);
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }


    /**
     * Sends a message from one user to another.
     * The message is saved to the chatDatabase(messages.txt).
     *
     * @param senderUUID The UUID of the message sender.
     * @param receiverUUID The UUID of the message receiver.
     * @param senderUserName The name of the sender, to be displayed with the message.
     * @param message The message content to be sent.
     * @throws IOException if an I/O error occurs while saving the message.
     */
    @Override
    public void sendMessage(String senderUUID, String receiverUUID, String senderUserName, String message) throws IOException {
        String targetChat = getWhichChat(senderUUID, receiverUUID);
        String timestamp = LocalDateTime.now().format(formatter);
        String formattedMessage = String.format("%s:\n%s\n%s", senderUserName, timestamp, message);

        chatDatabase.putIfAbsent(targetChat, new ArrayList<>());
        chatDatabase.get(targetChat).add(formattedMessage);

        saveAllChatsToFile();
    }


    /**
     * Loads the chat history between two users.
     * Returns a formatted string of the conversation or a message if no chat history exists.
     *
     * @param user1UUID The UUID of the first user.
     * @param user2UUID The UUID of the second user.
     * @return A string containing the formatted chat history.
     */
    @Override
    public String loadChatHistory(String user1UUID, String user2UUID) {
        String whichChat = getWhichChat(user1UUID, user2UUID);
        List<String> messages = chatDatabase.get(whichChat);

        if (messages == null || messages.isEmpty()) {
            return "No chat history.";
        }

        StringBuilder result = new StringBuilder(whichChat + "\n");
        for (String message : messages) {
            result.append(message).append("\n");
        }
        return result.toString();
    }

    /**
     * Generates a unique chat identifier for two users by sorting their UUIDs.
     * This ensures that the order of UUIDs does not affect the identifier.
     *
     * @param user1UUID The UUID of the first user.
     * @param user2UUID The UUID of the second user.
     * @return A sorted string of the two UUIDs.
     */
    @Override
    public String getWhichChat(String user1UUID, String user2UUID) {
        List<String> participants = Arrays.asList(user1UUID, user2UUID);
        Collections.sort(participants);
        return participants.get(0) + "," + participants.get(1);
    }
}
