import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * CreateItemDialog
 *
 * A modal form that lets a seller enter all the details for a new marketplace
 * listing (title, description, price, category, quantity, and an optional
 * product image). When the user clicks the data is sent to the
 * server; on success the dialog closes, otherwise an error message is shown.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class CreateItemDialog extends JDialog {

    /** Connection used to talk to the backend. */
    private final Client client;
    /** Username of the seller creating the item. */
    private final String sellerUsername;

    /**
     * Builds and shows the item‑creation form.
     *
     * @param mainFrame       parent window for modality/centering
     * @param client          network client
     * @param sellerUsername  current seller's username
     */
    public CreateItemDialog(MainFrame mainFrame, Client client, String sellerUsername) {
        this.client = client;
        this.sellerUsername = sellerUsername;

        setTitle("Create New Item");
        setSize(400, 400);
        setLocationRelativeTo(mainFrame);
        setLayout(new GridLayout(8, 2));

        // Input fields
        JTextField titleField    = new JTextField();
        JTextField descField     = new JTextField();
        JTextField priceField    = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{
                "Electronics", "Books", "Clothing", "Home", "Other"
        });
        JTextField quantityField = new JTextField();

        JTextField imagePathField = new JTextField();
        imagePathField.setEditable(false);
        JButton browseBtn = new JButton("Choose Image");

        // Layout helpers
        add(new JLabel("Title:"));       add(titleField);
        add(new JLabel("Description:")); add(descField);
        add(new JLabel("Price:"));       add(priceField);
        add(new JLabel("Category:"));    add(categoryBox);
        add(new JLabel("Quantity:"));    add(quantityField);
        add(new JLabel("Image Path:"));  add(imagePathField);
        add(new JLabel(""));             add(browseBtn); // empty label as spacer

        // explanation: open file chooser and copy chosen path to read‑only field
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        JButton createBtn = new JButton("Create");
        add(createBtn);

        // explanation: validate input then send ADD_ITEM command to server
        createBtn.addActionListener(e -> {
            String title     = titleField.getText().trim();
            String desc      = descField.getText().trim();
            String price     = priceField.getText().trim();
            String category  = (String) categoryBox.getSelectedItem();
            String quantity  = quantityField.getText().trim();
            String imagePath = imagePathField.getText().trim();

            if (title.isEmpty() || desc.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            client.send("ADD_ITEM|" + sellerUsername + "|" + title + "|" + desc + "|" + price +
                    "|" + category + "|" + quantity + "|" + imagePath);
            String res = client.read();
            if ("SUCCESS".equals(res)) {
                JOptionPane.showMessageDialog(this, "Item created!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create item.");
            }
        });
    }
}
