import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MarketPanel extends JPanel {

    private final MainFrame main;
    private final Client client;
    private final String me;
    private final RatingManager rating;

    private final JLabel balLabel = new JLabel();
    private final JPanel listPanel;

    public MarketPanel(MainFrame frame, Client client, String username) {
        this.rating = new RatingManager(client);
        this.main = frame;
        this.client = client;
        this.me = username;

        setLayout(new BorderLayout(8, 8));

        JTextField searchField = new JTextField(15);
        JComboBox<String> catBox = new JComboBox<>(new String[]{
                "All", "Electronics", "Books", "Clothing", "Home"
        });
        JButton searchBtn = new JButton("Go");
        JButton clearBtn = new JButton("Reset");

        JPanel toolBar = new JPanel();
        toolBar.add(new JLabel("Search:"));
        toolBar.add(searchField);
        toolBar.add(new JLabel("Category:"));
        toolBar.add(catBox);
        toolBar.add(searchBtn);
        toolBar.add(clearBtn);

        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.add(new JLabel("Welcome, " + me), BorderLayout.WEST);

        balLabel.setFont(balLabel.getFont().deriveFont(Font.BOLD));
        topBar.add(balLabel, BorderLayout.CENTER);

        JButton msgBtn = new JButton("Messages");
        msgBtn.addActionListener(e ->
                new ChatListDialog(
                        SwingUtilities.getWindowAncestor(this),
                        main, client, me).setVisible(true));
        topBar.add(msgBtn, BorderLayout.EAST);

        JPanel north = new JPanel(new BorderLayout());
        north.add(toolBar, BorderLayout.NORTH);
        north.add(topBar, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(listPanel), BorderLayout.CENTER);

        JButton createBtn = new JButton("Create Item");
        createBtn.addActionListener(e -> {
            new CreateItemDialog(main, client, me).setVisible(true);
            refreshMarket(listPanel, "", "All");
        });
        add(createBtn, BorderLayout.SOUTH);

        searchBtn.addActionListener(e ->
                refreshMarket(listPanel,
                        searchField.getText().trim(),
                        (String) catBox.getSelectedItem()));

        clearBtn.addActionListener(e -> {
            searchField.setText("");
            catBox.setSelectedIndex(0);
            refreshMarket(listPanel, "", "All");
        });

        refreshBalance();
        refreshMarket(listPanel, "", "All");
    }

    public void refreshBalance() {
        client.send("BALANCE|" + me);
        balLabel.setText("Balance: $" + client.read());
    }

    private void refreshMarket(JPanel target, String keyword, String category) {
        target.removeAll();

        client.send("GET_ITEMS");
        List<String> items = client.readBlock();
        for (String line : items) {
            String[] p = line.split(",", 7);
            if (p.length != 7) continue;

            String title = p[0], desc = p[1], price = p[2],
                    seller = p[3], img = p[4], cat = p[5], qtyStr = p[6];

            if (!keyword.isEmpty() &&
                    !(title.toLowerCase().contains(keyword.toLowerCase())
                            || desc.toLowerCase().contains(keyword.toLowerCase())))
                continue;
            if (!"All".equals(category) && !category.equalsIgnoreCase(cat)) continue;

            int qty = Integer.parseInt(qtyStr);
            target.add(createItemPanel(title, desc, price, seller, img, cat, qty));
        }
        target.revalidate();
        target.repaint();
    }

    private JPanel createItemPanel(String title, String desc, String price,
                                   String seller, String img, String cat, int qty) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        /* 左侧图片 */
        if (!img.isBlank() && new File(img).exists()) {
            ImageIcon icon = new ImageIcon(img);
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.WEST);
        }

        /* 中间文字 */
        double avg = rating.getAverage(seller);
        String ratingStr = avg < 0 ? "No rating" : String.format("%.1f / 5", avg);

        JTextArea info = new JTextArea("""
                Title: %s
                Description: %s
                Price: $%s
                Category: %s
                Quantity: %d
                Seller: %s
                Rating: %s
                """.formatted(title, desc, price, cat, qty, seller, ratingStr));
        info.setEditable(false);
        panel.add(info, BorderLayout.CENTER);

        /* 右侧按钮区 */
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        if (seller.equals(me)) {                /* ====== 卖家视角 ====== */

            JButton editBtn = new JButton("Edit");
            editBtn.addActionListener(e -> {
                String fileLine = String.join(",", title, desc, price,
                        seller, img, cat, String.valueOf(qty));
                new EditItemDialog(
                        SwingUtilities.getWindowAncestor(this),
                        client, me, fileLine
                ).setVisible(true);

                refreshMarket(listPanel, "", "All");
            });
            right.add(editBtn);

            JButton delBtn = new JButton("Delete");
            delBtn.addActionListener(e -> {
                int c = JOptionPane.showConfirmDialog(this,
                        "Delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    client.send("DELETE_ITEM|" + me + "|" + title);
                    if ("SUCCESS".equals(client.read())) {
                        JOptionPane.showMessageDialog(this, "Item deleted.");
                        refreshMarket(listPanel, "", "All");
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed.");
                    }
                }
            });
            right.add(delBtn);

        } else {

            Integer[] nums = new Integer[qty];
            for (int i = 0; i < qty; i++) nums[i] = i + 1;
            JComboBox<Integer> qtyBox = new JComboBox<>(nums);
            right.add(qtyBox);

            JButton buyBtn = new JButton("Buy");
            buyBtn.addActionListener(e -> {
                int want = (Integer) qtyBox.getSelectedItem();
                client.send("BUY_ITEM|%s|%s|%s|%d".formatted(me, title, seller, want));
                String resp = client.read();
                if (resp != null && resp.startsWith("SUCCESS")) {
                    double newBal = Double.parseDouble(resp.split("\\|")[1]);
                    balLabel.setText("Balance: $" + newBal);
                    JOptionPane.showMessageDialog(this, "Purchase successful!");
                    /* 评分 */
                    String s = JOptionPane.showInputDialog(this,
                            "Rate the seller (0‑5):", "5");
                    if (s != null) {
                        try {
                            double sc = Double.parseDouble(s);
                            if (sc >= 0 && sc <= 5) rating.addRating(me, seller, sc);
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    refreshMarket(listPanel, "", "All");
                } else {
                    JOptionPane.showMessageDialog(this, "Purchase failed.");
                }
            });
            right.add(buyBtn);

            JButton msgBtn = new JButton("Message");
            msgBtn.addActionListener(e -> {
                ChatPanel cp = new ChatPanel(main, client, me, seller);
                main.addPanel(cp, "Chat-" + seller);
                main.showPanel("Chat-" + seller);
            });
            right.add(msgBtn);
        }

        panel.add(right, BorderLayout.EAST);
        return panel;
    }
}