import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPanel extends BasePanel {
    private UserDAO userDAO;
    private JTextField userIDField;
    private JPasswordField passwordField;

    public RegistrationPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userIDLabel = new JLabel("User ID:");
        userIDField = new JTextField();
        formPanel.add(userIDLabel);
        formPanel.add(userIDField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton registerButton = createButton("Register", "Create a new account", Color.BLACK);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());
                userDAO.addUser(userID, password);
                JOptionPane.showMessageDialog(RegistrationPanel.this, "Registration successful!");
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(RegistrationPanel.this);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new LoginPanel(userDAO));
                frame.revalidate();
            }
        });
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

   
}
