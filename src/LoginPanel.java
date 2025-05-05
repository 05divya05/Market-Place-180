import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel
 *
 * A simple login/registration screen for the marketplace client.
 * Reads username, email, and password, then sends LOGIN or REGISTER
 * commands to the server through Client.java.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class LoginPanel extends JPanel {

    private final MainFrame mainFrame; // main window for screen switching
    private final Client client;// network helper

    public LoginPanel(MainFrame mf, Client cl) {
        this.mainFrame = mf;
        this.client = cl;
        buildGui();
    }

    /** Builds the UI. Buttons are placed in a FlowLayout so they stay compact. */
    private void buildGui() {
        setLayout(new BorderLayout(10, 10));

        // title
        JLabel title = new JLabel("Welcome to the Marketplace", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(22f));
        add(title, BorderLayout.NORTH);

        //form panel: 3 rows, 2 columns
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        JTextField user = new JTextField(16);
        JTextField email = new JTextField(16);
        JPasswordField pass = new JPasswordField(16);
        form.add(new JLabel("Username:"));
        form.add(user);
        form.add(new JLabel("Email:"));
        form.add(email);
        form.add(new JLabel("Password:"));
        form.add(pass);

        // button row with FlowLayout
        JButton login = new JButton("Login");
        JButton reg = new JButton("Register");
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.add(login);
        btnRow.add(reg);

        // combine form + buttons
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(form);
        center.add(Box.createVerticalStrut(10));
        center.add(btnRow);
        add(center, BorderLayout.CENTER);

        // Login button: send LOGIN, then BALANCE on success
        login.addActionListener(e -> {
            String u  = user.getText().trim();
            String em = email.getText().trim();
            String pw = new String(pass.getPassword());
            client.send("LOGIN|" + u + "|" + em + "|" + pw);
            String resp = client.read();
            if ("SUCCESS".equals(resp)) {
                client.send("BALANCE|" + u);
                double bal = Double.parseDouble(client.read());
                JOptionPane.showMessageDialog(this, "Login successful!");
                mainFrame.loginSuccess(u, em, bal);
            } else {
                JOptionPane.showMessageDialog(this, "Login failed.");
            }
        });

        // Register button: send REGISTER, then show result message
        reg.addActionListener(e -> {
            String u  = user.getText().trim();
            String em = email.getText().trim();
            String pw = new String(pass.getPassword());

            client.send("REGISTER|" + u + "|" + em + "|" + pw);
            String resp = client.read();
            String msg = "SUCCESS".equals(resp)
                    ? "Registration success! Please login."
                    : "Register failed (username/email exists or pw<6).";
            JOptionPane.showMessageDialog(this, msg);
        });
    }
}
