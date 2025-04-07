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
        } catch (IO Exception e){
            System.out.println("An error occurred while creating the itemListing" + e.getMessage());
        }
    }
    public ItemListing(){
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
    }

    @Override
    public void incrementUpvotes() {

    }

    @Override
    public void incrementDownvotes() {

    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public void setAuthor(String author) {

    }

    @Override
    public void getTitle() {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getImaagePath() {
        return "";
    }

    @Override
    public void setImaagePath(String imaagePath) {

    }

    @Override
    public String getDate() {
        return "";
    }

    @Override
    public void setDate(String date) {

    }

    @Override
    public int getDownvotes() {
        return 0;
    }

    @Override
    public void setDownvotes(int downvotes) {

    }
}
