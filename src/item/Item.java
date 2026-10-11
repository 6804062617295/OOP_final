package item;

import java.awt.*;

/**
 * Portable item that can be carried by a player or placed on stations.
 */
public interface Item {
    String getName();

    void draw(Graphics2D g, int x, int y, int size);
}
