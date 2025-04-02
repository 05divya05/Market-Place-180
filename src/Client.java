public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        boolean registrationComplete = false;
        boolean loginComplete = false;

        try (Socket socket = new Socket("localhost", 1234);
             Scanner scan = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream())) {

            // Authentication loop
            do {
                System.out.println("\nPlease Specify An Authentication Method");
                System.out.println("1 : Login\n2 : Register");
                String choice = scan.nextLine().trim();
                writer.println(choice);

                if (choice.equalsIgnoreCase("1")) { // Login process
                    do {
                        System.out.println("Enter your username:");
                        String username = scan.nextLine();
                        writer.println(username);

                        System.out.println("Enter your password:");
                        String password = scan.nextLine();
                        writer.println(password);

                        loginComplete = objectReader.readBoolean();

                        if (loginComplete) {
                            System.out.println("Login Successful");
                        } else {
                            System.out.println("Login Failed. Please try again");
                        }
                    } while (!loginComplete);
                } else if (choice.equalsIgnoreCase("2")) { // Registration process
                    System.out.println("Enter a username:");
                    String username = scan.nextLine();
                    writer.println(username);

                    System.out.println("Enter an email:");
                    String email = scan.nextLine();
                    writer.println(email);

                    System.out.println("Enter a password:");
                    String password = scan.nextLine();
                    writer.println(password);

                    registrationComplete = objectReader.readBoolean();

                    if (registrationComplete) {
                        System.out.println("Registration complete.");
                    } else {
                        System.out.println("User already exists. Please login");
                    }
                }
            } while (!registrationComplete && !loginComplete);

            System.out.println("\n--------- Welcome to 180 Market Place! ---------");
        }
    }
}
