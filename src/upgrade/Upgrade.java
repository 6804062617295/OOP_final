package upgrade;

import game.Restaurant;

// Base class for shop upgrades
public abstract class Upgrade {
    public static final int MAX_LEVEL = 3;

    private final String name;
    private final String description;
    private final int baseCost;
    private int level = 0;

    protected Upgrade(String name, String description, int baseCost) {
        this.name = name;
        this.description = description;
        this.baseCost = baseCost;
    }

    // Applies upgrade effect to restaurant
    protected abstract void apply(Restaurant restaurant);

    // Each level costs more than the last
    public int getCost() {
        return baseCost * (level + 1);
    }

    public boolean isMaxed() {
        return level >= MAX_LEVEL;
    }

    // Returns true if upgrade was bought
    public boolean buy(Restaurant restaurant) {
        if (isMaxed() || !restaurant.spend(getCost())) {
            return false;
        }
        level++;
        apply(restaurant);
        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }
}
