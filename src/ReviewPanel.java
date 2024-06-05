import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class ReviewPanel extends JPanel {
    private UserDAO userDAO;
    private String bookID;

    public ReviewPanel(UserDAO userDAO, String bookID) {
        this.userDAO = userDAO;
        this.bookID = bookID;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Book Reviews");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Reviews Table
        DefaultTableModel reviewsTableModel = new DefaultTableModel();
        reviewsTableModel.addColumn("Buyer ID");
        reviewsTableModel.addColumn("Seller ID");
        reviewsTableModel.addColumn("Comment");
        reviewsTableModel.addColumn("Rating");

        JTable reviewsTable = new JTable(reviewsTableModel);
        reviewsTable.setFillsViewportHeight(true);
        reviewsTable.setRowHeight(30);
        reviewsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(reviewsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        loadReviews(reviewsTableModel);
    }

    private void loadReviews(DefaultTableModel tableModel) {
        List<Review> reviews = userDAO.getReviewsForBook(bookID);
        for (Review review : reviews) {
            Vector<String> row = new Vector<>();
            row.add(review.getBuyerId());
            row.add(review.getSellerId());
            row.add(review.getComment());
            row.add(String.valueOf(review.getRating()));
            tableModel.addRow(row);
        }
    }
}
