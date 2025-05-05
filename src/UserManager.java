import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * UserManager
 *
 * Manages user accounts stored in users.txt as username,email,password,balance
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class UserManager implements UserManagerInterface {

    private static final String USER_FILE = "users.txt";
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Register a new user with a starting balance of 100.0.
     */
    @Override public synchronized boolean register(String u, String e, String p) {
        // explanation: basic input validation
        if (u.isBlank() || e.isBlank() || p.length() < 6) return false;
        if (!EMAIL.matcher(e).matches()) return false;
        if (userExists(u) || emailExists(e)) return false;

        // explanation: append new user record to file
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE, true))) {
            pw.println(u + "," + e + "," + p + ",100.0");
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    /**
     * Login by matching username, email, and password.
     */
    @Override public synchronized boolean login(String u, String e, String p) {
        // explanation: read file line by line looking for matching record
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 4);
                if (f.length == 4 && f[0].equals(u) && f[1].equals(e) && f[2].equals(p)) {
                    return true;
                }
            }
        } catch (IOException ignore) {}
        return false;
    }

    /**
     * Delete a user if username/email/password match.
     */
    @Override public synchronized boolean deleteAccount(String u, String e, String p) {
        List<String> keep = new ArrayList<>();
        boolean removed = false;
        // explanation: collect all lines except the one to remove
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 4);
                if (f[0].equals(u) && f[1].equals(e) && f[2].equals(p)) {
                    removed = true;
                } else {
                    keep.add(line);
                }
            }
        } catch (IOException ignore) {

        }
        if (!removed) return false;

        // explanation: write back only kept lines
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (String s : keep) pw.println(s);
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    /**
     * Retrieve current balance for the given user, or 0 if not found.
     */
    @Override public synchronized double getBalance(String u) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 4);
                if (f[0].equals(u)) {
                    // explanation: balance is the 4th field
                    return Double.parseDouble(f[3]);
                }
            }
        } catch (IOException ignore) {}
        return 0;
    }

    /**
     * Set a new balance for the user and save all records to file.
     */
    @Override public synchronized boolean setBalance(String u, double b) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        // explanation: read and modify matching line
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 4);
                if (f[0].equals(u)) {
                    f[3] = String.format("%.2f", b);
                    found = true;
                    line = String.join(",", f);
                }
                lines.add(line);
            }
        } catch (IOException ignore) {}
        if (!found) return false;

        // explanation: overwrite file with updated lines
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (String s : lines) pw.println(s);
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    /** Check if a username already exists. */
    @Override public boolean userExists(String u) { return check(0, u); }

    /** Check if an email already exists. */
    @Override public boolean emailExists(String e) { return check(1, e); }

    /**
     * Helper: check column idx (0=name,1=email) for a value.
     */
    private boolean check(int idx, String val) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", idx + 2);
                if (f[idx].equals(val)) {
                    return true;
                }
            }
        } catch (IOException ignore) {

        }
        return false;
    }
}

