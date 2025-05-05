import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket("localhost", 1234);
             Scanner scan = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Enter username:");
            String username = scan.nextLine();

            System.out.println("Enter password:");
            String password = scan.nextLine();

            // Normally send login/auth info
            writer.println("LOGIN");
            writer.println(username);
            writer.println(password);

            System.out.println("Login complete. Welcome, " + username);

            boolean running = true;
            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Add Item");
                System.out.println("2. Search by Category");
                System.out.println("3. Delete Item");
                System.out.println("4. Send Message");
                System.out.println("5. Make Payment");
                System.out.println("6. Check Balance");
                System.out.println("7. Rate Seller");
                System.out.println("8. View Sold Items");
                System.out.println("9. Exit");
                System.out.print("Select option: ");
                String choice = scan.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter item ID: ");
                        String id = scan.nextLine();
                        System.out.print("Enter name: ");
                        String name = scan.nextLine();
                        System.out.print("Enter description: ");
                        String desc = scan.nextLine();
                        System.out.print("Enter price: ");
                        String price = scan.nextLine();
                        System.out.print("Enter category: ");
                        String category = scan.nextLine();
                        System.out.print("Enter image path: ");
                        String image = scan.nextLine();

                        writer.println("ADD_ITEM|" + id + "|" + name + "|" + desc + "|" + price + "|" + username + "|" + category + "|" + image);
                        System.out.println(reader.readLine());
                        break;

                    case "2":
                        System.out.print("Enter category to search: ");
                        String cat = scan.nextLine();
                        writer.println("SEARCH_ITEM|" + cat);
                        String line;
                        while (!(line = reader.readLine()).equals("END")) {
                            System.out.println("Item: " + line);
                        }
                        break;

                    case "3":
                        System.out.print("Enter item ID to delete: ");
                        String delId = scan.nextLine();
                        writer.println("DELETE_ITEM|" + delId);
                        System.out.println(reader.readLine());
                        break;

                    case "4":
                        System.out.print("Send to (username): ");
                        String to = scan.nextLine();
                        System.out.print("Enter message: ");
                        String msg = scan.nextLine();
                        writer.println("SEND_MESSAGE|" + username + "|" + to + "|" + msg);
                        System.out.println(reader.readLine());
                        break;

                    case "5":
                        System.out.print("Pay to (username): ");
                        String seller = scan.nextLine();
                        System.out.print("Amount: ");
                        String amt = scan.nextLine();
                        writer.println("MAKE_PAYMENT|" + username + "|" + seller + "|" + amt);
                        System.out.println(reader.readLine());
                        break;

                    case "6":
                        writer.println("GET_BALANCE|" + username);
                        System.out.println("Your balance: $" + reader.readLine());
                        break;

                    case "7":
                        System.out.print("Rate seller (username): ");
                        String rateSeller = scan.nextLine();
                        System.out.print("Enter rating (1-5): ");
                        String rating = scan.nextLine();
                        writer.println("RATE_SELLER|" + rateSeller + "|" + rating);
                        System.out.println(reader.readLine());
                        break;

                    case "8":
                        writer.println("VIEW_SOLD_ITEMS|" + username);
                        System.out.println("Sold Items:");
                        while (!(line = reader.readLine()).equals("END")) {
                            System.out.println(line);
                        }
                        break;

                    case "9":
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }
}
