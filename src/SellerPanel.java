import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class SellerPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private String sellerID;

    public SellerPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.sellerID = userDAO.getCurrentUserID();  // Get current logged-in seller ID

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Book Name");
        tableModel.addColumn("Author");
        tableModel.addColumn("Edition");
        tableModel.addColumn("SellerID");
        tableModel.addColumn("BuyerID");
        tableModel.addColumn("Status");

        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddBookDialog();
            }
        });
        buttonPanel.add(addBookButton);

        JButton checkMyBooksButton = new JButton("Check My Books");
        checkMyBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });
        buttonPanel.add(checkMyBooksButton);

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookName = (String) tableModel.getValueAt(selectedRow, 0);
                    userDAO.updateBookStatus(sellerID, bookName);
                    JOptionPane.showMessageDialog(SellerPanel.this, "Status updated!");
                    loadBooks();  // Reload books to show the updated status
                } else {
                    JOptionPane.showMessageDialog(SellerPanel.this, "Please select a book to update.");
                }
            }
        });
        buttonPanel.add(updateStatusButton);

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

        JPanel addBookPanel = new JPanel(new GridLayout(5, 2));

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

        JLabel sellerIDLabel = new JLabel("Seller ID:");
        JTextField sellerIDField = new JTextField();
        addBookPanel.add(sellerIDLabel);
        addBookPanel.add(sellerIDField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookName = bookNameField.getText();
                String author = authorField.getText();
                String edition = editionField.getText();
                String sellerID = sellerIDField.getText();
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
}
