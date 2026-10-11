package upgrade;

import game.Restaurant;

/** The chef walks faster. */
public class SpeedShoesUpgrade extends Upgrade {

    public SpeedShoesUpgrade() {
        super("Speed Shoes", "Chef walks faster", 40);
    }

    @Override
    protected void apply(Restaurant restaurant) {
        restaurant.getPlayer().increaseSpeed(40);
    }
}
