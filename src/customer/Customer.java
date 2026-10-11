package customer;

import entity.GameObject;
import game.Assets;
import item.Ingredient;
import item.Plate;
import item.Recipe;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Abstract customer waiting for a dish in the dining area.
 */
public abstract class Customer extends GameObject {
    public static final int SIZE = 56;
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
        super(0, 0, SIZE, SIZE);
        this.order = order;
        this.maxPatience = patienceSeconds;
        this.patience = patienceSeconds;
    }

    // ----- Things every subclass must provide -----

    public abstract String getLabel();

    protected abstract String getSpriteName();

    protected abstract Color getColor();

    // ----- Things subclasses MAY change (POLYMORPHISM / OVERRIDING) -----

    /**
     * Whether this customer wants the ingredient order reversed (e.g. MaleCustomer).
     * Normal customers use standard order (false).
     */
    public boolean isReversed() {
        return false;
    }

    /** Normal price = 1.0. VIPs pay more, kids pay less. */
    protected double getPaymentMultiplier() {
        return 1.0;
    }

    /** How much money this customer pays. Faster service = bigger tip. */
    public int pay() {
        int price = order.getPrice();
        int tip = (int) Math.round(price * 0.5 * getPatienceRatio());
        return (int) Math.round((price + tip) * getPaymentMultiplier());
    }

    // ----- Shared behaviour -----

    public void update(double dt) {
        patience -= dt;
    }

    public boolean isOutOfPatience() {
        return patience <= 0;
    }

    /** 1.0 = just arrived, 0.0 = about to leave. */
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

    /**
     * Checks if the plate matches this customer's exact order,
     * polymorphically considering whether this customer requires a reversed recipe.
     */
    public boolean wants(Plate plate) {
        return plate.matches(order, isReversed());
    }

    public Recipe getOrder() {
        return order;
    }

    /** Helper for subclasses: pick a random dish from a list. */
    protected static Recipe randomRecipe(List<Recipe> choices) {
        return choices.get(RANDOM.nextInt(choices.size()));
    }

    // ----- Drawing -----

    @Override
    public void draw(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        Assets.draw(g, getSpriteName(), px, py, width, height, getColor(), getLabel());
        drawPatienceBar(g, px, py + height + 4);
        drawOrderBubble(g, px + width + 8, py);
    }

    private void drawPatienceBar(Graphics2D g, int barX, int barY) {
        double ratio = getPatienceRatio();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, width, 6);

        if (ratio > 0.5) {
            g.setColor(new Color(80, 220, 80));
        } else if (ratio > 0.25) {
            g.setColor(Color.ORANGE);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(barX, barY, (int) (width * ratio), 6);
    }

    private void drawOrderBubble(Graphics2D g, int bubbleX, int bubbleY) {
        List<Ingredient> parts = isReversed() ? order.getReversedIngredients() : order.getIngredients();
        Color plateColor = order.getPlateColor().getColor();

        Font titleFont = new Font("SansSerif", Font.BOLD, 12);
        String title = order.getName() + " $" + order.getPrice();
        int titleWidth = g.getFontMetrics(titleFont).stringWidth(title);

        int badgeWidth = isReversed() ? 66 : 0;
        int topWidth = 32 + titleWidth + (isReversed() ? (10 + badgeWidth) : 0) + 12;
        int step = 38;
        int trayWidth = parts.size() * step - (step - 22) + 12;
        int bottomWidth = trayWidth + 28;
        int bubbleWidth = Math.max(160, Math.max(topWidth, bottomWidth));
        int bubbleHeight = 58;

        // Prevent bubble from overflowing right edge of the screen
        bubbleX = Math.min(bubbleX, 960 - bubbleWidth - 8);

        // Draw bubble card (White rounded background)
        g.setColor(Color.WHITE);
        g.fillRoundRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight, 12, 12);

        // Distinct Left Accent Stripe in the plate's color
        g.setColor(plateColor);
        g.fillRoundRect(bubbleX, bubbleY, 8, bubbleHeight, 10, 10);
        g.fillRect(bubbleX + 4, bubbleY, 4, bubbleHeight);

        // Outer Card Border
        g.setColor(isReversed() ? new Color(220, 60, 60) : new Color(190, 190, 195));
        g.setStroke(new BasicStroke(isReversed() ? 2 : 1));
        g.drawRoundRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight, 12, 12);
        g.setStroke(new BasicStroke(1));

        // Plate indicator icon (solid colored disc with border)
        g.setColor(plateColor);
        g.fillOval(bubbleX + 13, bubbleY + 7, 12, 12);
        g.setColor(plateColor.darker());
        g.drawOval(bubbleX + 13, bubbleY + 7, 12, 12);

        // Recipe Title & Price with color tag (e.g. "[RED] Hellfire Steak $30")
        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        g.drawString(title, bubbleX + 30, bubbleY + 18);

        // Pill badge for reversed order (cleanly separated on the top-right)
        if (isReversed()) {
            int badgeX = bubbleX + bubbleWidth - badgeWidth - 6;
            int badgeY = bubbleY + 5;
            g.setColor(new Color(255, 235, 235));
            g.fillRoundRect(badgeX, badgeY, badgeWidth, 16, 6, 6);
            g.setColor(new Color(220, 40, 40));
            g.drawRoundRect(badgeX, badgeY, badgeWidth, 16, 6, 6);
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            Assets.drawCenteredText(g, "REVERSE", badgeX + badgeWidth / 2, badgeY + 11);
        }

        // Mini Plate Tray underneath ingredients in the exact plate color!
        int trayX = bubbleX + 14;
        int trayY = bubbleY + 25;
        int trayHeight = 27;

        // Subtle plate color fill + border
        g.setColor(new Color(plateColor.getRed(), plateColor.getGreen(), plateColor.getBlue(), 35));
        g.fillRoundRect(trayX, trayY, trayWidth, trayHeight, 8, 8);
        g.setColor(plateColor);
        g.drawRoundRect(trayX, trayY, trayWidth, trayHeight, 8, 8);

        // Draw ingredient sequence with clear centered arrows inside the plate tray
        for (int i = 0; i < parts.size(); i++) {
            int ix = trayX + 6 + i * step;
            parts.get(i).draw(g, ix, trayY + 2, 22);
            if (i < parts.size() - 1) {
                g.setColor(new Color(90, 90, 105));
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                Assets.drawCenteredText(g, "→", ix + 22 + (step - 22) / 2, trayY + trayHeight / 2);
            }
        }
    }
}
