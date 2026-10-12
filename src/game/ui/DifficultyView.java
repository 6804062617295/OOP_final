package game.ui;

import game.Assets;

import java.awt.*;

// Difficulty selection screen
public final class DifficultyView {

    private DifficultyView() {
    }

    public static void draw(Graphics2D g, int width, int height) {
        UiKit.drawBackdropWithGlow(g, width, height);

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

        int cardY = 135;
        int cardW = 216;
        int cardH = 325;
        int gap = 14;
        int startX = (width - (3 * cardW + 2 * gap)) / 2;

        String hint1 = "Press [ 1 ] for Easy";
        String hint2 = "Press [ 2 ] for Normal";
        String hint3 = "Press [ 3 ] for Hard";

        // Easy
        UiKit.drawSelectionCard(g, startX, cardY, cardW, cardH,
                "1", "EASY", "Apprentice Chef",
                new String[]{
                        "• Move Speed +100",
                        "• Customer Patience: 2x",
                        "• 2x Faster Prep & Cook",
                        "• Burn Warning: 20s",
                        "• Relaxed customer flow",
                        "• Best for beginners"
                },
                hint1,
                new Color(46, 125, 50), new Color(129, 199, 132));

        // Normal
        UiKit.drawSelectionCard(g, startX + cardW + gap, cardY, cardW, cardH,
                "2", "NORMAL", "Head Chef",
                new String[]{
                        "• Standard Speed",
                        "• Balanced Patience",
                        "• Standard Cook Speeds",
                        "• Burn Warning: 6s",
                        "• Standard Rush Flow",
                        "• Standard Rules"
                },
                hint2,
                new Color(230, 120, 20), new Color(255, 183, 77));

        // Hard
        UiKit.drawSelectionCard(g, startX + (cardW + gap) * 2, cardY, cardW, cardH,
                "3", "HARD", "Hell's Master",
                new String[]{
                        "• Strict Rush Speeds",
                        "• Impatient Customers",
                        "• Stoves catch fire in 3s!",
                        "• Frequent Demon orders",
                        "• Kitchen chaos challenge",
                        "• For master chefs"
                },
                hint3,
                new Color(198, 40, 40), new Color(239, 154, 154));

        int escBoxW = 300;
        int escBoxH = 28;
        int escBoxX = (width - escBoxW) / 2;
        int escBoxY = cardY + cardH + 12;

        g.setColor(new Color(30, 32, 45, 230));
        g.fillRoundRect(escBoxX, escBoxY, escBoxW, escBoxH, 8, 8);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(escBoxX, escBoxY, escBoxW, escBoxH, 8, 8);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(UiKit.TEXT_LIGHT);
        Assets.drawCenteredText(g, "Press [ ESC ] to Return to Title Menu", width / 2, escBoxY + escBoxH / 2);
    }
}
