import java.io.*;

public class ItemListing implements ItemListingInterface, Serializable {
    private String author;
    private String title;
    private String imagePath;
    private String date;
    private int upvotes;
    private int downvotes;

    public ItemListing(String author, String title, String imagePath, String date) {
        this.author = author;
        this.title = title;
        this.imagePath = imagePath;
        this.date = date;
        this.upvotes = 0;
        this.downvotes = 0;

        try (PrintWriter writer = new PrintWriter(new FileWriter("itemListing.txt", true))) {
            writer.println(author + "," + title + "," + imagePath + "," + date + "," + upvotes + "," + downvotes);
        } catch (IOException e){
            System.out.println("An error occurred while creating the itemListing" + e.getMessage());
        }
    }

        public static void deletePost(String title){
            System.out.println("Deleting post " + title);

            File originalFile = new File("itemListing.txt");
            File tempFile = new File("tempFile.txt");

            try (
                    BufferedReader readPost = new BufferedReader( new FileReader(originalFile));
                    PrintWriter tempWrite = new PrintWriter(new FileWriter (tempFile))
            ) {
                String line;

                while ((line = readPost.readLine()) != null) {

                    String[] parts = line.split(",");
                    if(parts[1].equals(title)) {
                        continue;
                    }
                    tempWrite.println(line);
                }
                } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (!tempFile.renameTo(originalFile)) {
                System.out.println("Error writing to original file");
            }
        }


    @Override
    public void incrementUpvotes() {
        this.upvotes++;
    }

    @Override
    public void incrementDownvotes() {
        this.downvotes++;
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
        return title;
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
    public int getDownvotes() {
        return downvotes;
    }

    @Override
    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
}
