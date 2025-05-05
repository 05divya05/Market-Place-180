import java.io.*;
import java.util.*;

/**
 * Message
 *
 * Implements basic message persistence. It appends conversation threads to
 * file messages.txt, separating each chat with a sorted key.
 * Provides methods to send a message, load the history between two users,
 * and list all chat keys involving a given user.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class Message implements MessageInterface {

    private static final String FILE = "messages.txt";

    /**
     * Compute a consistent key for two users by sorting their names and
     * joining with a comma. Used to mark the start of each conversation.
     */
    private String key(String a, String b) {
        List<String> users = Arrays.asList(a, b);
        Collections.sort(users);
        return users.get(0) + "," + users.get(1);
    }

    /**
     * Send a message from <code>f</code> (from) to <code>t</code> (to).
     * If the chat key has not yet been written, write the key line first.
     * Then write "from: content" on the next line.
     */
    @Override public synchronized void send(String f, String t, String c) throws IOException {
        String k = key(f, t);
        boolean needKey = true;

        // explanation: check if the last key in file matches k
        try (RandomAccessFile raf = new RandomAccessFile(FILE, "r")) {
            long len = raf.length();
            if (len > 0) {
                long pos = len - 1;
                // walk backwards until newline or start
                while (pos > 0) {
                    raf.seek(pos--);
                    if (raf.readByte() == '\n') {
                        String last = raf.readLine();
                        if (last != null && last.equals(k)) needKey = false;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException ignore) {
            // file doesn't exist yet, so key is needed
        }

        // append key if needed, then the message line
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE, true))) {
            if (needKey) pw.println(k);
            pw.println(f + ": " + c);
        }
    }

    /**
     * Load the message history between u1 and u2. Returns lines after the
     * matching key until the next key or EOF.
     */
    @Override public synchronized List<String> loadHistory(String u1, String u2) throws IOException {
        String k = key(u1, u2);
        List<String> res = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return res;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.equals(k)) {
                    found = true; // start collecting after this line
                    continue;
                }
                if (found) {
                    // stop if we hit another key (contains comma)
                    if (line.contains(",")) break;
                    res.add(line);
                }
            }
        }
        return res;
    }

    /**
     * List all chat keys ("user1,user2") where the given user appears.
     * Maintains insertion order, no duplicates.
     */
    @Override public synchronized List<String> listChats(String u) throws IOException {
        Set<String> keys = new LinkedHashSet<>();
        File f = new File(FILE);
        if (!f.exists()) return new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(",")) {
                    String[] p = line.split(",", 2);
                    // explanation: key lines are the only ones with a comma
                    if (p[0].equals(u) || p[1].equals(u)) {
                        keys.add(line);
                    }
                }
            }
        }
        return new ArrayList<>(keys);
    }
}
