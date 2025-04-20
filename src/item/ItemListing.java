package item;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
*This class handles seller's item listings, the listings have upvotes and downvotes so that users can see the popularity of the items.
* In this class you can also search for items using keywords or search several items by category.
* user.Users will also be able to see items that have already been sold. To sum, this class handles the main item listing of creation, deletion, and search.
*
* @version April 6th 2025
* @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
*
*/
public class ItemListing implements ItemListingInterface, Serializable {
    private String author;
    private String title;
    private String imagePath;
    private String date;
    private String category;
    private int upvotes;
    private int downvotes;
    private boolean sold;


    public ItemListing(String author, String title, String imagePath, String date, String category) {
        this.author = author;
        this.title = title;
        this.imagePath = imagePath;
        this.date = date;
        this.category = category;
        this.upvotes = 0;
        this.downvotes = 0;
        this.sold = false;

        try (PrintWriter writer = new PrintWriter(new FileWriter("itemListing.txt", true))) {
            writer.println(author + "," + title + "," + imagePath + "," + date + "," + category + "," + upvotes + "," + downvotes + "," + sold);
        } catch (IOException e) {
            System.out.println("An error occurred while creating the itemListing: " + e.getMessage());
        }
    }

    public ItemListing() {
        // Default constructor
    }

    public static void deletePost(String title) {
        System.out.println("Deleting post: " + title);

        File originalFile = new File("itemListing.txt");
        File tempFile = new File("tempFile.txt");

        try (
                BufferedReader readPost = new BufferedReader(new FileReader(originalFile));
                PrintWriter tempWrite = new PrintWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = readPost.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && parts[1].equals(title)) {
                    continue; // skip the line to "delete" the post
                }
                tempWrite.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the post: " + e.getMessage());
            return;
        }

        if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
            System.out.println("Error replacing the original file.");
        }
    }

    @Override
    public void incrementUpvotes() {
        upvotes++;
    }

    @Override
    public void incrementDownvotes() {
        downvotes++;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String getTitle() {
        return(title);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }
    @Override
    public boolean isSold() {
        return sold;
    }

    @Override
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public int getDownvotes() {
        return downvotes;
    }

    @Override
    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    // Getter and Setter for Category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Author: " + author +
                ", Title: " + title +
                ", Image Path: " + imagePath +
                ", Date: " + date +
                ", Category: " + category +
                ", Upvotes: " + upvotes +
                ", Downvotes: " + downvotes;
    }
    public static List<ItemListing> searchItems(String category, String keyword, boolean soldOnly) {
        List<ItemListing> results = new ArrayList<>();
        File file = new File("itemListing.txt");

        if (!file.exists()) {
            System.out.println("No item listings found.");
            return results;
        }

        keyword = keyword.toLowerCase();  // Make keyword case-insensitive

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 8) { // Ensure there are enough parts (including sold)
                    String author = parts[0];
                    String title = parts[1];
                    String imagePath = parts[2];
                    String date = parts[3];
                    String itemCategory = parts[4];
                    int upvotes = Integer.parseInt(parts[5]);
                    int downvotes = Integer.parseInt(parts[6]);
                    boolean sold = Boolean.parseBoolean(parts[7]);

                    // Check if the category matches (if provided) and if the keyword is found in title, author, or category
                    boolean categoryMatches = category == null || itemCategory.equalsIgnoreCase(category);
                    boolean keywordMatches = author.toLowerCase().contains(keyword) ||
                            title.toLowerCase().contains(keyword) ||
                            itemCategory.toLowerCase().contains(keyword);

                    // Check if we need to filter by sold status
                    boolean soldMatches = !soldOnly || sold;

                    // If all conditions match, add the item to the results
                    if (categoryMatches && keywordMatches && soldMatches) {
                        ItemListing item = new ItemListing();
                        item.setAuthor(author);
                        item.setTitle(title);
                        item.setImagePath(imagePath);
                        item.setDate(date);
                        item.setCategory(itemCategory);
                        item.upvotes = upvotes;
                        item.downvotes = downvotes;
                        item.sold = sold;

                        results.add(item);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error searching items: " + e.getMessage());
        }

        return results;
    }

    public void setUpvotes(int i) {
    }
}
