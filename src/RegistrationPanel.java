import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel {
    private JTextField studentIDField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private UserDAO userDAO;

    public RegistrationPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new GridLayout(3, 2));

        JLabel studentIDLabel = new JLabel("Student ID:");
        add(studentIDLabel);

        studentIDField = new JTextField();
        add(studentIDField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField();
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = studentIDField.getText();
                String password = new String(passwordField.getPassword());
                userDAO.addUser(studentID, password);
                JOptionPane.showMessageDialog(RegistrationPanel.this, "Registration successful!");
                clearFields();
            }
        });
        add(registerButton);
    }

    private void clearFields() {
        studentIDField.setText("");
        passwordField.setText("");
    }
}