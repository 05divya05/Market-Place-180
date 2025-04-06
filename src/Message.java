import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Message implements MessageInterface {

    private static final String FILE_NAME = "DirectMessages.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Map<String, List<String>> chatDatabase = new HashMap<>();

    public Message() throws IOException {
        loadAllChatsFromFile();
    }

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


    @Override
    public void sendMessage(String senderUUID, String receiverUUID, String senderUserName, String message) throws IOException {
        String targetChat = getWhichChat(senderUUID, receiverUUID);
        String timestamp = LocalDateTime.now().format(formatter);
        String formattedMessage = String.format("%s:\n%s\n%s", senderUserName, timestamp, message);

        chatDatabase.putIfAbsent(targetChat, new ArrayList<>());
        chatDatabase.get(targetChat).add(formattedMessage);

        saveAllChatsToFile();
    }


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

    @Override
    public String getWhichChat(String user1UUID, String user2UUID) {
        List<String> participants = Arrays.asList(user1UUID, user2UUID);
        Collections.sort(participants);
        return participants.get(0) + "," + participants.get(1);
    }
}
