import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainFrame extends JFrame {

    private final CardLayout cards   = new CardLayout();
    private final JPanel     main    = new JPanel(cards);

    private final Client client = new Client("localhost", 4242);

    private String  currentUser;
    private String  currentEmail;
    private double  currentBalance = 0.0;

    public MainFrame() {
        setTitle("Marketplace");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        main.add(new LoginPanel(this, client), "Login");
        add(main);

        setVisible(true);
    }

    public void showPanel(String name) { cards.show(main, name); }
    public void addPanel(JPanel p, String name) { main.add(p, name); }

    public void loginSuccess(String username, String email, double balance) {
        this.currentUser     = username;
        this.currentEmail    = email;
        this.currentBalance  = balance;

        MarketPanel market = new MarketPanel(this, client, currentUser);
        addPanel(market, "Market");
        showPanel("Market");
    }

    public void updateBalance(double newBal) {
        currentBalance = newBal;
        Component comp = Arrays.stream(main.getComponents())
                .filter(c -> c instanceof MarketPanel)
                .findFirst().orElse(null);
        if (comp instanceof MarketPanel mp) mp.refreshBalance();
    }

    public String getCurrentUser()  { return currentUser; }
    public double getBalance()      { return currentBalance; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
