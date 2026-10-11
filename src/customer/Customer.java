package customer;

import entity.GameObject;
import game.Assets;
import item.Ingredient;
import item.Plate;
import item.Recipe;

import java.awt.*;
import java.util.List;
import java.util.Random;

// Base class for restaurant customers
public abstract class Customer extends GameObject {
    public static final int CARD_WIDTH = 224;
    public static final int CARD_HEIGHT = 72;
    public static final int AVATAR_SIZE = 42;
    public static final int SIZE = 56; // backwards compatibility
    private static final Random RANDOM = new Random();

    private Recipe order;
    private double maxPatience;
    private double patience;

    public void setOrder(Recipe order) {
        this.order = order;
    }

    public void setMaxPatience(double maxPatience) {
        this.maxPatience = maxPatience;
    }

    protected Customer(Recipe order, double patienceSeconds) {
        super(0, 0, CARD_WIDTH, CARD_HEIGHT);
        this.order = order;
        this.maxPatience = patienceSeconds;
        this.patience = patienceSeconds;
    }

    public abstract String getLabel();

    protected abstract String getSpriteName();

    protected abstract Color getColor();

    // Custom behavior overrides
    public boolean isReversed() {
        return false;
    }

    // Instant game over if patience runs out (Inverted Demon)
    public boolean isFatalWhenAngry() {
        return false;
    }

    // Normal price = 1.0. VIPs pay more, kids pay less
    protected double getPaymentMultiplier() {
        return 1.0;
    }

    // Payment calculation with tips
    public int pay() {
        int price = order.getPrice();
        int tip = (int) Math.round(price * 0.5 * getPatienceRatio());
        return (int) Math.round((price + tip) * getPaymentMultiplier());
    }

    public void update(double dt) {
        patience -= dt;
    }

    public boolean isOutOfPatience() {
        return patience <= 0;
    }

    // Patience ratio (1.0 to 0.0)
    public double getPatienceRatio() {
        return Math.max(0, patience / maxPatience);
    }

    public double getPatience() {
        return patience;
    }

    public void setPatience(double patience) {
        this.patience = patience;
    }

    public double getMaxPatience() {
        return maxPatience;
    }

    public String getTypeName() {
        return getClass().getSimpleName();
    }

    // Checks if the plate matches this customer's order
    public boolean wants(Plate plate) {
        return plate.matches(order, isReversed());
    }

    public Recipe getOrder() {
        return order;
    }

    // Pick a random dish from a list
    protected static Recipe randomRecipe(List<Recipe> choices) {
        return choices.get(RANDOM.nextInt(choices.size()));
    }

    // ----- Drawing -----

    @Override
    public void draw(Graphics2D g) {
        int cx = (int) x;
        int cy = (int) y;
        List<Ingredient> parts = isReversed() ? order.getReversedIngredients() : order.getIngredients();
        Color plateColor = order.getPlateColor().getColor();

        // Card shadow and body
        g.setColor(new Color(0, 0, 0, 22));
        g.fillRoundRect(cx + 1, cy + 2, CARD_WIDTH, CARD_HEIGHT, 12, 12);
        g.setColor(Color.WHITE);
        g.fillRoundRect(cx, cy, CARD_WIDTH, CARD_HEIGHT, 12, 12);

        // Plate color stripe
        g.setColor(plateColor);
        g.fillRoundRect(cx, cy, 7, CARD_HEIGHT, 10, 10);
        g.fillRect(cx + 4, cy, 3, CARD_HEIGHT);

        // Card border
        if (isReversed()) {
            g.setColor(new Color(220, 50, 50));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(cx, cy, CARD_WIDTH, CARD_HEIGHT, 12, 12);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(new Color(205, 205, 215));
            g.drawRoundRect(cx, cy, CARD_WIDTH, CARD_HEIGHT, 12, 12);
        }

        // Avatar & patience bar
        int ax = cx + 10;
        int ay = cy + 8;
        Assets.draw(g, getSpriteName(), ax, ay, AVATAR_SIZE, AVATAR_SIZE, getColor(), getLabel());
        drawPatienceBar(g, ax, ay + AVATAR_SIZE + 4, AVATAR_SIZE, 6);

        // Order header
        int rightX = cx + 58;
        int availWidth = CARD_WIDTH - 58 - 8;

        g.setColor(plateColor);
        g.fillOval(rightX, cy + 9, 10, 10);
        g.setColor(plateColor.darker());
        g.drawOval(rightX, cy + 9, 10, 10);

        String title = order.getName();
        String price = "$" + order.getPrice();

        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.setColor(new Color(25, 25, 30));
        g.drawString(title, rightX + 14, cy + 18);

        if (isReversed()) {
            boolean fatal = isFatalWhenAngry();
            String badgeText = fatal ? "☠ REV" : "REV";
            int badgeW = fatal ? 40 : 34;
            int badgeH = 14;
            int badgeX = cx + CARD_WIDTH - badgeW - 6;
            int badgeY = cy + 6;
            g.setColor(fatal ? new Color(255, 215, 215) : new Color(255, 235, 235));
            g.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 4, 4);
            g.setColor(fatal ? new Color(190, 20, 20) : new Color(220, 40, 40));
            g.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 4, 4);
            g.setFont(new Font("SansSerif", Font.BOLD, fatal ? 8 : 9));
            Assets.drawCenteredText(g, badgeText, badgeX + badgeW / 2, badgeY + 10);

            g.setColor(new Color(40, 140, 40));
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            int priceW = g.getFontMetrics().stringWidth(price);
            g.drawString(price, badgeX - priceW - 4, cy + 17);
        } else {
            g.setColor(new Color(40, 140, 40));
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            int priceW = g.getFontMetrics().stringWidth(price);
            g.drawString(price, cx + CARD_WIDTH - priceW - 8, cy + 18);
        }

        // Ingredient tray
        int trayX = rightX;
        int trayY = cy + 26;
        int trayH = 37;
        int step = (parts.size() <= 2) ? 34 : 26;
        int trayW = parts.size() * step - (step - 22) + 12;
        trayW = Math.min(availWidth, trayW);

        g.setColor(new Color(plateColor.getRed(), plateColor.getGreen(), plateColor.getBlue(), 30));
        g.fillRoundRect(trayX, trayY, trayW, trayH, 8, 8);
        g.setColor(plateColor);
        g.drawRoundRect(trayX, trayY, trayW, trayH, 8, 8);

        for (int i = 0; i < parts.size(); i++) {
            int ix = trayX + 6 + i * step;
            parts.get(i).draw(g, ix, trayY + 7, 22);
            if (i < parts.size() - 1) {
                g.setColor(new Color(90, 90, 105));
                g.setFont(new Font("SansSerif", Font.BOLD, 11));
                Assets.drawCenteredText(g, "→", ix + 22 + (step - 22) / 2, trayY + trayH / 2);
            }
        }
    }

    private void drawPatienceBar(Graphics2D g, int barX, int barY, int barW, int barH) {
        double ratio = getPatienceRatio();
        g.setColor(new Color(60, 60, 65));
        g.fillRoundRect(barX, barY, barW, barH, 4, 4);

        if (ratio > 0.5) {
            g.setColor(new Color(75, 205, 75));
        } else if (ratio > 0.25) {
            g.setColor(new Color(245, 160, 40));
        } else {
            g.setColor(new Color(230, 50, 50));
        }
        int fillW = (int) Math.round(barW * ratio);
        if (fillW > 0) {
            g.fillRoundRect(barX, barY, fillW, barH, 4, 4);
        }
    }
}
