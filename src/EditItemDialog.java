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

    /** Client used for server communication. */
    private final Client client;
    /** Username of the seller who owns the item. */
    private final String seller;
    /** Original title (primary key) used to identify the record on the server. */
    private final String origTitle;

    private final JTextField tfDesc  = new JTextField();
    private final JTextField tfPrice = new JTextField();
    private final JComboBox<String> boxCat =
            new JComboBox<>(new String[]{"Electronics","Books","Clothing","Home","Sports"});
    private final JTextField tfImg   = new JTextField(); // read‑only, shows chosen path
    private final JComboBox<Integer> boxQty =
            new JComboBox<>(java.util.stream.IntStream.rangeClosed(1,99)
                    .boxed().toArray(Integer[]::new));

    /**
     * Builds the dialog and pre‑fills all fields with existing values.
     *
     * @param owner      parent window for modality/centering
     * @param client     network client
     * @param seller     username of the item owner
     * @param fileLine   CSV string: title,desc,price,seller,img,cat,qty
     */
    public EditItemDialog(Window owner, Client client, String seller, String fileLine){
        super(owner, "Edit Item", ModalityType.APPLICATION_MODAL);
        this.client = client;
        this.seller = seller;

        // explanation: split the CSV line and populate input widgets
        String[] f = fileLine.split(",", 7); // title,desc,price,seller,img,cat,qty
        origTitle = f[0];
        tfDesc .setText(f[1]);
        tfPrice.setText(f[2]);
        tfImg  .setText(f[4]);
        tfImg.setEditable(false);
        boxCat .setSelectedItem(f[5]);
        boxQty .setSelectedItem(Integer.parseInt(f[6]));

        buildGui();
        pack();
        setLocationRelativeTo(owner);
    }

    //GUI
    private void buildGui(){
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4,4,4,4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        addRow("Description:", tfDesc , g, 0);
        addRow("Price ($):",  tfPrice, g, 1);
        addRow("Category:",   boxCat , g, 2);

        // Image row: text field + Browse button
        g.gridx = 0; g.gridy = 3; g.weightx = 0; add(new JLabel("Image Path:"), g);
        JPanel imgPane = new JPanel(new BorderLayout(4,0));
        imgPane.add(tfImg, BorderLayout.CENTER);
        JButton browse = new JButton("Browse…");
        imgPane.add(browse, BorderLayout.EAST);
        g.gridx = 1; g.weightx = 1; add(imgPane, g);

        // explanation: open file chooser and update image path
        browse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                tfImg.setText(f.getAbsolutePath());
            }
        });

        addRow("Quantity:", boxQty, g, 4);

        JButton btn = new JButton("Save");
        g.gridx = 1; g.gridy = 5; add(btn, g);
        btn.addActionListener(e -> save());
    }

    private void addRow(String lab, JComponent comp, GridBagConstraints g, int y){
        g.gridx = 0; g.gridy = y; g.weightx = 0; add(new JLabel(lab), g);
        g.gridx = 1; g.weightx = 1; add(comp, g);
    }

    //save ITEM changes
    private void save(){
        double price;
        try {
            price = Double.parseDouble(tfPrice.getText().trim());
        } catch (NumberFormatException ex) {
            warn("Price must be a number");
            return;
        }

        // explanation: build EDIT_ITEM command and send to server
        String msg = String.join("|", "EDIT_ITEM", seller, origTitle,
                tfDesc.getText().trim(), String.valueOf(price),
                (String) boxCat.getSelectedItem(),
                tfImg.getText().trim(),
                String.valueOf(boxQty.getSelectedItem()));
        client.send(msg);

        if ("SUCCESS".equals(client.read())) dispose();
        else warn("Edit failed");
    }

    private void warn(String m){ JOptionPane.showMessageDialog(this, m); }
}
