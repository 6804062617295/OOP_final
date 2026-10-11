package upgrade;

import game.Restaurant;
import station.CuttingBoard;
import station.Station;

/** All cutting boards chop faster. */
public class SharpKnifeUpgrade extends Upgrade {

    public SharpKnifeUpgrade() {
        super("Sharp Knife", "Chop faster", 50);
    }

    @Override
    protected void apply(Restaurant restaurant) {
        for (Station station : restaurant.getStations()) {
            if (station instanceof CuttingBoard board) {
                board.increaseSpeed(0.35);
            }
        }
    }
}
