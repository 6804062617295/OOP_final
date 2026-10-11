package station;

import entity.GameObject;
import entity.Player;
import game.Assets;
import item.Item;

import java.awt.*;

/**
 * Base class for kitchen stations located on a grid tile.
 */
public abstract class Station extends GameObject {
    public static final int SIZE = 64;

    protected Item itemOnTop; // item lying on this station (can be null)

    private final String spriteName;
    private final Color color;
    private final String label;

    public Station(int col, int row, String spriteName, Color color, String label) {
        super(col * SIZE, row * SIZE, SIZE, SIZE);
        this.spriteName = spriteName;
        this.color = color;
        this.label = label;
    }

    /** Called when the player presses the interact key next to this station. */
    public abstract void interact(Player player);

    /** Called every frame. Most stations do nothing, some subclasses override it. */
    public void update(double dt) {
    }

    public Item getItemOnTop() {
        return itemOnTop;
    }

    public void setItemOnTop(Item item) {
        this.itemOnTop = item;
    }

    public int getCol() {
        return (int) (x / SIZE);
    }

    public int getRow() {
        return (int) (y / SIZE);
    }

    @Override
    public void draw(Graphics2D g) {
        Assets.draw(g, spriteName, (int) x, (int) y, width, height, color, label);
        if (itemOnTop != null) {
            itemOnTop.draw(g, (int) x + 16, (int) y + 12, 32);
        }
    }
}
