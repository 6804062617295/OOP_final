package game.ui;

import game.Restaurant;

import java.awt.*;

public final class HudView {

    public static final int HUD_HEIGHT = 40;

    private HudView() {
    }

    public static void draw(Graphics2D g, int width, Restaurant restaurant) {
        g.setColor(new Color(40, 40, 50));
        g.fillRect(0, 0, width, HUD_HEIGHT);

        int seconds = (int) Math.ceil(restaurant.getTimeLeft());
        String info = String.format("Day %d     Time %d:%02d     Money $%d     Today $%d / $%d",
                restaurant.getDay(), seconds / 60, seconds % 60, restaurant.getMoney(),
                restaurant.getEarnedToday(), restaurant.getDailyTarget());

        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(restaurant.reachedTarget() ? new Color(120, 230, 120) : Color.WHITE);
        g.drawString(info, 14, 26);

        String message = restaurant.getMessage();
        if (message != null && !message.isEmpty()) {
            g.setColor(Color.YELLOW);
            g.drawString(message, width - g.getFontMetrics().stringWidth(message) - 14, 26);
        }
    }
}
