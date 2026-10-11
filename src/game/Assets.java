package game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Loads images from the assets folder with fallback placeholders
public class Assets {
    private static final String FOLDER = "assets/";

    // Remembers images we already loaded. A null value means "file not found".
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    private Assets() {
        // Utility class: no objects needed
    }

    // Returns loaded image or null
    public static BufferedImage get(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        BufferedImage image = null;
        File file = findAssetFile(name);
        if (file != null) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                System.out.println("Could not read " + file.getPath());
            }
        }
        cache.put(name, image);
        return image;
    }

    private static File findAssetFile(String name) {
        String[] dirs = {FOLDER, "../" + FOLDER};
        String target = name + ".png";
        for (String dir : dirs) {
            File direct = new File(dir, target);
            if (direct.exists()) {
                return direct;
            }
            File folder = new File(dir);
            if (folder.isDirectory()) {
                File[] list = folder.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (f.getName().equalsIgnoreCase(target)) {
                            return f;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Draws sprite or fallback box
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

    // Draws tile or fallback color
    public static void drawTile(Graphics2D g, String name, int x, int y, int size, Color fallbackColor) {
        BufferedImage image = get(name);
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            g.setColor(fallbackColor);
            g.fillRect(x, y, size, size);
        }
    }

    // Draws centered text
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
