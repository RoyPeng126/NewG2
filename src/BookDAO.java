import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(Book book) {
        String query = "INSERT INTO book (bookname, author, edition, sellerID, buyerID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, book.getName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getEdition());
            stmt.setString(4, book.getSellerID());
            stmt.setString(5, book.getBuyerID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book(
                    rs.getString("bookname"),
                    rs.getString("author"),
                    rs.getString("edition"),
                    rs.getString("sellerID"),
                    rs.getString("buyerID")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void updateBookBuyer(String bookname, String buyerID) {
        String query = "UPDATE book SET buyerID = ? WHERE bookname = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, buyerID);
            stmt.setString(2, bookname);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}