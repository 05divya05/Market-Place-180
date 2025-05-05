import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class ContentScreen extends JPanel {

    private UserProfile user;
    private PrintWriter writer;
    private BufferedReader reader;
    private ObjectInputStream objectReader;

    public ContentScreen(BufferedReader reader, PrintWriter writer, ObjectInputStream objectReader, UserProfile userProfile) {
        this.user = userProfile;
        this.reader = reader;
        this.writer = writer;
        this.objectReader = objectReader;

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Marketplace", createMarketplacePanel());
        tabbedPane.addTab("Messages", createMessagePanel());
        tabbedPane.addTab("Payments & Ratings", createPaymentPanel());
        tabbedPane.addTab("Sold & Balance", createSoldPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createMarketplacePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField itemId = new JTextField();
        JTextField name = new JTextField();
        JTextField desc = new JTextField();
        JTextField price = new JTextField();
        JTextField category = new JTextField();
        JTextField imagePath = new JTextField();
        JButton addItemBtn = new JButton("Add Item");

        panel.add(new JLabel("Item ID:")); panel.add(itemId);
        panel.add(new JLabel("Name:")); panel.add(name);
        panel.add(new JLabel("Description:")); panel.add(desc);
        panel.add(new JLabel("Price:")); panel.add(price);
        panel.add(new JLabel("Category:")); panel.add(category);
        panel.add(new JLabel("Image Path:")); panel.add(imagePath);
        panel.add(addItemBtn);

        JTextArea resultArea = new JTextArea(10, 40);
        JScrollPane scroll = new JScrollPane(resultArea);
        panel.add(scroll);

        addItemBtn.addActionListener(e -> {
            writer.println("ADD_ITEM|" + itemId.getText() + "|" + name.getText() + "|" + desc.getText() + "|" +
                    price.getText() + "|" + user.getUsername() + "|" + category.getText() + "|" + imagePath.getText());
            try {
                resultArea.setText(reader.readLine());
            } catch (Exception ex) {
                resultArea.setText("Error adding item.");
            }
        });

        return panel;
    }

    private JPanel createMessagePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField toUser = new JTextField();
        JTextField message = new JTextField();
        JButton sendBtn = new JButton("Send Message");
        JTextArea output = new JTextArea(5, 40);

        panel.add(new JLabel("To User:")); panel.add(toUser);
        panel.add(new JLabel("Message:")); panel.add(message);
        panel.add(sendBtn); panel.add(new JLabel(""));
        panel.add(new JScrollPane(output));

        sendBtn.addActionListener(e -> {
            writer.println("SEND_MESSAGE|" + user.getUsername() + "|" + toUser.getText() + "|" + message.getText());
            try {
                output.setText(reader.readLine());
            } catch (Exception ex) {
                output.setText("Error sending message.");
            }
        });

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField toUser = new JTextField();
        JTextField amount = new JTextField();
        JButton payBtn = new JButton("Send Payment");

        JTextField sellerRate = new JTextField();
        JTextField rating = new JTextField();
        JButton rateBtn = new JButton("Rate Seller");

        JTextArea output = new JTextArea(5, 40);

        panel.add(new JLabel("To User:")); panel.add(toUser);
        panel.add(new JLabel("Amount:")); panel.add(amount);
        panel.add(payBtn); panel.add(new JLabel(""));
        panel.add(new JLabel("Rate Seller:")); panel.add(sellerRate);
        panel.add(new JLabel("Rating (1-5):")); panel.add(rating);
        panel.add(rateBtn); panel.add(new JLabel(""));
        panel.add(new JScrollPane(output));

        payBtn.addActionListener(e -> {
            writer.println("MAKE_PAYMENT|" + user.getUsername() + "|" + toUser.getText() + "|" + amount.getText());
            try {
                output.setText(reader.readLine());
            } catch (Exception ex) {
                output.setText("Error during payment.");
            }
        });

        rateBtn.addActionListener(e -> {
            writer.println("RATE_SELLER|" + sellerRate.getText() + "|" + rating.getText());
            try {
                output.setText(reader.readLine());
            } catch (Exception ex) {
                output.setText("Error adding rating.");
            }
        });

        return panel;
    }

    private JPanel createSoldPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea output = new JTextArea(10, 40);
        JButton viewBtn = new JButton("View Sold Items");
        JButton balanceBtn = new JButton("Check Balance");

        JPanel btnPanel = new JPanel();
        btnPanel.add(viewBtn);
        btnPanel.add(balanceBtn);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(output), BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            writer.println("VIEW_SOLD_ITEMS|" + user.getUsername());
            try {
                output.setText("");
                String line;
                while (!(line = reader.readLine()).equals("END")) {
                    output.append("Sold Item ID: " + line + "\\n");
                }
            } catch (Exception ex) {
                output.setText("Error retrieving sold items.");
            }
        });

        balanceBtn.addActionListener(e -> {
            writer.println("GET_BALANCE|" + user.getUsername());
            try {
                output.setText("Balance: $" + reader.readLine());
            } catch (Exception ex) {
                output.setText("Error retrieving balance.");
            }
        });

        return panel;
    }
}
