import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * MarketPanel
 *
 * Displays the marketplace: a toolbar for search and category filters,
 * the user's balance, a scrolling list of item panels, and a button
 * to create a new item. Sellers see Edit/Delete buttons, buyers see
 * quantity selector, Buy, and Message buttons.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class MarketPanel extends JPanel {

    private final MainFrame main;   // reference to main window for panel switching
    private final Client client;    // network helper
    private final String me;        // current username
    private final RatingManager rating; // handles seller rating logic

    private final JLabel balLabel = new JLabel(); // shows "Balance: $..."
    private final JPanel listPanel;             // container for item panels

    /**
     * Build the main marketplace view.
     * @param frame    main application window
     * @param client   network client
     * @param username current logged-in user
     */
    public MarketPanel(MainFrame frame, Client client, String username) {
        this.main = frame;
        this.client = client;
        this.me = username;
        this.rating = new RatingManager(client);

        setLayout(new BorderLayout(8, 8));

        // toolbar: search field, category dropdown, Go and Reset buttons
        JTextField searchField = new JTextField(15);
        JComboBox<String> catBox = new JComboBox<>(new String[]{"All","Electronics","Books","Clothing","Home"});
        JButton searchBtn = new JButton("Go");
        JButton clearBtn = new JButton("Reset");
        JPanel toolBar = new JPanel();
        toolBar.add(new JLabel("Search:"));
        toolBar.add(searchField);
        toolBar.add(new JLabel("Category:"));
        toolBar.add(catBox);
        toolBar.add(searchBtn);
        toolBar.add(clearBtn);

        // top bar: welcome message, balance, Messages button
        JPanel topBar = new JPanel(new BorderLayout(10,0));
        topBar.add(new JLabel("Welcome, " + me), BorderLayout.WEST);
        balLabel.setFont(balLabel.getFont().deriveFont(Font.BOLD));
        topBar.add(balLabel, BorderLayout.CENTER);
        JButton msgBtn = new JButton("Messages");
        // explanation: show dialog listing chat threads
        msgBtn.addActionListener(e -> new ChatListDialog(
                SwingUtilities.getWindowAncestor(this), main, client, me)
                .setVisible(true));
        topBar.add(msgBtn, BorderLayout.EAST);

        // put toolbar above topBar
        JPanel north = new JPanel(new BorderLayout());
        north.add(toolBar, BorderLayout.NORTH);
        north.add(topBar, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        // main list area: vertical box layout inside a scroll pane
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        // create item button at the bottom
        JButton createBtn = new JButton("Create Item");
        createBtn.addActionListener(e -> {
            new CreateItemDialog(main, client, me).setVisible(true);
            // refresh after returning from dialog
            refreshMarket("", "All");
        });
        add(createBtn, BorderLayout.SOUTH);

        // search and reset actions
        searchBtn.addActionListener(e -> refreshMarket(
                searchField.getText().trim(), (String)catBox.getSelectedItem()));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            catBox.setSelectedIndex(0);
            refreshMarket("", "All");
        });

        // initial load
        refreshBalance();
        refreshMarket("", "All");
    }

    /** Send BALANCE request and update the balance label. */
    public void refreshBalance() {
        client.send("BALANCE|" + me);
        balLabel.setText("Balance: $" + client.read());
    }

    /**
     * Load items from server, filter by keyword/category,
     * and populate listPanel with item entries.
     */
    private void refreshMarket(String keyword, String category) {
        listPanel.removeAll();
        client.send("GET_ITEMS");
        List<String> items = client.readBlock();
        for (String line : items) {
            String[] p = line.split(",",7);
            if (p.length != 7) continue;
            String title = p[0];
            String desc = p[1];
            String price = p[2];
            String seller = p[3];
            String img = p[4];
            String cat = p[5];
            String qtyStr = p[6];
            // skip if keyword not in title or description
            if (!keyword.isEmpty() &&
                    !(title.toLowerCase().contains(keyword.toLowerCase())
                            || desc.toLowerCase().contains(keyword.toLowerCase()))) continue;
            // skip if category doesn't match
            if (!"All".equals(category) && !category.equalsIgnoreCase(cat)) continue;
            int qty = Integer.parseInt(qtyStr);
            listPanel.add(createItemPanel(title, desc, price, seller, img, cat, qty));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Build a panel showing one item's details and action buttons.
     */
    private JPanel createItemPanel(String title, String desc, String price,
                                   String seller, String img, String cat, int qty) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // left: item image (scaled if file exists)
        if (!img.isBlank() && new File(img).exists()) {
            ImageIcon icon = new ImageIcon(img);
            Image scaled = icon.getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.WEST);
        }

        // center: text info area
        double avg = rating.getAverage(seller);
        String ratingStr = avg<0 ? "No rating" : String.format("%.1f / 5", avg);
        JTextArea info = new JTextArea(String.format(
                "Title: %s\nDescription: %s\nPrice: $%s\nCategory: %s\nQuantity: %d\nSeller: %s\nRating: %s\n",
                title,desc,price,cat,qty,seller,ratingStr));
        info.setEditable(false);
        panel.add(info, BorderLayout.CENTER);

        // right: buttons depending on seller vs buyer
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        if (seller.equals(me)) {
            // seller: Edit and Delete
            JButton edit = new JButton("Edit");
            edit.addActionListener(e -> {
                String fileLine = String.join(",", title, desc, price, seller, img, cat, String.valueOf(qty));
                new EditItemDialog(
                        SwingUtilities.getWindowAncestor(this), client, me, fileLine
                ).setVisible(true);
                refreshMarket("","All");
            });
            right.add(edit);

            JButton del = new JButton("Delete");
            del.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Delete this item?", "Confirm", JOptionPane.YES_NO_OPTION)
                        == JOptionPane.YES_OPTION) {
                    client.send("DELETE_ITEM|"+me+"|"+title);
                    if ("SUCCESS".equals(client.read())) JOptionPane.showMessageDialog(this, "Deleted.");
                    else JOptionPane.showMessageDialog(this, "Delete failed.");
                    refreshMarket("","All");
                }
            });
            right.add(del);
        } else {
            // buyer: quantity selector, Buy, Message
            Integer[] nums = new Integer[qty];
            for (int i=0;i<qty;i++) nums[i]=i+1;
            JComboBox<Integer> qtyBox = new JComboBox<>(nums);
            right.add(qtyBox);

            JButton buy = new JButton("Buy");
            buy.addActionListener(e -> {
                int want = (Integer)qtyBox.getSelectedItem();
                client.send(String.format("BUY_ITEM|%s|%s|%s|%d", me,title,seller,want));
                String resp = client.read();
                if (resp!=null && resp.startsWith("SUCCESS")) {
                    double newBal = Double.parseDouble(resp.split("\\|")[1]);
                    balLabel.setText("Balance: $"+newBal);
                    JOptionPane.showMessageDialog(this, "Purchase successful!");
                    // ask buyer to rate seller
                    String s = JOptionPane.showInputDialog(this, "Rate seller (0-5):","5");
                    if (s!=null) try {
                        double sc = Double.parseDouble(s);
                        if (sc>=0 && sc<=5) rating.addRating(me,seller,sc);
                    } catch (NumberFormatException ignored) {}
                    refreshMarket("","All");
                } else JOptionPane.showMessageDialog(this, "Purchase failed.");
            });
            right.add(buy);

            JButton msg = new JButton("Message");
            msg.addActionListener(e -> {
                ChatPanel cp = new ChatPanel(main, client, me, seller);
                main.addPanel(cp, "Chat-"+seller);
                main.showPanel("Chat-"+seller);
            });
            right.add(msg);
        }
        panel.add(right, BorderLayout.EAST);
        return panel;
    }
}
