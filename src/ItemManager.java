import java.io.*;
import java.util.*;

/**
 * ItemListingInterface
 *
 * Manages all marketplace items that are saved in the text file
 * itemListing.txt. When the program starts it reads the file into a
 * list stored in memory, and every time you add, edit, or delete an item the
 * list is written back to the file.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class ItemManager implements ItemListingInterface {
    private static final String FILE = "itemListing.txt";   // data file name
    private final List<ItemListing> cache = new ArrayList<>(); // items kept in memory

    /** Called at start‑up to fill the in‑memory list with existing items. */
    public ItemManager() throws IOException {
        load();
    }

    /** Read all lines from the file and turn them into ItemListing objects. */
    private synchronized void load() throws IOException {
        cache.clear();
        File f = new File(FILE);
        if (!f.exists()) return;// nothing to load yet
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                ItemListing it = ItemListing.fromFile(line);
                if (it != null) cache.add(it);
            }
        }
    }

    /** Write everything that is currently in memory back to the file. */
    private synchronized void save() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (ItemListing it : cache) pw.println(it.toFile());
        }
    }

    /** Add a new item and save the change to disk. */
    @Override public synchronized boolean addItem(ItemListing it) throws IOException {
        cache.add(it);
        save();
        return true;
    }

    /**
     * Find an item by <code>seller</code> + <code>title</code> and replace all
     * editable fields with the new values.
     */
    @Override public synchronized boolean editItem(String seller, String title, ItemListing up) throws IOException {
        for (ItemListing it : cache) {
            if (it.getSeller().equals(seller) && it.getTitle().equals(title)) {
                it.setDescription(up.getDescription());
                it.setPrice(up.getPrice());
                it.setCategory(up.getCategory());
                it.setImagePath(up.getImagePath());
                it.setQuantity(up.getQuantity());
                save();
                return true;
            }
        }
        return false; // item not found
    }

    /** Remove an item from the list (returns true if something was deleted). */
    @Override public synchronized boolean deleteItem(String seller, String title) throws IOException {
        boolean removed = cache.removeIf(it -> it.getSeller().equals(seller) && it.getTitle().equals(title));
        if (removed) save();
        return removed;
    }

    /** Return a copy of the full list so callers can read it safely. */
    @Override public synchronized List<ItemListing> getAll() {
        return new ArrayList<>(cache);
    }

    /** Look up a single item by seller + title (returns null if not found). */
    @Override public synchronized ItemListing find(String seller, String title) {
        return cache.stream()
                .filter(it -> it.getSeller().equals(seller) && it.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
}
