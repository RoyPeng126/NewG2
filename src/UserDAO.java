import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class UserDAO {
    private String currentUserID;

    public UserDAO(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void addUser(String studentID, String password) {
        String query = "INSERT INTO user (studentID, password) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String studentID, String password) {
        String query = "SELECT * FROM user WHERE studentID = ? AND password = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    this.currentUserID = studentID;
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean purchaseBook(String studentID, String bookname) {
        if (isBookSold(bookname)) {
            System.out.println("Book already sold.");
            return false;
        }

        String query = "INSERT INTO purchases (userID, bookID) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, bookname);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                updateBookStatus(bookname, "Sold");
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isBookSold(String bookname) {
        String query = "SELECT status FROM book WHERE bookname = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return "Sold".equals(resultSet.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isBookPurchased(String bookname) {
        String query = "SELECT * FROM purchases WHERE bookID = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateBookStatus(String bookname, String status) {
        if (isBookPurchased(bookname)) {
            String query = "UPDATE book SET status = ? WHERE bookname = ?";
            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, status);
                preparedStatement.setString(2, bookname);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            
            JOptionPane.showMessageDialog(null, "Book has not been purchased yet. Status update not allowed.", "Status Update Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addReview(String buyerId, String sellerId, String comment, int rating, String bookname) {
        String query = "INSERT INTO review (buyerId, sellerId, comment, rating, bookID) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, buyerId);
            preparedStatement.setString(2, sellerId);
            preparedStatement.setString(3, comment);
            preparedStatement.setInt(4, rating);
            preparedStatement.setString(5, bookname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Review> getReviewsForSeller(String sellerId) {
        String query = "SELECT * FROM review WHERE sellerId = ?";
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sellerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String buyerId = resultSet.getString("buyerId");
                    String comment = resultSet.getString("comment");
                    String rating = resultSet.getString("rating");
                    String bookname = resultSet.getString("bookID");
                    reviews.add(new Review(buyerId, sellerId, comment, rating, bookname));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public List<Review> getReviewsForBook(String bookname) {
        String query = "SELECT * FROM review WHERE bookID = ?";
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookname);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String buyerId = resultSet.getString("buyerId");
                    String sellerId = resultSet.getString("sellerId");
                    String comment = resultSet.getString("comment");
                    String rating = resultSet.getString("rating");
                    reviews.add(new Review(buyerId, sellerId, comment, rating, bookname));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public void addBook(String bookName, String author, String edition, String sellerID) {
        String query = "INSERT INTO book (bookname, author, edition, sellerID, status) VALUES (?, ?, ?, ?, 'Available')";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookName);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, edition);
            preparedStatement.setString(4, sellerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
