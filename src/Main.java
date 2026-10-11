import game.GamePanel;

import javax.swing.*;

// Main entry point
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hell's Kitchen - Restaurant Rush");
            GamePanel panel = new GamePanel();

            frame.add(panel);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            panel.start();
        });
    }
}
