import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class Server extends PasswordProtection implements Runnable, ServerInterface {

    private Socket clientSocket;

    public Server(Socket socket) {
        this.clientSocket = socket;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening to port 1234...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server: Client connected");

                Thread clientThread = new Thread(new Server(socket));
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println(new File("users.txt").getAbsolutePath());

        String username = "";
        String email = "";
        String password = "";
        boolean loginComplete = false;
        boolean registrationComplete = false;

        try (BufferedReader read = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter write = new PrintWriter(clientSocket.getOutputStream(), true);
             ObjectOutputStream objectWrite = new ObjectOutputStream(clientSocket.getOutputStream());
             BufferedReader readPost = new BufferedReader(new FileReader("newsPosts.txt"))) {

            System.out.println("Server: Client connected");
            UserProfile currentUser = null;

            do {
                String loginOrRegister = read.readLine();

                if (loginOrRegister.equals("1")) {
                    System.out.println("User selected login");
                    username = read.readLine();
                    System.out.println("Received username: " + username);

                    password = read.readLine();
                    System.out.println("Received password: " + password);

                    loginComplete = authenticate(username, password);
                    objectWrite.writeBoolean(loginComplete);
                    objectWrite.flush();

                } else if (loginOrRegister.equalsIgnoreCase("2")) {
                    System.out.println("User selected register");

                    username = read.readLine();
                    System.out.println("Received username");

                    email = read.readLine();
                    System.out.println("Received email");

                    password = read.readLine();
                    System.out.println("Received password");

                    CreateNewProfile tempUser = new CreateNewProfile(username, email, password);

                    if (!tempUser.isAlreadyRegistered()) {
                        System.out.println("User: " + username + " created and saved to file");
                        registrationComplete = true;
                        objectWrite.writeBoolean(registrationComplete);
                        objectWrite.flush();
                    } else {
                        System.out.println("User already exists, please login");
                        objectWrite.writeBoolean(registrationComplete);
                        objectWrite.flush();
                    }
                }
            } while (!loginComplete && !registrationComplete);

            currentUser = SellerSearch.findUserByUsername(username);

            while (loginComplete || registrationComplete) {
                System.out.println("Looking for input");
                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    case "1" -> {
                        System.out.println("user selected search");
                        prompt = read.readLine();

                        if (prompt.equals("exit")) {
                            System.out.println("prompt invalid");
                            break;
                        }

                        System.out.println("Searching for user: " + prompt);

                        UserProfile searchedUser = SellerSearch.findUserByUsername(prompt);

                        if (searchedUser == null) {
                            objectWrite.writeObject("User not found");
                            objectWrite.flush();
                        } else {
                            objectWrite.writeObject(searchedUser);
                            objectWrite.flush();
                            // Comment functionality removed
                        }

                        objectWrite.flush();
                    }

                    case "2" -> {
                        System.out.println("User selected content");
                        prompt = read.readLine();

                        if (prompt.equals("1")) {
                            System.out.println("user trying to create post");
                            synchronized (this) {
                                String title = read.readLine();
                                String imagePath = read.readLine();
                                String date = String.valueOf(LocalDate.now());
                                new ItemListing(); // Assuming actual post logic happens in constructor
                                System.out.println("tried to create new post");
                            }

                        } else if (prompt.equals("2")) {
                            System.out.println("user trying to delete post");
                            synchronized (this) {
                                String title = read.readLine();
                                ItemListing.deletePost(title);
                                System.out.println("tried to delete post: " + title);
                            }

                        } else if (prompt.equals("exit")) {
                            System.out.println("user exited");
                        }
                    }

                    case "3" -> {
                        System.out.println("User selected friend actions");
                        prompt = read.readLine();

                        switch (prompt) {
                            case "1" -> {
                                String friendToAdd = read.readLine();
                                System.out.println("current friends list: " + currentUser.getFriends());
                                currentUser.addFriend(friendToAdd);
                                currentUser.updateFriendsList();
                                write.println("Added friend: " + friendToAdd);
                                write.flush();
                                System.out.println("new friends list: " + currentUser.getFriends());
                            }
                            case "2" -> {
                                String userToBlock = read.readLine();
                                write.println("Blocked: " + userToBlock);
                                write.flush();
                                currentUser.blockUser(userToBlock);
                                currentUser.updateBlockedList();
                                currentUser.updateFriendsList();
                            }
                            case "3" -> {
                                String friendToRemove = read.readLine();
                                write.println("Removed friend: " + friendToRemove);
                                write.flush();
                                currentUser.removeFriend(friendToRemove);
                                currentUser.updateFriendsList();
                            }
                            case "4" -> {
                                String userToUnblock = read.readLine();
                                write.println("Unblocked: " + userToUnblock);
                                write.flush();
                                currentUser.removeBlockedUser(userToUnblock);
                                currentUser.updateBlockedList();
                            }
                            case "exit" -> {
                                System.out.println("user changed pages");
                            }
                            default -> write.println("A valid input was not selected!");
                        }
                    }

                    case "4" -> {
                        System.out.println("User selected account");
                        prompt = read.readLine();

                        if (prompt.equals("exit")) {
                            System.out.println("user exited");
                        } else {
                            switch (prompt) {
                                case "1" -> {
                                    System.out.println("viewing posts");
                                    synchronized (this) {
                                        if (!currentUser.getUserPosts().isEmpty()) {
                                            objectWrite.writeObject(currentUser.getUserPosts());
                                            objectWrite.flush();
                                        } else {
                                            objectWrite.writeObject(new ArrayList<>());
                                            objectWrite.flush();
                                        }
                                    }
                                }
                                case "2" -> {
                                    write.println(currentUser.getAccountInfo());
                                    write.flush();
                                }
                                default -> System.out.println("A valid input was not selected!");
                            }
                        }
                    }

                    case "5" -> {
                        System.out.println("user selected home");
                    }

                    case "exit" -> {
                        System.out.println("user exited");
                    }

                    default -> {
                        System.out.println("A valid input was not selected!");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
