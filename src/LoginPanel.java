import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel, UserDAO userDAO) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.userDAO = userDAO;

        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        add(usernameLabel);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (userDAO.checkUser(username, password)) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Login successful");
                    cardLayout.show(mainPanel, "roleSelection");
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password. Please try again.");
                }
            }
        });
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                userDAO.addUser(username, password);
                JOptionPane.showMessageDialog(LoginPanel.this, "Registration successful. You can now log in.");
            }
        });
        add(registerButton);
    }
}