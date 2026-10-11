package station;

import entity.Player;
import item.Ingredient;
import item.IngredientType;
import item.Plate;

import java.awt.*;

/** A crate with an endless supply of one raw ingredient. */
public class IngredientBox extends Station {
    private final IngredientType type;
    private final Ingredient icon; // only used to draw a picture on the box

    public IngredientBox(int col, int row, IngredientType type) {
        super(col, row, "box_" + type.name().toLowerCase(), new Color(130, 90, 50), type.getDisplayName());
        this.type = type;
        this.icon = new Ingredient(type);
    }

    @Override
    public void interact(Player player) {
        if (!player.hasItem()) {
            player.setHeldItem(new Ingredient(type));
        } else if (player.getHeldItem() instanceof Plate plate) {
            // Shortcut: holding a plate only takes ready-to-eat ingredients (like bread)
            if (!type.isChoppable() && !type.isCookable()) {
                plate.addIngredient(new Ingredient(type));
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        icon.draw(g, (int) x + 18, (int) y + 10, 28);
    }
}
