package item;

import game.Assets;

import java.awt.*;

/**
 * A fire extinguisher that the chef can carry to put out kitchen fires.
 */
public class FireExtinguisher implements Item {

    @Override
    public String getName() {
        return "Fire Extinguisher";
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int size) {
        // Try drawing sprite if available, otherwise draw detailed fallback
        java.awt.image.BufferedImage img = Assets.get("extinguisher");
        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
            return;
        }

        // Fallback drawing: Red cylinder extinguisher with yellow band and nozzle
        int w = (int) (size * 0.55);
        int h = (int) (size * 0.85);
        int ox = x + (size - w) / 2;
        int oy = y + (size - h) / 2 + 2;

        // Tank body
        g.setColor(new Color(210, 40, 40));
        g.fillRoundRect(ox, oy, w, h, 8, 8);
        g.setColor(new Color(150, 20, 20));
        g.drawRoundRect(ox, oy, w, h, 8, 8);

        // Center stripe
        g.setColor(new Color(245, 215, 60));
        g.fillRect(ox + 1, oy + h / 3, w - 2, h / 5);

        // Black top cap and handle
        g.setColor(new Color(40, 40, 40));
        g.fillRect(ox + w / 4, oy - 4, w / 2, 5);
        g.fillRect(ox + w / 2 - 1, oy - 7, 3, 4);

        // Black hose on the side
        g.drawArc(ox + w - 4, oy - 2, 8, 14, 0, 180);
    }
}
