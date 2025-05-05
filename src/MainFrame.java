import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * MainFrame
 *
 * Main application window. Uses a CardLayout so different screens (login,
 * marketplace, chat etc.) can be swapped in and out by name.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class MainFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel main = new JPanel(cards); // holds all screens
    private final Client client = new Client("localhost", 4242);

    // current logged‑in user info
    private String currentUser;
    private String currentEmail;
    private double currentBalance = 0.0;

    /** Set up window and show the login screen. */
    public MainFrame() {
        setTitle("Marketplace");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null); // center on screen

        main.add(new LoginPanel(this, client), "Login");
        add(main);

        setVisible(true);
    }

    /** Show a screen that has already been added. */
    public void showPanel(String name) { cards.show(main, name); }

    /** Add a new screen to the CardLayout. */
    public void addPanel(JPanel p, String name) { main.add(p, name); }

    /**
     * Called by LoginPanel after successful login. Creates the market screen
     * and switches to it.
     */
    public void loginSuccess(String username, String email, double balance) {
        this.currentUser    = username;
        this.currentEmail   = email;
        this.currentBalance = balance;

        MarketPanel market = new MarketPanel(this, client, currentUser);
        addPanel(market, "Market");
        showPanel("Market");
    }

    /** Update cached balance and refresh the label in MarketPanel. */
    public void updateBalance(double newBal) {
        currentBalance = newBal;
        // explanation: look for the market panel in the list of screens
        for (Component c : main.getComponents()) {
            if (c instanceof MarketPanel) {
                ((MarketPanel) c).refreshBalance();
                break;
            }
        }
    }

    public String getCurrentUser() { return currentUser; }
    public double getBalance()     { return currentBalance; }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}