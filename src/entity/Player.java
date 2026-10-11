package entity;

import game.Assets;
import item.Item;

import java.awt.*;
import java.util.List;

// Chef character controlled by the player
public class Player extends GameObject {
    private static final int SIZE = 48;

    private double speed = 220; // pixels per second
    private Item heldItem;      // null = empty hands
    private String name = "Chef";
    private Color color = new Color(220, 70, 70); // Default Red Chef

    public Player(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    public Player(double x, double y, String name, Color color) {
        super(x, y, SIZE, SIZE);
        this.name = name;
        this.color = color;
    }

    // Moves player and handles wall sliding
    public void move(double dirX, double dirY, double dt,
                     List<? extends GameObject> obstacles, Rectangle worldBounds) {
        double oldX = x;
        x += dirX * getSpeed() * dt;
        if (isBlocked(obstacles, worldBounds)) {
            x = oldX;
        }

        double oldY = y;
        y += dirY * getSpeed() * dt;
        if (isBlocked(obstacles, worldBounds)) {
            y = oldY;
        }
    }

    private boolean isBlocked(List<? extends GameObject> obstacles, Rectangle worldBounds) {
        Rectangle me = getBounds();
        if (!worldBounds.contains(me)) {
            return true;
        }
        for (GameObject obstacle : obstacles) {
            if (me.intersects(obstacle.getBounds())) {
                return true;
            }
        }
        return false;
    }

    // ----- Holding items -----

    public boolean hasItem() {
        return heldItem != null;
    }

    public Item getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(Item item) {
        this.heldItem = item;
    }

    // Takes and removes held item
    public Item takeHeldItem() {
        Item item = heldItem;
        heldItem = null;
        return item;
    }

    public void increaseSpeed(double amount) {
        speed += amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void draw(Graphics2D g) {
        int drawW = 50;
        int drawH = 56;
        int drawX = (int) x - (drawW - width) / 2;
        int drawY = (int) y - (drawH - height);
        Assets.draw(g, "player", drawX, drawY, drawW, drawH, color, name);

        // Show the carried item above the chef's head
        if (heldItem != null) {
            heldItem.draw(g, (int) x + (width - 28) / 2, drawY - 22, 28);
        }
    }
}
