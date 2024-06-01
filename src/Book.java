public class Book {
    private int id;
    private String name;
    private String author;
    private String edition;
    private String sellerID;
    private String buyerID;

    public Book(int id, String name, String author, String edition, String sellerID, String buyerID) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.edition = edition;
        this.sellerID = sellerID;
        this.buyerID = buyerID;
    }

    public Book(String name, String author, String edition, String sellerID, String buyerID) {
        this.name = name;
        this.author = author;
        this.edition = edition;
        this.sellerID = sellerID;
        this.buyerID = buyerID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getEdition() {
        return edition;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getBuyerID() {
        return buyerID;
    }
}