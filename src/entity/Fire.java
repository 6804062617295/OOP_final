package entity;

import game.Assets;
import station.Station;

import java.awt.*;

// Fire hazard that blocks movement until put out
public class Fire extends GameObject {
    private static final int SIZE = Station.SIZE;

    private final double maxHealth = 1.6; // seconds of extinguishing needed
    private double health = maxHealth;
    private double animTime = 0;

    public Fire(int col, int row) {
        super(col * SIZE, row * SIZE, SIZE, SIZE);
    }

    public void update(double dt) {
        animTime += dt * 8.0;
    }

    // Extinguishes fire over time
    public boolean extinguish(double dt) {
        health -= dt;
        return health <= 0;
    }

    public boolean isExtinguished() {
        return health <= 0;
    }

    public int getCol() {
        return (int) (x / SIZE);
    }

    public int getRow() {
        return (int) (y / SIZE);
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealthRatio() {
        return Math.max(0, health / maxHealth);
    }

    @Override
    public void draw(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;

        // Danger tile background warning
        g.setColor(new Color(180, 40, 20, 50));
        g.fillRoundRect(px + 4, py + 4, width - 8, height - 8, 8, 8);

        // Animated GIF sprite (fire.gif)
        java.awt.image.BufferedImage img = Assets.getFrame("fire", animTime);
        if (img == null) {
            img = Assets.get("fire");
        }

        if (img != null) {
            g.drawImage(img, px, py, width, height, null);
        }

        // Extinguish health bar if taking damage
        if (health < maxHealth) {
            int bx = (int) x + 8;
            int by = (int) y + 2;
            int bw = width - 16;
            g.setColor(new Color(30, 30, 30, 200));
            g.fillRect(bx - 1, by - 1, bw + 2, 7);
            g.setColor(new Color(70, 170, 255));
            g.fillRect(bx, by, (int) (bw * (1.0 - getHealthRatio())), 5);
        }
    }
}
