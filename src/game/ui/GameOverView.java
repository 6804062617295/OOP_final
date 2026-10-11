package game.ui;

import game.Assets;
import game.Restaurant;

import java.awt.*;

// Game over screen
public final class GameOverView {

    private GameOverView() {
    }

    public static void draw(Graphics2D g, int width, int height, Restaurant restaurant) {
        g.setColor(UiKit.BG_DARK);
        g.fillRect(0, 0, width, height);

        GradientPaint redGlow = new GradientPaint(
                0, 0, new Color(180, 20, 20, 90),
                0, 200, new Color(0, 0, 0, 0));
        g.setPaint(redGlow);
        g.fillRect(0, 0, width, 200);

        int centerX = width / 2;
        boolean isDemonGameOver = restaurant.isGameOver();

        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(new Color(255, 100, 100));
        Assets.drawCenteredText(g, isDemonGameOver ? "★ GHOST IDENTITY EXPOSED ★" : "★ KITCHEN CLOSED DOWN ★", centerX, 70);

        g.setFont(new Font("SansSerif", Font.BOLD, 52));
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, "GAME OVER", centerX + 3, 126);
        g.setColor(new Color(255, 60, 60));
        Assets.drawCenteredText(g, "GAME OVER", centerX, 125);
        g.setColor(new Color(255, 200, 200));
        Assets.drawCenteredText(g, "GAME OVER", centerX, 124);

        int cardW = 580;
        int cardH = 210;
        int cardX = (width - cardW) / 2;
        int cardY = 175;

        UiKit.drawModalCard(g, cardX, cardY, cardW, cardH, new Color(200, 50, 50, 100));

        if (isDemonGameOver) {
            g.setFont(UiKit.FONT_TITLE_MEDIUM);
            g.setColor(new Color(255, 90, 90));
            Assets.drawCenteredText(g, restaurant.getGameOverTitle(), centerX, cardY + 45);

            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g.setColor(new Color(220, 220, 230));
            Assets.drawCenteredText(g, "The Inverted Demon ran out of patience and destroyed the diner!", centerX, cardY + 80);
            Assets.drawCenteredText(g, "Your secret ghost disguise in the mortal world was shattered!", centerX, cardY + 106);

            g.setFont(new Font("SansSerif", Font.BOLD, 13));
            g.setColor(new Color(255, 180, 100));
            Assets.drawCenteredText(g, "⚠️ Lore Tip: Always prioritize the Inverted Demon (ปีศาจกลับหัว) to survive!", centerX, cardY + 138);

            g.setFont(new Font("SansSerif", Font.ITALIC, 12));
            g.setColor(new Color(255, 215, 0));
            Assets.drawCenteredText(g, "\"A ghost that can't even flip a burger backwards has no place on Earth!\"", centerX, cardY + 172);
        } else {
            g.setFont(UiKit.FONT_TITLE_MEDIUM);
            g.setColor(Color.WHITE);
            Assets.drawCenteredText(g, "Daily Revenue Target Missed!", centerX, cardY + 45);

            g.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g.setColor(new Color(210, 210, 220));
            String targetDesc = String.format("On Day %d, you earned $%d out of the required $%d.",
                    restaurant.getDay(), restaurant.getEarnedToday(), restaurant.getDailyTarget());
            Assets.drawCenteredText(g, targetDesc, centerX, cardY + 85);

            int deficit = restaurant.getDailyTarget() - restaurant.getEarnedToday();
            g.setFont(UiKit.FONT_BODY_BOLD);
            g.setColor(new Color(255, 140, 140));
            Assets.drawCenteredText(g, "Revenue Deficit:  -$" + deficit, centerX, cardY + 118);

            g.setFont(new Font("SansSerif", Font.ITALIC, 13));
            g.setColor(new Color(255, 215, 0));
            Assets.drawCenteredText(g, "\"You call that cooking?! Pack your knives and get out!\"", centerX, cardY + 160);
        }

        int btnW = 220;
        int btnH = 46;
        int btnY = cardY + cardH + 45;
        int btn1X = centerX - btnW - 16;
        int btn2X = centerX + 16;

        UiKit.drawButton(g, btn1X, btnY, btnW, btnH, "[ R ]  TRY AGAIN",
                new Color(40, 130, 60), new Color(100, 230, 130), Color.WHITE);

        UiKit.drawButton(g, btn2X, btnY, btnW, btnH, "[ ESC ]  TITLE MENU",
                new Color(45, 50, 70), new Color(120, 140, 180), Color.WHITE);
    }
}
