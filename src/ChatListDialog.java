import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ChatListDialog
 *
 * A modal Swing dialog that lists all chat threads belonging to the current user.
 * It queries the server for existing conversations, extracts the peer usernames, and presents them
 * in a scrollable list. Double‑clicking a name opens the corresponding chat and closes the dialog.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class ChatListDialog extends JDialog {

    /** Main application frame. */
    private final MainFrame main;
    /** Network client used for server communication. */
    private final Client client;
    /** Identifier of the current user (UUID or username). */
    private final String me;

    /** List model that backs the on‑screen list of peers. */
    private final DefaultListModel<String> model = new DefaultListModel<>();

    /**
     * Creates and sets up the dialog.
     *
     * @param owner parent window used for modality and positioning
     * @param main  main application frame that will host chat panels
     * @param cl    client used to ask the server for chat data
     * @param me    identifier of the current user
     */
    public ChatListDialog(Window owner, MainFrame main, Client cl, String me) {
        super(owner, "Messages", ModalityType.APPLICATION_MODAL);
        this.main   = main;
        this.client = cl;
        this.me     = me;

        buildGui();
        loadChats();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Builds the Swing interface - a scrollable list in the center and a hint label at the bottom.
     * Double‑clicking a list entry opens a new chat window for that peer.
     */
    private void buildGui() {
        setLayout(new BorderLayout(5, 5));

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(250, 300));
        add(sp, BorderLayout.CENTER);

        add(new JLabel("Double‑click a user to open chat"), BorderLayout.SOUTH);

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && list.getSelectedIndex() != -1) {
                    String peer = list.getSelectedValue();
                    // explanation: create a new chat panel for the selected peer and display it
                    ChatPanel cp = new ChatPanel(main, client, me, peer);
                    main.addPanel(cp, "Chat-" + peer);
                    main.showPanel("Chat-" + peer);
                    dispose();
                }
            }
        });
    }

    /**
     * Asks the server for all chat keys, figures out the peer in each one,
     * and fills the list model (ignoring duplicates).
     */
    private void loadChats() {
        model.clear();
        client.send("LIST_CHATS|" + me);
        List<String> keys = client.readBlock();

        for (String k : keys) {
            // explanation: each key looks like "userA,userB". pick the name that isn't me.
            String[] p = k.split(",", 2);
            if (p.length == 2) {
                String peer = p[0].equals(me) ? p[1] : p[0];
                if (!model.contains(peer)) model.addElement(peer);
            }
        }
    }
}