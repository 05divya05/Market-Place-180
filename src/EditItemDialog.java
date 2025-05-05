import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * EditItemDialog
 *
 * A modal form that lets a seller modify an existing marketplace listing. The
 * constructor receives the current item data, populates the fields,
 * and when Save is pressed sends a command to the server.
 * On success the dialog closes; otherwise an error message appears.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class EditItemDialog extends JDialog {

    private final Client client;// for server communication
    private final String seller;// seller username
    private final String origTitle;// original title used as primary key

    private final JTextField tfDesc  = new JTextField();
    private final JTextField tfPrice = new JTextField();
    private final JComboBox<String> boxCat = new JComboBox<>(
            new String[]{"Electronics","Books","Clothing","Home","Sports"}
    );
    private final JTextField tfImg = new JTextField(); // read-only image path
    private final JComboBox<Integer> boxQty = new JComboBox<>(
            java.util.stream.IntStream.rangeClosed(1, 99)
                    .boxed().toArray(Integer[]::new)
    );

    /**
     * Initialize dialog and populate fields from a CSV line.
     * @param owner    parent window
     * @param client   network client
     * @param seller   seller username
     * @param fileLine CSV: title,desc,price,seller,img,cat,qty
     */
    public EditItemDialog(Window owner, Client client, String seller, String fileLine) {
        super(owner, "Edit Item", ModalityType.APPLICATION_MODAL);
        this.client = client;
        this.seller = seller;

        // split CSV and fill fields
        String[] f = fileLine.split(",", 7);
        origTitle = f[0];
        tfDesc.setText(f[1]);
        tfPrice.setText(f[2]);
        tfImg.setText(f[4]);
        tfImg.setEditable(false);
        boxCat.setSelectedItem(f[5]);
        boxQty.setSelectedItem(Integer.parseInt(f[6]));

        buildGui();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Assemble the UI using simpler layouts (Border + Grid + Flow).
     */
    private void buildGui() {
        setLayout(new BorderLayout(8, 8));

        // form panel: 5 rows × 2 columns
        JPanel form = new JPanel(new GridLayout(5, 2, 4, 4));
        form.add(new JLabel("Description:"));
        form.add(tfDesc);
        form.add(new JLabel("Price ($):"));
        form.add(tfPrice);
        form.add(new JLabel("Category:"));
        form.add(boxCat);

        // image chooser row
        form.add(new JLabel("Image Path:"));
        JPanel imgPane = new JPanel(new BorderLayout(4, 0));
        imgPane.add(tfImg, BorderLayout.CENTER);
        JButton browse = new JButton("Browse…");
        imgPane.add(browse, BorderLayout.EAST);
        form.add(imgPane);

        form.add(new JLabel("Quantity:"));      form.add(boxQty);

        add(form, BorderLayout.CENTER);
        // Save button at bottom right
        JButton saveBtn = new JButton("Save");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // open file chooser when browse clicked
        browse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                tfImg.setText(f.getAbsolutePath());
            }
        });

        // save action: validate price, build command and send
        saveBtn.addActionListener(e -> save());
    }

    /**
     * Send EDIT_ITEM command to server or show error if price is invalid.
     */
    private void save() {
        double price;
        try {
            price = Double.parseDouble(tfPrice.getText().trim());
        } catch (NumberFormatException ex) {
            warn("Price must be a number");
            return;
        }

        // build and send command
        String msg = String.join("|",
                "EDIT_ITEM", seller, origTitle,
                tfDesc.getText().trim(), String.valueOf(price),
                (String)boxCat.getSelectedItem(),
                tfImg.getText().trim(),
                String.valueOf(boxQty.getSelectedItem())
        );
        client.send(msg);

        if ("SUCCESS".equals(client.read())) {
            dispose();
        } else {
            warn("Edit failed");
        }
    }

    // show a warning dialog
    private void warn(String m) {
        JOptionPane.showMessageDialog(this, m);
    }
}