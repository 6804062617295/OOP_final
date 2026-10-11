package station;

import entity.Player;
import item.FireExtinguisher;
import item.Item;

import java.awt.*;

/**
 * A wall stand that holds the fire extinguisher for emergency firefighting.
 */
public class ExtinguisherStand extends Station {

    public ExtinguisherStand(int col, int row) {
        super(col, row, "extinguisher_stand", new Color(140, 50, 50), "Extinguisher");
        // Starts with a fire extinguisher ready for emergency
        this.itemOnTop = new FireExtinguisher();
    }

    @Override
    public void interact(Player player) {
        Item held = player.getHeldItem();

        // 1) Player hands empty: pick up extinguisher
        if (held == null) {
            if (itemOnTop != null) {
                player.setHeldItem(itemOnTop);
                itemOnTop = null;
            }
            return;
        }

        // 2) Player holding the extinguisher: place it back on the stand
        if (held instanceof FireExtinguisher && itemOnTop == null) {
            itemOnTop = player.takeHeldItem();
        }
    }
}
