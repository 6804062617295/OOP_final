package station;

import item.Ingredient;

import java.awt.*;

/** Chops lettuce and tomatoes. */
public class CuttingBoard extends ProcessingStation {

    public CuttingBoard(int col, int row) {
        super(col, row, "cutting_board", new Color(200, 170, 120), "Cut", 2.0);
    }

    @Override
    protected boolean canProcess(Ingredient ingredient) {
        return ingredient.canBeChopped();
    }

    @Override
    protected void finish(Ingredient ingredient) {
        ingredient.chop();
    }
}
