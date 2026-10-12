package game;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Loads images and gif animations from the assets folder with fallback placeholders
public class Assets {
    private static final String FOLDER = "assets/";

    // Remembers images we already loaded. A null value means "file not found".
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static final Map<String, BufferedImage[]> animCache = new HashMap<>();

    private Assets() {
        // Utility class: no objects needed
    }

    // Returns loaded single image or first frame of gif
    public static BufferedImage get(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        File file = findAssetFile(name);
        if (file != null && file.getName().toLowerCase().endsWith(".gif")) {
            BufferedImage[] frames = getFrames(name);
            return (frames != null && frames.length > 0) ? frames[0] : null;
        }

        BufferedImage image = null;
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

    // Returns all frames of an animated gif
    public static BufferedImage[] getFrames(String name) {
        if (animCache.containsKey(name)) {
            return animCache.get(name);
        }

        File file = findAssetFile(name);
        if (file != null && file.getName().toLowerCase().endsWith(".gif")) {
            try (ImageInputStream stream = ImageIO.createImageInputStream(file)) {
                Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(stream);
                    int count = reader.getNumImages(true);
                    if (count > 0) {
                        BufferedImage[] frames = new BufferedImage[count];
                        for (int i = 0; i < count; i++) {
                            frames[i] = reader.read(i);
                        }
                        animCache.put(name, frames);
                        cache.put(name, frames[0]);
                        return frames;
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not load GIF frames for " + file.getPath());
            }
        }

        // Fallback to single static image if not a multi-frame gif
        BufferedImage single = get(name);
        if (single != null) {
            BufferedImage[] frames = new BufferedImage[]{single};
            animCache.put(name, frames);
            return frames;
        }

        animCache.put(name, null);
        return null;
    }

    // Returns the current frame based on elapsed animation time
    public static BufferedImage getFrame(String name, double animTime) {
        BufferedImage[] frames = getFrames(name);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int idx = (int) Math.floor(animTime) % frames.length;
        if (idx < 0) {
            idx = (idx % frames.length + frames.length) % frames.length;
        }
        return frames[idx];
    }

    private static File findAssetFile(String name) {
        String[] dirs = {FOLDER, "../" + FOLDER};
        String[] exts = {".gif", ".png", ".jpg", ".jpeg"};
        for (String dir : dirs) {
            for (String ext : exts) {
                File direct = new File(dir, name + ext);
                if (direct.exists()) {
                    return direct;
                }
            }
            File folder = new File(dir);
            if (folder.isDirectory()) {
                File[] list = folder.listFiles();
                if (list != null) {
                    for (File f : list) {
                        for (String ext : exts) {
                            if (f.getName().equalsIgnoreCase(name + ext)) {
                                return f;
                            }
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
