import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoleSelectionPanel extends BasePanel {
    private UserDAO userDAO;
    private String selectedRole;

    public RoleSelectionPanel(UserDAO userDAO) {
        this.userDAO = userDAO;
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Select Your Role");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel rolePanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rolePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JRadioButton buyerRadioButton = new JRadioButton("Buyer");
        buyerRadioButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        buyerRadioButton.setActionCommand("Buyer");
        JRadioButton sellerRadioButton = new JRadioButton("Seller");
        sellerRadioButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        sellerRadioButton.setActionCommand("Seller");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(buyerRadioButton);
        roleGroup.add(sellerRadioButton);

        rolePanel.add(buyerRadioButton);
        rolePanel.add(sellerRadioButton);

        add(rolePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton confirmButton = createButton("Confirm", "Confirm your role selection", Color.BLACK);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedRole = roleGroup.getSelection().getActionCommand();
                JOptionPane.showMessageDialog(RoleSelectionPanel.this, "Role selected: " + selectedRole);
                
                if (selectedRole.equals("Buyer")) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(RoleSelectionPanel.this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new BuyerPanel(userDAO));
                    frame.revalidate();
                } else if (selectedRole.equals("Seller")) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(RoleSelectionPanel.this);
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(new SellerPanel(userDAO));
                    frame.revalidate();
                }
            }
        });
        buttonPanel.add(confirmButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

   
}
