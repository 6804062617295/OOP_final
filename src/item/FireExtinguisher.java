package item;

import game.Assets;

import java.awt.*;

// Fire extinguisher item
public class FireExtinguisher implements Item {

    @Override
    public String getName() {
        return "Fire Extinguisher";
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int size) {
        java.awt.image.BufferedImage img = Assets.get("extinguisher");
        if (img != null) {
            g.drawImage(img, x, y, size, size, null);
        }
    }
}
