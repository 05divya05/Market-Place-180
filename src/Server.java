import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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

    private record Handler(Socket sock) implements Runnable{
        @Override public void run(){
            try(sock){
                BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
                PrintWriter out=new PrintWriter(sock.getOutputStream(),true);

                String req;
                while((req=in.readLine())!=null){
                    String[] p=req.split("\\|");
                    switch(p[0]){

                        case "REGISTER" -> {
                            if(p.length!=4){ out.println("FAIL"); break; }
                            out.println(users.register(p[1],p[2],p[3])?"SUCCESS":"FAIL");
                        }
                        case "LOGIN" -> {
                            if(p.length!=4){ out.println("FAIL"); break; }
                            out.println(users.login(p[1],p[2],p[3])?"SUCCESS":"FAIL");
                        }
                        case "BALANCE" -> out.println(String.format("%.2f",users.getBalance(p[1])));

                        case "ADD_ITEM" -> {
                            if(p.length<7 || p.length>8){ out.println("FAIL"); break; }

                            String seller = p[1];
                            String title = p[2];
                            String desc = p[3];
                            double price = Double.parseDouble(p[4]);
                            String category = p[5];
                            int qty = Integer.parseInt(p[6]);
                            String imagePath = (p.length==8) ? p[7] : "";

                            if(imagePath == null || imagePath.isBlank()) imagePath = "empty";

                            ItemListing it = new ItemListing(seller, title, desc, price, category, imagePath, qty);

                            out.println(items.addItem(it) ? "SUCCESS" : "FAIL");
                        }
                        case "EDIT_ITEM" -> {
                            if(p.length!=8){ out.println("FAIL"); break; }
                            ItemListing up=new ItemListing(p[1],p[2],p[3],Double.parseDouble(p[4]),
                                    p[5],p[6],Integer.parseInt(p[7]));
                            out.println(items.editItem(p[1],p[2],up)?"SUCCESS":"FAIL");
                        }
                        case "DELETE_ITEM" -> out.println(items.deleteItem(p[1],p[2])?"SUCCESS":"FAIL");
                        case "GET_ITEMS" -> {
                            for(ItemListing it:items.getAll()) out.println(it.toFile());
                            out.println("END");
                        }


                        case "BUY_ITEM" -> {
                            int qty = Integer.parseInt(p[4]);

                            ItemListing it = items.find(p[3], p[2]);
                            if (it == null || it.getQuantity() < qty) {
                                out.println("FAIL");
                                break;
                            }

                            double cost = it.getPrice() * qty;
                            double buyerBal  = users.getBalance(p[1]);
                            if (buyerBal < cost) {
                                out.println("FAIL");
                                break;
                            }

                            users.setBalance(p[1], buyerBal - cost);
                            users.setBalance(p[3], users.getBalance(p[3]) + cost);

                            if (it.getQuantity() == qty) {
                                items.deleteItem(p[3], p[2]);
                            } else {
                                it.setQuantity(it.getQuantity() - qty);
                                items.editItem(p[3], p[2], it);
                            }

                            double newBal = buyerBal - cost;
                            out.println("SUCCESS|" + String.format("%.2f", newBal));
                        }

                        case "SEND_MESSAGE" -> {
                            chats.send(p[1],p[2],p[3]); out.println("SUCCESS");
                        }
                        case "LIST_CHATS" -> {
                            for(String k:chats.listChats(p[1])) out.println(k);
                            out.println("END");
                        }
                        case "LOAD_CHAT" -> {
                            List<String> h=chats.loadHistory(p[1],p[2]);
                            if(h.isEmpty()) out.println("No chat history.");
                            else h.forEach(out::println);
                            out.println("END");
                        }

                        case "ADD_RATING" -> {
                            if (p.length != 4) { out.println("FAIL"); break; }
                            try (PrintWriter pw = new PrintWriter(new FileWriter("ratings.txt", true))) {
                                pw.println(p[2] + "," + p[1] + "," + p[3]);  // seller,buyer,score
                            } catch (IOException e) { out.println("FAIL"); break; }
                            out.println("SUCCESS");
                        }

                        case "GET_RATING" -> {
                            if (p.length != 2) { out.println("NONE"); break; }
                            double sum = 0; int cnt = 0;
                            try (BufferedReader br = new BufferedReader(new FileReader("ratings.txt"))) {
                                String l;
                                while ((l = br.readLine()) != null) {
                                    String[] f = l.split(",", 3);
                                    if (f.length == 3 && f[0].equals(p[1])) {
                                        sum += Double.parseDouble(f[2]); cnt++;
                                    }
                                }
                            } catch (IOException ignore) {}
                            if (cnt == 0) out.println("NONE");
                            else          out.println(String.format("%.1f", sum / cnt));
                        }

                        default -> out.println("FAIL");
                    }
                }
            }catch(IOException ignore){}
        }
    }
}
