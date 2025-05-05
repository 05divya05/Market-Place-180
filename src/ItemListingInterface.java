import java.io.IOException;
import java.util.List;

/**
 * ItemListingInterface
 *
 * Basic actions for storing and retrieving ItemListing in the system.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public interface ItemListingInterface {
    boolean addItem(ItemListing item) throws IOException;
    boolean editItem(String seller,String title,ItemListing updated) throws IOException;
    boolean deleteItem(String seller,String title) throws IOException;
    List<ItemListing> getAll() throws IOException;
    ItemListing find(String seller,String title) throws IOException;
}
