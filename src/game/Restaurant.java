package game;

import customer.*;
import entity.Fire;
import entity.GameObject;
import entity.Player;
import item.FireExtinguisher;
import item.IngredientType;
import item.Plate;
import item.PlateColor;
import item.Recipe;
import station.*;
import upgrade.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main simulation state holding kitchen stations, chef, customers, and hazards.
 */
public final class Restaurant {
    public static final int COLS = 15;
    public static final int ROWS = 10;
    public static final int TILE = Station.SIZE;

    private static final double DAY_LENGTH = 120;   // seconds per day
    private static final double REACH = 70;         // how far the chef can reach a station
    private static final int SEAT_COUNT = 4;

    /*
     * Kitchen map - Hell's Kitchen edition!
     *   . = floor        C = counter      S = serving window
     *   K = cutting board  O = stove      P = plate rack   X = trash
     *   B = bread  L = lettuce  T = tomato  M = meat  (ingredient boxes)
     *   E = extinguisher stand (for putting out hell fires)
     * Rows 0-1 are the dining room where customers wait.
     */
    private static final String[] LAYOUT = {
            "...............",
            "...............",
            "CCCCCCSSSCCCCCC",
            "C.............C",
            "K.............O",
            "K....CCCCC....O",
            "C.............O",
            "C.............C",
            "C.............C",
            "CBLTMCPCECCCCXC",
    };

    private final Player player;
    private final List<Station> stations = new ArrayList<>();
    private final List<Fire> fires = new ArrayList<>();
    private final Customer[] seats = new Customer[SEAT_COUNT]; // null = empty seat
    private final List<Upgrade> upgrades;
    private final Rectangle worldBounds = new Rectangle(0, 0, COLS * TILE, ROWS * TILE);
    private final Random random = new Random();

    private int money = 0;
    private int day = 1;
    private int earnedToday;
    private int servedToday;
    private int lostToday;
    private double timeLeft;
    private double spawnTimer;
    private double fireSpawnTimer;
    private double patienceMultiplier;
    private final Difficulty difficulty;

    private String message = "";
    private double messageTimer;

    public Restaurant() {
        this(Difficulty.NORMAL);
    }

    public Restaurant(int difficultyLevel) {
        this(Difficulty.fromLevel(difficultyLevel));
    }

    public Restaurant(Difficulty difficulty) {
        this.difficulty = (difficulty != null) ? difficulty : Difficulty.NORMAL;
        this.patienceMultiplier = this.difficulty.getPatienceMultiplier();

        player = new Player(7 * TILE, 7 * TILE);
        if (this.difficulty.getChefSpeedBonus() != 0) {
            player.increaseSpeed(this.difficulty.getChefSpeedBonus());
        }

        buildKitchen();

        for (Station s : stations) {
            if (s instanceof ProcessingStation ps && this.difficulty.getWorkSpeedBonus() != 0) {
                ps.increaseSpeed(this.difficulty.getWorkSpeedBonus());
            }
            if (s instanceof Stove stove && this.difficulty.getBurnTimeBonus() != 0) {
                stove.increaseBurnTime(this.difficulty.getBurnTimeBonus());
            }
        }

        upgrades = List.of(
                new SpeedShoesUpgrade(),
                new SharpKnifeUpgrade(),
                new BetterStoveUpgrade(),
                new CozyDecorUpgrade()
        );
        startDay();
    }

    // ===================== Setup =====================

    private void buildKitchen() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Station station = createStation(LAYOUT[row].charAt(col), col, row);
                if (station != null) {
                    stations.add(station);
                }
            }
        }
    }

    private Station createStation(char symbol, int col, int row) {
        return switch (symbol) {
            case 'C' -> new Counter(col, row);
            case 'S' -> new ServingCounter(col, row, this);
            case 'K' -> new CuttingBoard(col, row);
            case 'O' -> new Stove(col, row);
            case 'P' -> new PlateRack(col, row);
            case 'E' -> new ExtinguisherStand(col, row);
            case 'X' -> new TrashCan(col, row);
            case 'B' -> new IngredientBox(col, row, IngredientType.BREAD);
            case 'L' -> new IngredientBox(col, row, IngredientType.LETTUCE);
            case 'T' -> new IngredientBox(col, row, IngredientType.TOMATO);
            case 'M' -> new IngredientBox(col, row, IngredientType.MEAT);
            default -> null; // floor
        };
    }

    private void startDay() {
        timeLeft = DAY_LENGTH;
        earnedToday = 0;
        servedToday = 0;
        lostToday = 0;
        spawnTimer = 1; // first customer arrives after 1 second
        fireSpawnTimer = 25.0; // first hell fire attempt after 25s
        fires.clear();
        for (int i = 0; i < SEAT_COUNT; i++) {
            seats[i] = null;
        }
    }

    public void nextDay() {
        day++;
        startDay();
    }

    public Player getPlayer() {
        return player;
    }

    // ===================== Game loop =====================

    /** dirX / dirY = movement direction from the keyboard (-1, 0 or 1). */
    public void update(double dt, double dirX, double dirY) {
        List<GameObject> obstacles = new ArrayList<>(stations);
        obstacles.addAll(fires);
        player.move(dirX, dirY, dt, obstacles, worldBounds);
        updateLogic(dt);
    }

    /** Updates simulation logic without moving chef 1. Used by Dedicated Server. */
    public void updateLogic(double dt) {
        timeLeft -= dt;

        for (Station station : stations) {
            station.update(dt);
        }

        for (Fire fire : fires) {
            fire.update(dt);
        }

        updateHellFires(dt);
        updateCustomers(dt);

        if (messageTimer > 0) {
            messageTimer -= dt;
        }
    }



    private void updateHellFires(double dt) {
        fireSpawnTimer -= dt;
        if (fireSpawnTimer <= 0) {
            spawnHellFire();
            fireSpawnTimer = 25.0 + random.nextInt(20);
        }
    }

    private void spawnHellFire() {
        if (fires.size() >= 2) {
            return;
        }
        for (int attempt = 0; attempt < 30; attempt++) {
            int col = 2 + random.nextInt(11);
            int row = 3 + random.nextInt(6);
            if (LAYOUT[row].charAt(col) == '.') {
                Rectangle target = new Rectangle(col * TILE, row * TILE, TILE, TILE);
                boolean blocked = false;
                for (Fire f : fires) {
                    if (f.getBounds().intersects(target)) {
                        blocked = true;
                        break;
                    }
                }
                if (!blocked && !player.getBounds().intersects(target)) {
                    fires.add(new Fire(col, row));
                    showMessage("FIRE! Hell flames erupted! Grab the extinguisher!");
                    break;
                }
            }
        }
    }

    private void updateCustomers(double dt) {
        spawnTimer -= dt;
        if (spawnTimer <= 0) {
            spawnCustomer();
            spawnTimer = getSpawnInterval();
        }

        for (int i = 0; i < SEAT_COUNT; i++) {
            Customer customer = seats[i];
            if (customer == null) {
                continue;
            }
            customer.update(dt);
            if (customer.isOutOfPatience()) {
                seats[i] = null;
                lostToday++;
                showMessage("A " + customer.getLabel() + " left angry!");
            }
        }
    }

    private void spawnCustomer() {
        for (int i = 0; i < SEAT_COUNT; i++) {
            if (seats[i] == null) {
                Customer customer = createRandomCustomer();
                customer.setPosition(20 + i * 240, 24);
                seats[i] = customer;
                return;
            }
        }
    }

    private Customer createRandomCustomer() {
        int roll = random.nextInt(100);
        if (roll < 15) {
            return new VipCustomer(patienceMultiplier);
        } else if (roll < 25) {
            return new CriticCustomer(patienceMultiplier);
        } else if (roll < 45) {
            return new KidCustomer(patienceMultiplier);
        } else if (roll < 75) {
            // POLYMORPHISM: MirrorCustomer demands REVERSED/REFLECTED ingredient order
            return new MirrorCustomer(patienceMultiplier);
        } else {
            return new NormalCustomer(patienceMultiplier);
        }
    }

    /** Customers come more often on later days. */
    private double getSpawnInterval() {
        double interval = 14 - day * 1.5;
        interval += difficulty.getSpawnIntervalOffset();
        return Math.max(difficulty.getMinSpawnInterval(), interval);
    }

    // ===================== Player actions =====================

    public void interact() {
        if (player.getHeldItem() instanceof FireExtinguisher) {
            Fire nearestFire = findNearestFire(player);
            if (nearestFire != null) {
                nearestFire.extinguish(0.45);
                showMessage("Extinguishing fire...");
                if (nearestFire.isExtinguished()) {
                    fires.remove(nearestFire);
                    showMessage("Fire extinguished! Kitchen safe.");
                }
                return;
            }
        }

        Station target = findNearestStation(player);
        if (target != null) {
            target.interact(player);
        }
    }

    public void alternateInteract() {
        Station target = findNearestStation(player);
        if (target instanceof Counter counter) {
            counter.alternateInteract(player);
        }
    }

    public void cyclePlateColor() {
        if (player.getHeldItem() instanceof Plate plate && plate.isEmpty()) {
            plate.cycleColor();
            showMessage("Plate: " + plate.getColor().getDisplayName());
        }
    }

    public Fire findNearestFire() {
        return findNearestFire(player);
    }

    public Fire findNearestFire(Player p) {
        if (p == null) return null;
        Fire nearest = null;
        double bestDistance = REACH;
        for (Fire fire : fires) {
            double distance = p.distanceTo(fire);
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = fire;
            }
        }
        return nearest;
    }

    public Station findNearestStation() {
        return findNearestStation(player);
    }

    public Station findNearestStation(Player p) {
        if (p == null) return null;
        Station nearest = null;
        double bestDistance = REACH;
        for (Station station : stations) {
            double distance = p.distanceTo(station);
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = station;
            }
        }
        return nearest;
    }

    /**
     * Gives the plate to a customer who ordered it.
     * If several customers want the same dish, the one who waited longest gets it.
     */
    public boolean serve(Plate plate) {
        int best = -1;
        for (int i = 0; i < SEAT_COUNT; i++) {
            Customer customer = seats[i];
            if (customer != null && customer.wants(plate)) {
                if (best == -1 || customer.getPatienceRatio() < seats[best].getPatienceRatio()) {
                    best = i;
                }
            }
        }
        if (best == -1) {
            return false;
        }

        Customer customer = seats[best];
        int payment = customer.pay(); // POLYMORPHISM: each customer type pays differently
        money += payment;
        earnedToday += payment;
        servedToday++;
        seats[best] = null;
        showMessage(customer.getLabel() + " paid $" + payment + "!");
        return true;
    }

    // ===================== Money & upgrades =====================

    public boolean spend(int amount) {
        if (amount > money) {
            return false;
        }
        money -= amount;
        return true;
    }

    public void increasePatience(double amount) {
        patienceMultiplier += amount;
    }

    public int getDailyTarget() {
        return 50 + day * 50;
    }

    public boolean isDayOver() {
        return timeLeft <= 0;
    }

    public boolean reachedTarget() {
        return earnedToday >= getDailyTarget();
    }

    public void showMessage(String text) {
        message = text;
        messageTimer = 2.5;
    }

    // ===================== Getters =====================

    public List<Station> getStations() {
        return stations;
    }

    public List<Fire> getFires() {
        return fires;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getMoney() {
        return money;
    }

    public int getDay() {
        return day;
    }

    public int getEarnedToday() {
        return earnedToday;
    }

    public int getServedToday() {
        return servedToday;
    }

    public int getLostToday() {
        return lostToday;
    }

    public double getTimeLeft() {
        return Math.max(0, timeLeft);
    }

    public String getMessage() {
        return messageTimer > 0 ? message : "";
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    // ===================== Drawing =====================

    public void draw(Graphics2D g) {
        drawFloor(g);

        for (Station station : stations) {
            station.draw(g); // POLYMORPHISM: each station draws itself
        }

        for (Fire fire : fires) {
            fire.draw(g); // POLYMORPHISM: each hazard draws itself
        }

        // Highlight nearest fire if player holds extinguisher
        if (player.getHeldItem() instanceof FireExtinguisher) {
            Fire targetFire = findNearestFire();
            if (targetFire != null) {
                g.setColor(new Color(80, 190, 255));
                g.setStroke(new BasicStroke(3));
                Rectangle r = targetFire.getBounds();
                g.drawRect(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
                g.setStroke(new BasicStroke(1));
            }
        }

        // Highlight the station the chef would use
        Station target = findNearestStation();
        if (target != null) {
            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(3));
            Rectangle r = target.getBounds();
            g.drawRect(r.x + 1, r.y + 1, r.width - 3, r.height - 3);
            g.setStroke(new BasicStroke(1));
        }

        for (Customer customer : seats) {
            if (customer != null) {
                customer.draw(g);
            }
        }

        player.draw(g);
    }

    private void drawFloor(Graphics2D g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boolean light = (row + col) % 2 == 0;
                if (row < 2) {
                    Color color = light ? new Color(150, 100, 70) : new Color(140, 92, 64);
                    Assets.drawTile(g, "floor_dining", col * TILE, row * TILE, TILE, color);
                } else {
                    Color color = light ? new Color(225, 225, 215) : new Color(205, 205, 195);
                    Assets.drawTile(g, "floor", col * TILE, row * TILE, TILE, color);
                }
            }
        }
    }


    public Station getStationAt(int col, int row) {
        for (Station s : stations) {
            if (s.getCol() == col && s.getRow() == row) {
                return s;
            }
        }
        return null;
    }
}
