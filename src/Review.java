public class Review {
    private String buyerId;
    private String sellerId;
    private String comment;
    private String rating;
    private String bookname;

    public Review(String buyerId, String sellerId, String comment, String rating, String bookname) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.comment = comment;
        this.rating = rating;
        this.bookname = bookname;
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

    public String getRating() {
        return rating;
    }

    public String getBookname() {
        return bookname;
    }
}
