package entity;

import java.awt.*;

// Base class for game objects with position and size
public abstract class GameObject {
    protected double x;
    protected double y;
    protected final int width;
    protected final int height;

    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Draw object
    public abstract void draw(Graphics2D g);

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }

    // Distance between two objects
    public double distanceTo(GameObject other) {
        return Math.hypot(getCenterX() - other.getCenterX(), getCenterY() - other.getCenterY());
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
