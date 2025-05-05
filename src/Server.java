import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Server
 *
 * Listens on port 4242 and handles all client requests in separate threads.
 * Supports user registration/login, item CRUD, purchasing, messaging, and ratings.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class Server {
    private static final int PORT=4242;
    private static final UserManager    users = new UserManager();
    private static final ItemManager    items;
    private static final Message chats = new Message();
    static{ ItemManager tmp=null; try{tmp=new ItemManager();}catch(IOException ignore){} items=tmp; }

    public static void main(String[]a){
        System.out.println("Server on "+PORT);
        try(ServerSocket ss=new ServerSocket(PORT)){
            while(true) new Thread(new Handler(ss.accept())).start();
        }catch(IOException e){e.printStackTrace();}
    }

    /**
     * Handler runs per client connection. Reads commands and writes responses.
     */
    private static class Handler implements Runnable {
        private final Socket sock;
        public Handler(Socket sock) { this.sock = sock; }

        @Override
        public void run() {
            try (
                    // set up input and output streams
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    PrintWriter out = new PrintWriter(sock.getOutputStream(), true)
            ) {
                String req;
                while ((req = in.readLine()) != null) {
                    String[] p = req.split("\\|");
                    switch (p[0]) {

                        case "REGISTER" -> {
                            // register a new user: username, email, password
                            if (p.length != 4) {
                                out.println("FAIL");
                                break;
                            }
                            boolean ok = users.register(p[1], p[2], p[3]);
                            out.println(ok ? "SUCCESS" : "FAIL");
                        }

                        case "LOGIN" -> {
                            // login existing user
                            if (p.length != 4) {
                                out.println("FAIL");
                                break;
                            }
                            boolean ok = users.login(p[1], p[2], p[3]);
                            out.println(ok ? "SUCCESS" : "FAIL");
                        }

                        case "BALANCE" ->
                            // send current balance
                                out.println(String.format("%.2f", users.getBalance(p[1])));

                        case "ADD_ITEM" -> {
                            // add new item: seller, title, desc, price, category, qty [,image]
                            if (p.length < 7 || p.length > 8) {
                                out.println("FAIL");
                                break;
                            }
                            String seller = p[1];
                            String title = p[2];
                            String desc = p[3];
                            double price = Double.parseDouble(p[4]);
                            String category = p[5];
                            int qty = Integer.parseInt(p[6]);
                            // image path is optional
                            String image = (p.length == 8) ? p[7] : "";
                            if (image.isBlank()) image = "empty";
                            ItemListing it = new ItemListing(seller, title, desc, price, category, image, qty);
                            boolean added = items.addItem(it);
                            out.println(added ? "SUCCESS" : "FAIL");
                        }

                        case "EDIT_ITEM" -> {
                            // edit existing item: seller, title + new full item data
                            if (p.length != 8) { out.println("FAIL"); break; }
                            ItemListing up = new ItemListing(p[1], p[2], p[3],
                                    Double.parseDouble(p[4]), p[5], p[6], Integer.parseInt(p[7]));
                            boolean edited = items.editItem(p[1], p[2], up);
                            out.println(edited ? "SUCCESS" : "FAIL");
                        }

                        case "DELETE_ITEM" ->
                            // remove item by seller + title
                                out.println(items.deleteItem(p[1], p[2]) ? "SUCCESS" : "FAIL");

                        case "GET_ITEMS" -> {
                            // list all items, then END marker
                            for (ItemListing it : items.getAll()) out.println(it.toFile());
                            out.println("END");
                        }

                        case "BUY_ITEM" -> {
                            // purchase logic: buyer, title, seller, quantity
                            int want = Integer.parseInt(p[4]);
                            ItemListing it = items.find(p[3], p[2]);
                            // // explanation: check availability and funds
                            if (it == null || it.getQuantity() < want) {
                                out.println("FAIL"); break;
                            }
                            double cost = it.getPrice() * want;
                            double buyerBal = users.getBalance(p[1]);
                            if (buyerBal < cost) { out.println("FAIL"); break; }
                            // update balances
                            users.setBalance(p[1], buyerBal - cost);
                            users.setBalance(p[3], users.getBalance(p[3]) + cost);
                            // update or remove item
                            if (it.getQuantity() == want) items.deleteItem(p[3], p[2]);
                            else {
                                it.setQuantity(it.getQuantity() - want);
                                items.editItem(p[3], p[2], it);
                            }
                            double newBal = buyerBal - cost;
                            out.println("SUCCESS|" + String.format("%.2f", newBal));
                        }

                        case "SEND_MESSAGE" -> {
                            // write chat message to file
                            chats.send(p[1], p[2], p[3]);
                            out.println("SUCCESS");
                        }

                        case "LIST_CHATS" -> {
                            // send all chat keys for this user, then END
                            for (String k : chats.listChats(p[1])) out.println(k);
                            out.println("END");
                        }

                        case "LOAD_CHAT" -> {
                            // send history between two users, then END
                            List<String> hist = chats.loadHistory(p[1], p[2]);
                            if (hist.isEmpty()) out.println("No chat history.");
                            else hist.forEach(out::println);
                            out.println("END");
                        }

                        case "ADD_RATING" -> {
                            // append rating record: seller,buyer,score
                            if (p.length != 4) { out.println("FAIL"); break; }
                            try (PrintWriter pw = new PrintWriter(new FileWriter("ratings.txt", true))) {
                                pw.println(p[2] + "," + p[1] + "," + p[3]);
                                out.println("SUCCESS");
                            } catch (IOException e) {
                                out.println("FAIL");
                            }
                        }

                        case "GET_RATING" -> {
                            // compute average rating for seller
                            if (p.length != 2) { out.println("NONE"); break; }
                            double sum = 0;
                            int count = 0;
                            try (BufferedReader br = new BufferedReader(new FileReader("ratings.txt"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                    String[] flds = line.split(",", 3);
                                    if (flds.length == 3 && flds[0].equals(p[1])) {
                                        sum += Double.parseDouble(flds[2]);
                                        count++;
                                    }
                                }
                            } catch (IOException ignore) { }
                            if (count == 0) out.println("NONE");
                            else out.println(String.format("%.1f", sum / count));
                        }

                        default -> out.println("FAIL"); // unrecognized command
                    }
                }
            } catch (IOException ignore) {
                // client disconnected or error occurred
            } finally {
                try { sock.close(); } catch (IOException ignored) { }
            }
        }
    }
}
