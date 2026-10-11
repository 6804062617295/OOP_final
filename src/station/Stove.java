package station;

import item.Ingredient;
import item.IngredientState;

import java.awt.*;

/**
 * Cooks meat. Burns food if left unattended too long.
 */
public class Stove extends ProcessingStation {
    private double burnTime = 6.0; // seconds after cooking until it burns

    public Stove(int col, int row) {
        super(col, row, "stove", new Color(90, 90, 100), "Stove", 4.0);
    }

    public void increaseBurnTime(double amount) {
        burnTime += amount;
    }

    @Override
    protected boolean canProcess(Ingredient ingredient) {
        return ingredient.canBeCooked();
    }

    @Override
    protected void finish(Ingredient ingredient) {
        ingredient.cook();
    }

    /** True when cooked food is sitting on the stove (and will burn soon). */
    private boolean isBurning() {
        return itemOnTop instanceof Ingredient ingredient && ingredient.getState() == IngredientState.COOKED;
    }

    @Override
    public void update(double dt) {
        super.update(dt); // normal cooking

        if (isBurning()) {
            progress += dt;
            if (progress >= processTime + burnTime) {
                ((Ingredient) itemOnTop).burn();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (isBurning()) {
            double burnRatio = (progress - processTime) / burnTime;
            drawProgressBar(g, burnRatio, Color.RED);
        }
    }
}
