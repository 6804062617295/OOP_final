package station;

import entity.Player;
import item.FireExtinguisher;
import item.Item;

import java.awt.*;

// Stand holding the fire extinguisher
public class ExtinguisherStand extends Station {

    public ExtinguisherStand(int col, int row) {
        super(col, row, "extinguisher_stand", new Color(140, 50, 50), "");
        this.itemOnTop = new FireExtinguisher();
    }

    @Override
    public void interact(Player player) {
        Item held = player.getHeldItem();

        // Pick up
        if (held == null) {
            if (itemOnTop != null) {
                player.setHeldItem(itemOnTop);
                itemOnTop = null;
            }
            return;
        }

        // Place back
        if (held instanceof FireExtinguisher && itemOnTop == null) {
            itemOnTop = player.takeHeldItem();
        }
    }
}
