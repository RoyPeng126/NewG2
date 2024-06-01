import javax.swing.*;

public class SellerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}