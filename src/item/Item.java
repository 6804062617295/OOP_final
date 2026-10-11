package item;

import java.awt.*;

// Item that can be held or placed on stations
public interface Item {
    String getName();

    void draw(Graphics2D g, int x, int y, int size);
}
