package server;

import message.Message;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, PrintWriter> clientWriters = new HashMap<>();
    private static Message messageHandler;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);
        messageHandler = new Message();

        System.out.println("MessageServer is running on port 4242");
        System.out.println("Waiting for client connection...");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private String userUUID;
        private String userName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                userUUID = in.readLine();
                userName = in.readLine();
                clientWriters.put(userUUID, out);

                String receiverUUID;
                String messageContent;

                while ((receiverUUID = in.readLine()) != null &&
                        (messageContent = in.readLine()) != null) {

                    messageHandler.sendMessage(userUUID, receiverUUID, userName, messageContent);

                    PrintWriter receiverOut = clientWriters.get(receiverUUID);
                    if (receiverOut != null) {
                        receiverOut.println(userName + ": " + messageContent);
                    }
                }

            } catch (IOException e) {
                System.out.println("Connection lost: " + userUUID);
            } finally {
                if (userUUID != null) {
                    clientWriters.remove(userUUID);
                }
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}