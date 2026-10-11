package upgrade;

import game.Restaurant;
import station.Station;
import station.Stove;

// Upgrade: stoves cook faster
public class BetterStoveUpgrade extends Upgrade {

    public BetterStoveUpgrade() {
        super("Better Stove", "Cook faster", 60);
    }

    @Override
    protected void apply(Restaurant restaurant) {
        for (Station station : restaurant.getStations()) {
            if (station instanceof Stove stove) {
                stove.increaseSpeed(0.35);
            }
        }
    }
}
