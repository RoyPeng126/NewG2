import javax.swing.*;
import java.awt.*;

public class BasePanel extends JPanel {
    protected JButton createButton(String text, String toolTip, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setToolTipText(toolTip);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(textColor);
        return button;
    }
}
