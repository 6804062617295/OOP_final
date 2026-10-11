package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads sprite images from the "assets" folder (next to the "src" folder).
 *
 * Example: Assets.get("player") loads "assets/player.png".
 * If a file is missing, a colored placeholder box is drawn instead,
 * so the game always runs even before you add your own sprites.
 */
public class Assets {
    private static final String FOLDER = "assets/";

    // Remembers images we already loaded. A null value means "file not found".
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    private Assets() {
        // Utility class: no objects needed
    }

    /** Returns the image for this name, or null if there is no such file. */
    public static BufferedImage get(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        BufferedImage image = null;
        File file = new File(FOLDER + name + ".png");
        if (file.exists()) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                System.out.println("Could not read " + file.getPath());
            }
        }
        cache.put(name, image);
        return image;
    }

    /** Draws a sprite, or a colored box with a label if the sprite is missing. */
    public static void draw(Graphics2D g, String name, int x, int y, int w, int h,
                            Color fallbackColor, String label) {
        BufferedImage image = get(name);
        if (image != null) {
            g.drawImage(image, x, y, w, h, null);
            return;
        }

        // Placeholder box
        g.setColor(fallbackColor);
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setColor(fallbackColor.darker());
        g.drawRoundRect(x, y, w - 1, h - 1, 10, 10);

        if (label != null && !label.isEmpty()) {
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, h / 5)));
            g.setColor(isBright(fallbackColor) ? Color.BLACK : Color.WHITE);
            // Big boxes: label at the bottom (so items on top stay visible). Small boxes: centered.
            int textY = (h >= 48) ? y + h - 10 : y + h / 2;
            drawCenteredText(g, label, x + w / 2, textY);
        }
    }

    /** Draws a floor tile, or a plain colored square if the sprite is missing. */
    public static void drawTile(Graphics2D g, String name, int x, int y, int size, Color fallbackColor) {
        BufferedImage image = get(name);
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            g.setColor(fallbackColor);
            g.fillRect(x, y, size, size);
        }
    }

    /** Draws text so that its center is at (centerX, centerY). */
    public static void drawCenteredText(Graphics2D g, String text, int centerX, int centerY) {
        FontMetrics fm = g.getFontMetrics();
        int textX = centerX - fm.stringWidth(text) / 2;
        int textY = centerY + (fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, textX, textY);
    }

    private static boolean isBright(Color c) {
        int brightness = (c.getRed() * 299 + c.getGreen() * 587 + c.getBlue() * 114) / 1000;
        return brightness > 140;
    }
}
