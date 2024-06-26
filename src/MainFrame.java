import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private UserDAO userDAO;

    public MainFrame() {
        userDAO = new UserDAO(null); 
        setTitle("Second-Hand Trading Platform");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        LoginPanel loginPanel = new LoginPanel(userDAO);
        add(loginPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
