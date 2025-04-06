
public class RatingEntry {
    private String sellerID;
    private String buyerID;
    private double score;

    public RatingEntry(String sellerID, String buyerID, double score) {
        this.sellerID = sellerID;
        this.buyerID = buyerID;
        this.score = score;
    }

    public String getSellerID() { return sellerID; }
    public String getBuyerID() { return buyerID; }
    public double getScore() { return score; }
}
