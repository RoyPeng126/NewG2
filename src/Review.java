public class Review {
    private String buyerId;
    private String sellerId;
    private String comment;
    private int rating;

    public Review(String buyerId, String sellerId, String comment, int rating) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
