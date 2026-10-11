package station;

import entity.Player;
import item.Ingredient;
import item.Item;
import item.Plate;

import java.awt.*;

// Counter table for placing items or plating food
public class Counter extends Station {

    public Counter(int col, int row) {
        super(col, row, "counter", new Color(170, 120, 80), "");
    }

    @Override
    public void interact(Player player) {
        Item held = player.getHeldItem();

        // Pick up item
        if (held == null) {
            if (itemOnTop != null) {
                player.setHeldItem(itemOnTop);
                itemOnTop = null;
            }
            return;
        }

        // Place item
        if (itemOnTop == null) {
            if (held instanceof Plate plate && !plate.isEmpty()) {
                itemOnTop = plate.removeLastIngredient();
                return;
            }
            itemOnTop = player.takeHeldItem();
            return;
        }

        // Combine with plate
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

    // Alternate interact (Q key): place entire plate or take top ingredient
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
