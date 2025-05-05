import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends PasswordProtectedLogin implements Runnable {

    private Socket clientSocket;
    private static final String ITEM_FILE = "items.txt";
    private static ArrayList<Item> itemList = new ArrayList<>();
    private static HashMap<String, UserProfile> users = new HashMap<>();

    public Server(Socket socket) {
        this.clientSocket = socket;
    }

    public static void main(String[] args) {
        loadItems();
        loadUsers();

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                Thread clientThread = new Thread(new Server(socket));
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadItems() {
        try (BufferedReader br = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                itemList.add(Item.fromFileFormat(line));
            }
        } catch (IOException e) {
            System.out.println("Item file not found. Starting with empty list.");
        }
    }

    public static void saveItems() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ITEM_FILE))) {
            for (Item item : itemList) {
                pw.println(item.toFileFormat());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (HashMap<String, UserProfile>) ois.readObject();
        } catch (Exception e) {
            System.out.println("User file not found. Starting fresh.");
        }
    }

    public static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String command;
            while ((command = reader.readLine()) != null) {
                String[] tokens = command.split("\\|");
                String action = tokens[0];

                switch (action) {
                    case "ADD_ITEM":
                        // tokens: ADD_ITEM|id|name|desc|price|seller|category|imagePath
                        Item newItem = new Item(tokens[1], tokens[2], tokens[3],
                                Double.parseDouble(tokens[4]), tokens[5], tokens[6], tokens[7]);
                        itemList.add(newItem);
                        users.get(tokens[5]).addItemListing(tokens[1]);
                        saveItems();
                        writer.println("Item added");
                        break;

                    case "DELETE_ITEM":
                        itemList.removeIf(item -> item.getId().equals(tokens[1]));
                        writer.println("Item deleted");
                        break;

                    case "SEARCH_ITEM":
                        String category = tokens[1];
                        for (Item item : itemList) {
                            if (!item.isSold() && item.getCategory().equalsIgnoreCase(category)) {
                                writer.println(item.toFileFormat());
                            }
                        }
                        writer.println("END");
                        break;

                    case "SEND_MESSAGE":
                        // tokens: SEND_MESSAGE|from|to|message
                        users.get(tokens[2]).receiveMessage(tokens[1], tokens[3]);
                        saveUsers();
                        writer.println("Message sent");
                        break;

                    case "MAKE_PAYMENT":
                        // tokens: MAKE_PAYMENT|buyer|seller|amount
                        UserProfile buyer = users.get(tokens[1]);
                        UserProfile seller = users.get(tokens[2]);
                        double amount = Double.parseDouble(tokens[3]);
                        if (buyer.getBalance() >= amount) {
                            buyer.deductBalance(amount);
                            seller.addBalance(amount);
                            writer.println("Payment successful");
                            saveUsers();
                        } else {
                            writer.println("Insufficient balance");
                        }
                        break;

                    case "GET_BALANCE":
                        writer.println(users.get(tokens[1]).getBalance());
                        break;

                    case "RATE_SELLER":
                        // tokens: RATE_SELLER|seller|rating
                        users.get(tokens[1]).addRating(Double.parseDouble(tokens[2]));
                        saveUsers();
                        writer.println("Rating added");
                        break;

                    case "VIEW_SOLD_ITEMS":
                        for (String soldId : users.get(tokens[1]).getSoldItems()) {
                            writer.println(soldId);
                        }
                        writer.println("END");
                        break;

                    default:
                        writer.println("Unknown command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
