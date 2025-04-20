package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4242);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your UUID: ");
        String uuid = sc.nextLine();
        System.out.print("Enter your username: ");
        String name = sc.nextLine();

        Thread receiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected.");
                }
            }
        });
        receiverThread.start();

        while (true) {
            System.out.print("Receiver UUID: ");
            String receiverUUID = sc.nextLine();
            System.out.print("Message.Message: ");
            String message = sc.nextLine();

            String formatted = "SEND:" + uuid + ":" + receiverUUID + ":" + name + ":" + message;
            out.println(formatted);
        }
    }
}