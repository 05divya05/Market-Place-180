import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ChatPanel
 *
 * A chat window that shows message history between the current user
 * and a selected peer. It lets users send new messages, periodically refreshes the conversation,
 * and provides a button to return to the marketplace view.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class ChatPanel extends JPanel {

    /** Reference to the main application frame (for navigation). */
    private final MainFrame main;
    /** Client used to exchange data with the server. */
    private final Client client;
    /** Current user and chat partner identifiers. */
    private final String me, peer;

    /** Text area that displays the chat history. */
    private final JTextArea chatArea = new JTextArea();

    /**
     * Builds the panel, loads existing messages, and starts the auto‑refresh timer.
     *
     * @param mf   main application frame
     * @param cl   network client
     * @param me   identifier of the current user
     * @param peer identifier of the chat partner
     */
    public ChatPanel(MainFrame mf, Client cl, String me, String peer) {
        this.main = mf;
        this.client = cl;
        this.me = me;
        this.peer = peer;

        buildGui();// assemble Swing components
        loadHistory();// initial history load
        startTimer();// begin periodic refresh
    }

    /**
     * Assembles the chat interface: back button, scrollable chat area,
     * text input field, and send button.
     */
    private void buildGui() {
        setLayout(new BorderLayout(5, 5));

        // Back button switches view to the marketplace
        JButton back = new JButton("< Back");
        back.addActionListener(e -> main.showPanel("Market"));
        add(back, BorderLayout.NORTH);

        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JTextField input = new JTextField();
        JButton send = new JButton("Send");
        send.addActionListener(e -> {
            String txt = input.getText().trim();
            if (txt.isEmpty()) return;
            // explanation: send message to server → wait for SUCCESS → append locally
            client.send("SEND_MESSAGE|" + me + "|" + peer + "|" + txt);
            if ("SUCCESS".equals(client.read())) {
                input.setText("");
                append(me + ": " + txt);
            }
        });

        JPanel south = new JPanel(new BorderLayout());
        south.add(input, BorderLayout.CENTER);
        south.add(send, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Clears the chat area and reloads the entire conversation from the server.
     * Called at startup and by the refresh timer.
     */
    private void loadHistory() {
        chatArea.setText("");
        client.send("LOAD_CHAT|" + me + "|" + peer);
        for (String l : client.readBlock()) append(l);
    }

    /** Convenience wrapper to append a single line to {@link #chatArea}. */
    private void append(String l) {
        chatArea.append(l + "\n"); 
    }

    /**
     * Starts a Swing timer that refreshes the chat history every two seconds.
     * This keeps the window up‑to‑date with incoming messages.
     */
    private void startTimer() {
        new javax.swing.Timer(2000, e -> loadHistory()).start();
    }
}
