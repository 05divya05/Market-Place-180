import java.util.List;

public class RatingManager {

    private final Client client;

    public RatingManager(Client client) {
        this.client = client;
    }

    public boolean addRating(String buyer, String seller, double score) {
        client.send("ADD_RATING|" + buyer + "|" + seller + "|" + score);
        return "SUCCESS".equals(client.read());
    }

    public double getAverage(String seller) {
        client.send("GET_RATING|" + seller);
        String resp = client.read();
        return "NONE".equals(resp) ? -1 : Double.parseDouble(resp);
    }

}
