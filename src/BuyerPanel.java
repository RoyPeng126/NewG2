import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class BuyerPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;

    public BuyerPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Edition");
        tableModel.addColumn("Seller ID");

        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String sellerID = (String) tableModel.getValueAt(selectedRow, 3);
                    String bookName = (String) tableModel.getValueAt(selectedRow, 0);
                    userDAO.purchaseBook(sellerID, bookName);
                    JOptionPane.showMessageDialog(BuyerPanel.this, "Purchase successful!");
                    String buyerID = userDAO.getCurrentUserID();  // get the current logged in user ID
                    userDAO.updateBuyerID(sellerID, bookName, buyerID);
                    openReviewDialog(sellerID);
                } else {
                    JOptionPane.showMessageDialog(BuyerPanel.this, "Please select a book to buy.");
                }
            }
        });
        add(buyButton, BorderLayout.SOUTH);

        loadBooks();
    }

    private void loadBooks() {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("bookname"));
                row.add(rs.getString("author"));
                row.add(rs.getString("edition"));
                row.add(rs.getString("sellerID"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openReviewDialog(String sellerID) {
        JFrame reviewFrame = new JFrame("Leave a Review");
        reviewFrame.setSize(300, 200);
        reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel reviewPanel = new JPanel(new GridLayout(2, 1));
        
        JTextArea reviewTextArea = new JTextArea();
        reviewPanel.add(new JScrollPane(reviewTextArea));
        
        JButton submitReviewButton = new JButton("Submit Review");
        submitReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reviewText = reviewTextArea.getText();
                userDAO.addReview(sellerID, reviewText);
                JOptionPane.showMessageDialog(reviewFrame, "Review submitted!");
                reviewFrame.dispose();
            }
        });
        reviewPanel.add(submitReviewButton);
        
        reviewFrame.add(reviewPanel);
        reviewFrame.setVisible(true);
    }
}