import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
	private String currentUserID;
    // Method to check user credentials
    public boolean checkUser(String username, String password) {
        String query = "SELECT * FROM user WHERE studentID = ? AND password = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if a match is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method for a user to purchase a book
    public boolean purchaseBook(String userID, String bookID) {
        String query = "INSERT INTO purchases (userID, bookID) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, bookID);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to add a new user
    public boolean addUser(String username, String password) {
        String query = "INSERT INTO user (studentID, password) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to update the buyer ID for a book
    public boolean updateBuyerID(String bookID, String newBuyerID, String sellerID) {
        String query = "UPDATE book SET buyerID = ? WHERE bookID = ? AND sellerID = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newBuyerID);
            preparedStatement.setString(2, bookID);
            preparedStatement.setString(3, sellerID);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addBook(String bookName, String author, String edition, String sellerID) {
        String sql = "INSERT INTO book (bookname, author, edition, sellerID, buyerID, status) VALUES (?, ?, ?, ?, ?, 'Available')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookName);
            stmt.setString(2, author);
            stmt.setString(3, edition);
            stmt.setString(4, sellerID);
            stmt.setNull(5, java.sql.Types.INTEGER);  // Set buyerID as NULL
            stmt.executeUpdate();
            System.out.println("Book added successfully.");  // Debug output
            // Optionally, you can fetch and print the inserted record here for verification
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateBookStatus(String sellerID, String bookName) {
        String query = "UPDATE book SET status = 'Updated' WHERE sellerID = ? AND bookname = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, sellerID);
            preparedStatement.setString(2, bookName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Method to add a review for a book
    public boolean addReview(String bookID, String review) {
        String query = "INSERT INTO reviews (bookID, review) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookID);
            preparedStatement.setString(2, review);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public void setCurrentUserID(String userID) {
        this.currentUserID = userID;
    }
    // Placeholder method to get the current user ID
    public String getCurrentUserID() {
        return "sellerID";  // Replace with actual logic to retrieve current seller ID
    }
}