import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserDAO userDAO;

    public MainFrame() {
        setTitle("Seller Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userDAO = new UserDAO();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(cardLayout, mainPanel, userDAO);
        mainPanel.add(loginPanel, "login");

        RoleSelectionPanel roleSelectionPanel = new RoleSelectionPanel(cardLayout, mainPanel);
        mainPanel.add(roleSelectionPanel, "roleSelection");

        SellerPanel sellerPanel = new SellerPanel(userDAO);
        mainPanel.add(sellerPanel, "seller");

        BuyerPanel buyerPanel = new BuyerPanel(userDAO);
        mainPanel.add(buyerPanel, "buyer");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }
}