package station;

import entity.Player;
import game.SoundManager;
import item.Ingredient;
import item.Item;
import item.Plate;

import java.awt.*;

// Station that processes ingredients over time (chopping, cooking)
public abstract class ProcessingStation extends Station {
    protected double progress;          // seconds of work done
    protected final double processTime; // seconds needed to finish
    private double speed = 1.0;         // upgrades make this bigger

    public ProcessingStation(int col, int row, String spriteName, Color color, String label,
                             double processTime) {
        super(col, row, spriteName, color, label);
        this.processTime = processTime;
    }

    // Checks if this ingredient can be processed
    protected abstract boolean canProcess(Ingredient ingredient);

    // Called when processing completes
    protected abstract void finish(Ingredient ingredient);

    @Override
    public void interact(Player player) {
        Item held = player.getHeldItem();

        // Station is empty: put a suitable ingredient on it
        if (itemOnTop == null) {
            if (held instanceof Ingredient ingredient && canProcess(ingredient)) {
                itemOnTop = player.takeHeldItem();
                progress = 0;
            }
            return;
        }

        // Station has food: take it with empty hands, or scoop it onto a plate
        if (held == null) {
            player.setHeldItem(itemOnTop);
            removeItem();
        } else if (held instanceof Plate plate && !isWorking() && plate.addIngredient((Ingredient) itemOnTop)) {
            removeItem();
        }
    }

    private void removeItem() {
        itemOnTop = null;
        progress = 0;
    }

    // True while ingredient is being processed
    public boolean isWorking() {
        return itemOnTop instanceof Ingredient ingredient && canProcess(ingredient);
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getProcessTime() {
        return processTime;
    }

    @Override
    public void update(double dt) {
        if (isWorking()) {
            progress += dt * speed;
            if (progress >= processTime) {
                finish((Ingredient) itemOnTop);
                SoundManager.playFoodDone();
            }
        }
    }

    public void increaseSpeed(double amount) {
        speed += amount;
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (isWorking()) {
            drawProgressBar(g, progress / getProcessTime(), new Color(80, 220, 80));
        }
    }

    protected void drawProgressBar(Graphics2D g, double ratio, Color color) {
        int barX = (int) x + 6;
        int barY = (int) y + 2;
        int barWidth = width - 12;
        g.setColor(Color.BLACK);
        g.fillRect(barX - 1, barY - 1, barWidth + 2, 8);
        g.setColor(color);
        g.fillRect(barX, barY, (int) (barWidth * Math.min(1.0, ratio)), 6);
    }
}
