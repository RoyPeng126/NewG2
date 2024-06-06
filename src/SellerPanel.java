import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class SellerPanel extends BasePanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private String sellerID;

    public SellerPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.sellerID = userDAO.getCurrentUserID();  // Get current logged-in seller ID

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Books");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
   
        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Edition");
        tableModel.addColumn("Seller ID");
        tableModel.addColumn("Buyer ID");
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

        JButton addBookButton = createButton("Add Book", "Add a new book", Color.BLACK);
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddBookDialog();
            }
        });
        buttonPanel.add(addBookButton);

        JButton updateStatusButton = createButton("Update Status", "Update the status of the selected book", Color.BLACK);
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookName = (String) tableModel.getValueAt(selectedRow, 0); // Use bookName to identify the book
                    userDAO.updateBookStatus(bookName, "Sold");
                    JOptionPane.showMessageDialog(SellerPanel.this, "Status updated!");
                    loadBooks();  // Reload books to show the updated status
                } else {
                    JOptionPane.showMessageDialog(SellerPanel.this, "Please select a book to update.");
                }
            }
        });
        buttonPanel.add(updateStatusButton);
        
        JButton viewReviewsButton = createButton("View Reviews", "View reviews for the selected book", Color.BLACK);
        viewReviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReviewsDialog();
            }
        });
        buttonPanel.add(viewReviewsButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();
    }

    private void loadBooks() {
        tableModel.setRowCount(0);  // Clear existing rows

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM book WHERE sellerID = ?")) {
            stmt.setString(1, sellerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("bookname"));
                row.add(rs.getString("author"));
                row.add(rs.getString("edition"));
                row.add(rs.getString("sellerID"));
                row.add(rs.getString("buyerID"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddBookDialog() {
        JFrame addBookFrame = new JFrame("Add Book");
        addBookFrame.setSize(300, 250);
        addBookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel addBookPanel = new JPanel(new GridLayout(4, 2));

        JLabel bookNameLabel = new JLabel("Book Name:");
        JTextField bookNameField = new JTextField();
        addBookPanel.add(bookNameLabel);
        addBookPanel.add(bookNameField);

        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        addBookPanel.add(authorLabel);
        addBookPanel.add(authorField);

        JLabel editionLabel = new JLabel("Edition:");
        JTextField editionField = new JTextField();
        addBookPanel.add(editionLabel);
        addBookPanel.add(editionField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = bookNameField.getText();
                String author = authorField.getText();
                String edition = editionField.getText();
                userDAO.addBook(bookName, author, edition, sellerID);
                JOptionPane.showMessageDialog(addBookFrame, "Book added!");
                addBookFrame.dispose();
                loadBooks();  // Reload books to show the new book
            }
        });
        addBookPanel.add(submitButton);

        addBookFrame.add(addBookPanel);
        addBookFrame.setVisible(true);
    }

    private void openReviewsDialog() {
        JFrame reviewsFrame = new JFrame("View Reviews");
        reviewsFrame.setSize(400, 300);
        reviewsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel reviewsTableModel = new DefaultTableModel();
        reviewsTableModel.addColumn("Book Name");
        reviewsTableModel.addColumn("User ID");
        reviewsTableModel.addColumn("Review Text");
        reviewsTableModel.addColumn("Rating");
        JTable reviewsTable = new JTable(reviewsTableModel);
        JScrollPane scrollPane = new JScrollPane(reviewsTable);
        reviewsFrame.add(scrollPane, BorderLayout.CENTER);

        List<Review> reviews = userDAO.getReviewsForSeller(sellerID);
        for (Review review : reviews) {
            Vector<String> row = new Vector<>();
            row.add(review.getBookname());
            row.add(review.getBuyerId());
            row.add(review.getComment());
            row.add(review.getRating());
            reviewsTableModel.addRow(row);
        }

        reviewsFrame.setVisible(true);
    }
}
