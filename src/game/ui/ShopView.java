package game.ui;

import game.Assets;
import game.Restaurant;
import upgrade.Upgrade;

import java.awt.*;
import java.util.List;

// Shop screen for upgrades and daily earnings
public final class ShopView {

    private ShopView() {
    }

    public static void draw(Graphics2D g, int width, int height, Restaurant restaurant, String shopMessage) {
        UiKit.drawBackdropWithGlow(g, width, height);

        int centerX = width / 2;

        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(UiKit.TEXT_AMBER);
        Assets.drawCenteredText(g, "★ SHIFT COMPLETED ★", centerX, 38);

        g.setFont(UiKit.FONT_TITLE_LARGE);
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, "DAY " + restaurant.getDay() + " SUMMARY & SHOP", centerX + 2, 78);
        g.setColor(UiKit.TEXT_FLAME);
        Assets.drawCenteredText(g, "DAY " + restaurant.getDay() + " SUMMARY & SHOP", centerX, 77);
        g.setColor(UiKit.TEXT_CREAM);
        Assets.drawCenteredText(g, "DAY " + restaurant.getDay() + " SUMMARY & SHOP", centerX, 76);

        // Stats summary
        int statsW = 672;
        int statsH = 70;
        int statsX = (width - statsW) / 2;
        int statsY = 100;

        g.setColor(UiKit.PANEL_BG);
        g.fillRoundRect(statsX, statsY, statsW, statsH, 14, 14);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(statsX, statsY, statsW, statsH, 14, 14);

        int colW = statsW / 4;
        drawStatColumn(g, statsX, statsY, colW, "EARNED TODAY",
                "$" + restaurant.getEarnedToday() + " / $" + restaurant.getDailyTarget(), new Color(129, 199, 132));
        drawStatColumn(g, statsX + colW, statsY, colW, "SERVED CUSTOMERS",
                String.valueOf(restaurant.getServedToday()), new Color(144, 202, 249));
        drawStatColumn(g, statsX + colW * 2, statsY, colW, "ANGRY LEFTOVERS",
                String.valueOf(restaurant.getLostToday()), new Color(239, 154, 154));
        drawStatColumn(g, statsX + colW * 3, statsY, colW, "TOTAL BANK",
                "$" + restaurant.getMoney(), UiKit.TEXT_GOLD);

        // Upgrades
        List<Upgrade> upgrades = restaurant.getUpgrades();
        int upgradeY = 180;
        int cardH = 64;
        int cardGap = 8;

        for (int i = 0; i < upgrades.size(); i++) {
            Upgrade u = upgrades.get(i);
            int y = upgradeY + i * (cardH + cardGap);
            drawUpgradeCard(g, statsX, y, statsW, cardH, i + 1, u, restaurant.getMoney());
        }

        int footerY = upgradeY + upgrades.size() * (cardH + cardGap) + 10;
        if (shopMessage != null && !shopMessage.isBlank()) {
            g.setFont(UiKit.FONT_SUBTITLE);
            g.setColor(UiKit.TEXT_CREAM);
            Assets.drawCenteredText(g, shopMessage, centerX, footerY);
        }

        int promptY = height - 42;
        int promptW = 672;
        int promptH = 32;
        int promptX = (width - promptW) / 2;

        g.setColor(new Color(24, 26, 38, 220));
        g.fillRoundRect(promptX, promptY, promptW, promptH, 8, 8);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(promptX, promptY, promptW, promptH, 8, 8);

        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(Color.WHITE);
        String actionPrompt = "Press [ 1 - " + upgrades.size() + " ] to Buy  •  "
                + "Press [ ENTER ] to Start Day " + (restaurant.getDay() + 1) + "  •  "
                + "Press [ ESC ] to Quit to Menu";
        Assets.drawCenteredText(g, actionPrompt, centerX, promptY + promptH / 2);
    }

    private static void drawStatColumn(Graphics2D g, int x, int y, int w, String label, String value, Color valueColor) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(UiKit.TEXT_MUTED);
        Assets.drawCenteredText(g, label, x + w / 2, y + 25);

        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.setColor(valueColor);
        Assets.drawCenteredText(g, value, x + w / 2, y + 54);
    }

    private static void drawUpgradeCard(Graphics2D g, int x, int y, int w, int h, int num, Upgrade u, int money) {
        g.setColor(UiKit.CARD_BG);
        g.fillRoundRect(x, y, w, h, 12, 12);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(x, y, w, h, 12, 12);

        int badgeW = 32;
        int badgeH = 30;
        int badgeX = x + 16;
        int badgeY = y + (h - badgeH) / 2;

        g.setColor(new Color(45, 50, 70));
        g.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 6, 6);
        g.setColor(new Color(100, 150, 240));
        g.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 6, 6);
        g.setFont(UiKit.FONT_KEY);
        g.setColor(Color.WHITE);
        Assets.drawCenteredText(g, String.valueOf(num), badgeX + badgeW / 2, badgeY + badgeH / 2);

        int textX = badgeX + badgeW + 18;
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString(u.getName(), textX, y + 32);

        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(u.isMaxed() ? new Color(255, 215, 0) : new Color(140, 200, 255));
        String lvStr = u.isMaxed() ? "MAX LEVEL" : "Level " + u.getLevel() + " / " + Upgrade.MAX_LEVEL;
        g.drawString(" (" + lvStr + ")", textX + g.getFontMetrics(new Font("SansSerif", Font.BOLD, 16)).stringWidth(u.getName()), y + 32);

        g.setFont(UiKit.FONT_BODY);
        g.setColor(UiKit.TEXT_LIGHT);
        g.drawString(u.getDescription(), textX, y + 56);

        int btnW = 120;
        int btnH = 34;
        int btnX = x + w - btnW - 20;
        int btnY = y + (h - btnH) / 2;

        if (u.isMaxed()) {
            g.setColor(new Color(40, 42, 55));
            g.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);
            g.setColor(new Color(100, 100, 120));
            g.drawRoundRect(btnX, btnY, btnW, btnH, 8, 8);
            g.setFont(UiKit.FONT_BODY_BOLD);
            g.setColor(new Color(170, 170, 180));
            Assets.drawCenteredText(g, "MAXED", btnX + btnW / 2, btnY + btnH / 2);
        } else {
            boolean affordable = money >= u.getCost();
            Color bg = affordable ? new Color(30, 90, 45) : new Color(60, 30, 30);
            Color border = affordable ? new Color(80, 200, 110) : new Color(160, 60, 60);
            Color textCol = affordable ? Color.WHITE : new Color(200, 130, 130);

            g.setColor(bg);
            g.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);
            g.setColor(border);
            g.drawRoundRect(btnX, btnY, btnW, btnH, 8, 8);
            g.setFont(UiKit.FONT_BODY_BOLD);
            g.setColor(textCol);
            Assets.drawCenteredText(g, "$" + u.getCost(), btnX + btnW / 2, btnY + btnH / 2);
        }
    }
}
