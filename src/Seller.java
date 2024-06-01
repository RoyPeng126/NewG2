public class Seller extends Person {
    private String storeName;

    public Seller(String id, String name, String storeName) {
        super(id, name);  // Calling the constructor of the Person class
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
