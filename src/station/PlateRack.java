package station;

import entity.Player;
import item.Plate;

import java.awt.*;

/**
 * Dispenses clean plates and allows cycling plate colors when held empty.
 */
public class PlateRack extends Station {

    public PlateRack(int col, int row) {
        super(col, row, "plate_rack", new Color(180, 200, 220), "Plates");
    }

    @Override
    public void interact(Player player) {
        if (!player.hasItem()) {
            // Player picks up a new Red plate
            player.setHeldItem(new Plate());
        } else if (player.getHeldItem() instanceof Plate plate && plate.isEmpty()) {
            // Player holds an empty plate: cycle its color (Red -> Yellow -> Green -> Blue -> Black)
            plate.cycleColor();
        }
    }
}
