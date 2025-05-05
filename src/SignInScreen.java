import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class SignInScreen extends JPanel {

    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectInputStream objectReader;
    private AppGUI appGUI;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerUsernameField;
    private JTextField registerEmailField;
    private JPasswordField registerPasswordField;

    public SignInScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, AppGUI appGUI) {
        this.reader = reader;
        this.writer = writer;
        this.objectReader = objectReader;
        this.appGUI = appGUI;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel loginLabel = new JLabel("Login");
        loginUsernameField = new JTextField(15);
        loginPasswordField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.println("LOGIN");
                writer.println(loginUsernameField.getText());
                writer.println(new String(loginPasswordField.getPassword()));
                try {
                    boolean loginSuccess = Boolean.parseBoolean(reader.readLine());
                    if (loginSuccess) {
                        UserProfile userProfile = (UserProfile) objectReader.readObject();
                        ContentScreen contentScreen = new ContentScreen(reader, writer, objectReader, userProfile);
                        appGUI.showPage(contentScreen);
                    } else {
                        JOptionPane.showMessageDialog(SignInScreen.this, "Invalid credentials.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignInScreen.this, "Login error.");
                }
            }
        });

        JPanel loginPanel = new JPanel();
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginBtn);

        JLabel registerLabel = new JLabel("Register");
        registerUsernameField = new JTextField(15);
        registerEmailField = new JTextField(15);
        registerPasswordField = new JPasswordField(15);
        JButton registerBtn = new JButton("Register");

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.println("REGISTER");
                writer.println(registerUsernameField.getText());
                writer.println(registerEmailField.getText());
                writer.println(new String(registerPasswordField.getPassword()));
                try {
                    boolean registerSuccess = Boolean.parseBoolean(reader.readLine());
                    if (registerSuccess) {
                        UserProfile userProfile = (UserProfile) objectReader.readObject();
                        ContentScreen contentScreen = new ContentScreen(reader, writer, objectReader, userProfile);
                        appGUI.showPage(contentScreen);
                    } else {
                        JOptionPane.showMessageDialog(SignInScreen.this, "Username already exists.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignInScreen.this, "Registration error.");
                }
            }
        });

        JPanel registerPanel = new JPanel();
        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(registerUsernameField);
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(registerEmailField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(registerPasswordField);
        registerPanel.add(registerBtn);

        add(loginLabel);
        add(loginPanel);
        add(registerLabel);
        add(registerPanel);
    }
}
