package upgrade;

import game.Restaurant;

/** Nicer decoration: new customers wait longer before getting angry. */
public class CozyDecorUpgrade extends Upgrade {

    public CozyDecorUpgrade() {
        super("Cozy Decor", "Customers wait longer", 50);
    }

    @Override
    protected void apply(Restaurant restaurant) {
        restaurant.increasePatience(0.15);
    }
}
