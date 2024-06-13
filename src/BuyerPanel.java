import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class BuyerPanel extends BasePanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;

    public BuyerPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Available Books");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Edition");
        tableModel.addColumn("Seller ID");
        tableModel.addColumn("Status");

        bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton buyButton = createButton("Buy", "Purchase the selected book", Color.BLACK);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String sellerID = (String) tableModel.getValueAt(selectedRow, 3);
                    String bookName = (String) tableModel.getValueAt(selectedRow, 0);
                    String buyerID = userDAO.getCurrentUserID();  

                    if (userDAO.purchaseBook(buyerID, bookName)) {
                        JOptionPane.showMessageDialog(BuyerPanel.this, "Purchase successful!");
                        openReviewDialog(bookName, sellerID, buyerID);
                        loadBooks();  
                    } else {
                        JOptionPane.showMessageDialog(BuyerPanel.this, "This book has already been sold.");
                    }
                } else {
                    JOptionPane.showMessageDialog(BuyerPanel.this, "Please select a book to buy.");
                }
            }
        });
        buttonPanel.add(buyButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();
    }

    private void loadBooks() {
        tableModel.setRowCount(0);  

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book WHERE status = 'Available'")) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("bookname"));
                row.add(rs.getString("author"));
                row.add(rs.getString("edition"));
                row.add(rs.getString("sellerID"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openReviewDialog(String bookName, String sellerID, String buyerID) {
        JFrame reviewFrame = new JFrame("Leave a Review");
        reviewFrame.setSize(400, 300);
        reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel reviewPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        reviewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel commentLabel = new JLabel("Comment:");
        JTextArea commentTextArea = new JTextArea();
        commentTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        reviewPanel.add(commentLabel);
        reviewPanel.add(new JScrollPane(commentTextArea));

        JLabel ratingLabel = new JLabel("Rating (1-5):");
        JTextField ratingField = new JTextField();
        reviewPanel.add(ratingLabel);
        reviewPanel.add(ratingField);

        JButton submitReviewButton = createButton("Submit Review", "Submit your review for the book", Color.BLACK);
        submitReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentTextArea.getText();
                int rating = Integer.parseInt(ratingField.getText());
                userDAO.addReview(buyerID, sellerID, comment, rating, bookName);
                JOptionPane.showMessageDialog(reviewFrame, "Review submitted!");
                reviewFrame.dispose();
            }
        });
        reviewPanel.add(submitReviewButton);

        reviewFrame.add(reviewPanel);
        reviewFrame.setVisible(true);
    }
}
