package station;

import entity.Player;
import game.Restaurant;
import item.Plate;

import java.awt.*;

/** The window where finished dishes are handed to the customers. */
public class ServingCounter extends Station {
    private final Restaurant restaurant;

    public ServingCounter(int col, int row, Restaurant restaurant) {
        super(col, row, "serving", new Color(240, 200, 60), "Serve");
        this.restaurant = restaurant;
    }

    @Override
    public void interact(Player player) {
        if (!(player.getHeldItem() instanceof Plate plate) || plate.isEmpty()) {
            restaurant.showMessage("Bring a plate with food!");
            return;
        }

        if (restaurant.serve(plate)) {
            player.takeHeldItem(); // the plate goes to the customer
        } else {
            restaurant.showMessage("Nobody ordered that!");
        }
    }
}
