package entity;

import game.Assets;
import station.Station;

import java.awt.*;

/**
 * A hell fire hazard burning on a kitchen tile. Blocks path until extinguished.
 */
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

    /**
     * Sprays fire extinguisher on this fire.
     * @return true if fire is completely extinguished.
     */
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
        // Try drawing sprite if available
        java.awt.image.BufferedImage img = Assets.get("fire");
        if (img != null) {
            g.drawImage(img, (int) x, (int) y, width, height, null);
        } else {
            // Animated flickering procedural fire
            int px = (int) x;
            int py = (int) y;

            // Danger tile background
            g.setColor(new Color(180, 40, 20, 60));
            g.fillRoundRect(px + 4, py + 4, width - 8, height - 8, 8, 8);

            // Outer red flame tongues
            g.setColor(new Color(230, 40, 20, 220));
            int wave1 = (int) (Math.sin(animTime) * 4);
            int wave2 = (int) (Math.cos(animTime * 1.3) * 4);
            g.fillOval(px + 10 + wave1, py + 16, width - 20, height - 22);

            // Middle orange flame
            g.setColor(new Color(255, 130, 20, 230));
            g.fillOval(px + 16 - wave2, py + 22, width - 32, height - 30);

            // Inner yellow core
            g.setColor(new Color(255, 220, 50, 240));
            g.fillOval(px + 22 + wave2 / 2, py + 28, width - 44, height - 38);

            // White-hot center
            g.setColor(new Color(255, 255, 200, 220));
            g.fillOval(px + 26, py + 34, width - 52, height - 46);
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
