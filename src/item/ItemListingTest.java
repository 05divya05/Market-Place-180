package item;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.List;

public class ItemListingTest {

    // Test case for creating a new item listing
    @Test
    public void testCreateItemListing() {
        String author = "Test Seller";
        String title = "Test item.Item";
        String imagePath = "/path/to/image";
        String date = "2025-04-06";
        String category = "Electronics";

        // Create new item listing
        ItemListing item = new ItemListing(author, title, imagePath, date, category);

        // Verify that the item has been created with correct values
        assertEquals(author, item.getAuthor());
        assertEquals(title, item.getTitle());
        assertEquals(imagePath, item.getImagePath());
        assertEquals(date, item.getDate());
        assertEquals(category, item.getCategory());
        assertEquals(0, item.getUpvotes());  // Initially 0
        assertEquals(0, item.getDownvotes()); // Initially 0
        assertFalse(item.isSold());  // Initially not sold
    }

    // Test case for incrementing upvotes
    @Test
    public void testIncrementUpvotes() {
        ItemListing item = new ItemListing("Test Seller", "Test item.Item", "/path/to/image", "2025-04-06", "Electronics");

        item.incrementUpvotes();
        assertEquals(1, item.getUpvotes());  // Verify upvotes incremented

        item.incrementUpvotes();
        assertEquals(2, item.getUpvotes());  // Verify upvotes incremented again
    }

    // Test case for incrementing downvotes
    @Test
    public void testIncrementDownvotes() {
        ItemListing item = new ItemListing("Test Seller", "Test item.Item", "/path/to/image", "2025-04-06", "Electronics");

        item.incrementDownvotes();
        assertEquals(1, item.getDownvotes());  // Verify downvotes incremented

        item.incrementDownvotes();
        assertEquals(2, item.getDownvotes());  // Verify downvotes incremented again
    }

    // Test case for deleting an item listing
    @Test
    public void testDeleteItemListing() throws IOException {
        // Assuming itemListing.txt already has a test item in it
        String titleToDelete = "Test item.Item";

        // Create an item and write it to file
        ItemListing item = new ItemListing("Test Seller", titleToDelete, "/path/to/image", "2025-04-06", "Electronics");

        // Ensure the item exists before deletion
        File file = new File("itemListing.txt");
        assertTrue(file.exists());

        // Delete the item
        ItemListing.deletePost(titleToDelete);

        // Verify that the file no longer contains the item
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean itemDeleted = true;

        while ((line = reader.readLine()) != null) {
            if (line.contains(titleToDelete)) {
                itemDeleted = false;  // The item should be deleted
                break;
            }
        }

        assertTrue(itemDeleted);  // The item should be deleted from the file
    }

    // Test case for searching items with a keyword
    @Test
    public void testSearchItemsWithKeyword() throws IOException {
        // Assuming itemListing.txt contains multiple items, including one with the keyword
        String keyword = "Test";
        List<ItemListing> results = ItemListing.searchItems(null, keyword, false);

        assertNotNull(results);
        assertFalse(results.isEmpty());

        // Verify that all found items contain the keyword in title, author, or category
        for (ItemListing item : results) {
            assertTrue(item.getTitle().contains(keyword) || item.getAuthor().contains(keyword) || item.getCategory().contains(keyword));
        }
    }

    // Test case for searching items by category
    @Test
    public void testSearchItemsByCategory() throws IOException {
        // Assuming itemListing.txt contains items under the "Electronics" category
        String category = "Electronics";
        List<ItemListing> results = ItemListing.searchItems(category, null, false);

        assertNotNull(results);
        assertFalse(results
