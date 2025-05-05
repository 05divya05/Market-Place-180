import java.util.List;

/**
 * Rating Manager
 *
 * Handles sending and retrieving seller ratings via the server.
 * Provides methods to submit a buyer's rating for a seller and to fetch
 * the seller's average score.
 *
 * @version May 3, 2025
 * @author Yihang Li, Divya Vemireddy, Sultan Al Qahtani, Jay Nitz, Steven Win
 */
public class RatingManager {

    /** Client used for network communication with the server. */
    private final Client client;

    /**
     * Constructs a RatingManager with the given Client.
     * @param client network client for sending/receiving rating commands
     */
    public RatingManager(Client client) {
        this.client = client;
    }

    /**
     * Sends an ADD_RATING command to the server.
     * @param buyer  username of the buyer giving the rating
     * @param seller username of the seller being rated
     * @param score  rating score (e.g., 0.0 to 5.0)
     * @return true if server responds with SUCCESS, false otherwise
     */
    public boolean addRating(String buyer, String seller, double score) {
        client.send("ADD_RATING|" + buyer + "|" + seller + "|" + score);
        String response = client.read();
        return "SUCCESS".equals(response);
    }

    /**
     * Requests the average rating for a seller from the server.
     * @param seller username of the seller
     * @return the average rating, or -1 if there is no rating yet
     */
    public double getAverage(String seller) {
        client.send("GET_RATING|" + seller);
        String resp = client.read();
        // explanation: server returns "NONE" if no ratings exist
        return "NONE".equals(resp) ? -1 : Double.parseDouble(resp);
    }

}
