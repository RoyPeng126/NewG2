import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends BasePanel {
    private UserDAO userDAO;
    private JTextField userIDField;
    private JPasswordField passwordField;

    public LoginPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

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

        JButton loginButton = createButton("Login", "Login with your credentials", Color.BLACK);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());
                if (userDAO.validateUser(userID, password)) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!");
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new RoleSelectionPanel(userDAO));
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid userID or password.");
                }
            }
        });
        buttonPanel.add(loginButton);

        JButton registerButton = createButton("Register", "Create a new account", Color.BLACK);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginPanel.this);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new RegistrationPanel(userDAO));
                frame.revalidate();
            }
        });
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

   
}
