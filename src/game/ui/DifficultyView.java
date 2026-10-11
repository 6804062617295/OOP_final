package game.ui;

import game.Assets;

import java.awt.*;

/**
 * Renders the 3-tier difficulty selection screen for Solo Campaign and Co-op Host modes.
 */
public final class DifficultyView {

    private DifficultyView() {
    }

    public static void draw(Graphics2D g, int width, int height) {
        // 1. Dark atmospheric backdrop
        UiKit.drawBackdropWithGlow(g, width, height);

        // Header Title
        String badge = "★ CHEF CAMPAIGN ★";
        String modeTitle = "SELECT DIFFICULTY";
        String subDesc = "Choose your kitchen shift intensity";

        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(UiKit.TEXT_AMBER);
        Assets.drawCenteredText(g, badge, width / 2, 45);

        g.setFont(new Font("SansSerif", Font.BOLD, 42));
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, modeTitle, width / 2 + 2, 90);
        g.setColor(UiKit.TEXT_FLAME);
        Assets.drawCenteredText(g, modeTitle, width / 2, 89);
        g.setColor(UiKit.TEXT_CREAM);
        Assets.drawCenteredText(g, modeTitle, width / 2, 88);

        g.setFont(UiKit.FONT_SUBTITLE);
        g.setColor(new Color(200, 190, 180));
        Assets.drawCenteredText(g, subDesc, width / 2, 118);

        // 3 Cards: Easy, Normal, Hard
        int cardY = 145;
        int cardW = 265;
        int cardH = 370;
        int gap = 25;
        int startX = (width - (3 * cardW + 2 * gap)) / 2;

        String hint1 = "Press [ 1 ] for Easy";
        String hint2 = "Press [ 2 ] for Normal";
        String hint3 = "Press [ 3 ] for Hard";

        // Card 1: Easy
        UiKit.drawSelectionCard(g, startX, cardY, cardW, cardH,
                "1", "EASY", "Apprentice Chef",
                new String[]{
                        "• Move Speed +100 (Super fast)",
                        "• Customer Patience: 2x longer",
                        "• 2x Faster Chopping & Cooking",
                        "• Stove Burn Warning: 20s",
                        "• Slower customer arrivals",
                        "• Recommended for beginners"
                },
                hint1,
                new Color(46, 125, 50), new Color(129, 199, 132));

        // Card 2: Normal
        UiKit.drawSelectionCard(g, startX + cardW + gap, cardY, cardW, cardH,
                "2", "NORMAL", "Head Chef",
                new String[]{
                        "• Standard Chef Movement Speed",
                        "• Balanced Customer Patience",
                        "• Standard Cooking Speeds",
                        "• Stove Burn Warning: 6s",
                        "• Regular customer rush flow",
                        "• Standard Hell's Kitchen rules"
                },
                hint2,
                new Color(230, 120, 20), new Color(255, 183, 77));

        // Card 3: Hard
        UiKit.drawSelectionCard(g, startX + (cardW + gap) * 2, cardY, cardW, cardH,
                "3", "HARD", "Hell's Master",
                new String[]{
                        "• Demanding Customers (-30% wait)",
                        "• Rapid Customer Spawns",
                        "• Stoves catch fire in only 3s!",
                        "• Frequent Demon orders",
                        "• Unforgiving kitchen chaos",
                        "• Extreme challenge for pros"
                },
                hint3,
                new Color(198, 40, 40), new Color(239, 154, 154));

        // ESC Prompt at bottom
        int escBoxW = 340;
        int escBoxH = 38;
        int escBoxX = (width - escBoxW) / 2;
        int escBoxY = 550;

        g.setColor(new Color(30, 32, 45, 230));
        g.fillRoundRect(escBoxX, escBoxY, escBoxW, escBoxH, 10, 10);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(escBoxX, escBoxY, escBoxW, escBoxH, 10, 10);

        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(UiKit.TEXT_LIGHT);
        Assets.drawCenteredText(g, "Press [ ESC ] to Return to Title Menu", width / 2, escBoxY + escBoxH / 2);
    }
}
