package station;

import entity.Player;
import item.Plate;

import java.awt.*;

/** Throws away the held item. A plate is kept, only the food on it is thrown away. */
public class TrashCan extends Station {

    public TrashCan(int col, int row) {
        super(col, row, "trash", new Color(70, 70, 70), "Trash");
    }

    @Override
    public void interact(Player player) {
        if (player.getHeldItem() instanceof Plate plate) {
            plate.clear();
        } else if (player.getHeldItem() instanceof item.FireExtinguisher) {
            // Protect emergency equipment from being trashed
            return;
        } else {
            player.takeHeldItem();
        }
    }
}
