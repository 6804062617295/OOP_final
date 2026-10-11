package station;

import entity.Player;
import item.Ingredient;
import item.Item;
import item.Plate;

import java.awt.*;

/**
 * A plain table. You can put anything on it and pick it up again.
 * If there is a plate on it, you can drop ingredients onto the plate.
 */
public class Counter extends Station {

    public Counter(int col, int row) {
        super(col, row, "counter", new Color(170, 120, 80), "");
    }

    @Override
    public void interact(Player player) {
        Item held = player.getHeldItem();

        // 1) Empty hands: pick up whatever is on the counter
        if (held == null) {
            if (itemOnTop != null) {
                player.setHeldItem(itemOnTop);
                itemOnTop = null;
            }
            return;
        }

        // 2) Counter is empty:
        if (itemOnTop == null) {
            // If holding a plate with food, unload the last added ingredient onto the counter!
            if (held instanceof Plate plate && !plate.isEmpty()) {
                itemOnTop = plate.removeLastIngredient();
                return;
            }
            // Otherwise, put the held item down normally
            itemOnTop = player.takeHeldItem();
            return;
        }

        // 3) Both have something: try to combine an ingredient with a plate
        if (itemOnTop instanceof Plate plate && held instanceof Ingredient ingredient) {
            if (plate.addIngredient(ingredient)) {
                player.takeHeldItem();
            }
        } else if (held instanceof Plate plate && itemOnTop instanceof Ingredient ingredient) {
            if (plate.addIngredient(ingredient)) {
                itemOnTop = null;
            }
        }
    }

    /**
     * Alternate interact (Q key):
     * - If holding a plate with food: places the ENTIRE plate onto the counter.
     * - If counter has a plate with food & hands are empty: takes top ingredient into hand.
     */
    public void alternateInteract(Player player) {
        Item held = player.getHeldItem();

        if (itemOnTop == null && held instanceof Plate) {
            itemOnTop = player.takeHeldItem();
            return;
        }

        if (held == null && itemOnTop instanceof Plate plate && !plate.isEmpty()) {
            player.setHeldItem(plate.removeLastIngredient());
        }
    }
}
