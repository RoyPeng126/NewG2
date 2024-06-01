import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoleSelectionPanel extends JPanel {
    public RoleSelectionPanel(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new GridLayout(2, 1));

        JButton sellerButton = new JButton("Seller");
        sellerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "seller");
            }
        });
        add(sellerButton);

        JButton buyerButton = new JButton("Buyer");
        buyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "buyer");
            }
        });
        add(buyerButton);
    }
}
