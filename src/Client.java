import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Client
 *
 * It sends simple text commands to the server, waits for replies, and exposes helper
 * methods for reading a single line or a multi‑line block that terminates with
 * the sentinel string
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class Client {

    /** Network socket connected to the server. */
    private Socket socket;
    /** Reader for incoming text lines. */
    private BufferedReader in;
    /** Writer for outgoing commands. */
    private PrintWriter out;

    /**
     * Opens a blocking TCP connection to the given host and port.
     *
     * @param host server hostname or IP address
     * @param port server port
     * @throws RuntimeException if the connection cannot be established
     */
    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // auto‑flush
        } catch (IOException e) {
            throw new RuntimeException("Connect server fail");
        }
    }

    /** Sends a single text line to the server. */
    public void send(String s) {
        out.println(s);
    }

    /** Reads one line from the server (returns <code>null</code> on error). */
    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a block of lines until the server sends <code>"END"</code>.
     * @return all lines (excluding the sentinel)
     */
    public List<String> readBlock() {
        List<String> r = new ArrayList<>();
        try {
            // explanation: stop when we hit the END marker or the stream closes
            String l;
            while ((l = in.readLine()) != null && !l.equals("END")) r.add(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}

