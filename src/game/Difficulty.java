package game;

/**
 * Kitchen difficulty. Each level stores the numbers that change how hard a shift is,
 * so the rest of the game never has to check "if difficulty == 1".
 */
public enum Difficulty {
    //        title     rank               patience  chefSpeed  workSpeed  burnTime  spawnOffset  minSpawn
    EASY(    "EASY",   "Apprentice Chef", 2.0,      100,       1.0,       14.0,     6,           8),
    NORMAL(  "NORMAL", "Head Chef",       1.0,      0,         0.0,       0.0,      0,           4),
    HARD(    "HARD",   "Hell's Master",   0.7,      0,         0.0,       -3.0,     -3,          4);

    private final String title;
    private final String rank;
    private final double patienceMultiplier;  // how long customers wait (x normal)
    private final double chefSpeedBonus;      // extra pixels per second
    private final double workSpeedBonus;      // extra chopping / cooking speed
    private final double burnTimeBonus;       // extra seconds before food burns
    private final double spawnIntervalOffset; // extra seconds between customers
    private final double minSpawnInterval;    // customers never come faster than this

    Difficulty(String title, String rank, double patienceMultiplier, double chefSpeedBonus,
               double workSpeedBonus, double burnTimeBonus, double spawnIntervalOffset,
               double minSpawnInterval) {
        this.title = title;
        this.rank = rank;
        this.patienceMultiplier = patienceMultiplier;
        this.chefSpeedBonus = chefSpeedBonus;
        this.workSpeedBonus = workSpeedBonus;
        this.burnTimeBonus = burnTimeBonus;
        this.spawnIntervalOffset = spawnIntervalOffset;
        this.minSpawnInterval = minSpawnInterval;
    }

    /** Level number used over the network (1 = Easy, 2 = Normal, 3 = Hard). */
    public int getLevel() {
        return ordinal() + 1;
    }

    /** Converts a network level number back to a difficulty. Unknown values become NORMAL. */
    public static Difficulty fromLevel(int level) {
        Difficulty[] all = values();
        return (level >= 1 && level <= all.length) ? all[level - 1] : NORMAL;
    }

    public String getTitle() {
        return title;
    }

    public String getRank() {
        return rank;
    }

    public double getPatienceMultiplier() {
        return patienceMultiplier;
    }

    public double getChefSpeedBonus() {
        return chefSpeedBonus;
    }

    public double getWorkSpeedBonus() {
        return workSpeedBonus;
    }

    public double getBurnTimeBonus() {
        return burnTimeBonus;
    }

    public double getSpawnIntervalOffset() {
        return spawnIntervalOffset;
    }

    public double getMinSpawnInterval() {
        return minSpawnInterval;
    }
}
