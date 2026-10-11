package game.ui;

import game.Assets;

import java.awt.*;

public final class TitleView {

    private TitleView() {
    }

    public static void draw(Graphics2D g, int width, int height) {
        UiKit.drawBackdropWithGlow(g, width, height);

        // Title
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(UiKit.TEXT_AMBER);
        Assets.drawCenteredText(g, "★ GHOST CHEF: MORTAL BISTRO ★", width / 2, 42);

        g.setFont(new Font("SansSerif", Font.BOLD, 52));
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, "GHOST CHEF", width / 2 + 3, 87);
        g.setColor(UiKit.TEXT_FLAME);
        Assets.drawCenteredText(g, "GHOST CHEF", width / 2, 85);
        g.setColor(UiKit.TEXT_CREAM);
        Assets.drawCenteredText(g, "GHOST CHEF", width / 2, 84);

        g.setFont(UiKit.FONT_SUBTITLE);
        g.setColor(new Color(210, 190, 170));
        Assets.drawCenteredText(g, "SECRET GHOST DINER IN THE MORTAL REALM", width / 2, 116);

        // Start button
        int btnW = 440;
        int btnH = 48;
        int btnX = (width - btnW) / 2;
        int btnY = 142;
        UiKit.drawButton(g, btnX, btnY, btnW, btnH, "PRESS  [ ENTER ]  OR  [ SPACE ]  TO START",
                new Color(46, 125, 50), new Color(129, 199, 132), Color.WHITE);

        // Info panel
        int infoY = 212;
        int infoW = 860;
        int infoH = 438;
        int infoStartX = (width - infoW) / 2;

        g.setColor(UiKit.PANEL_BG);
        g.fillRoundRect(infoStartX, infoY, infoW, infoH, 16, 16);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(infoStartX, infoY, infoW, infoH, 16, 16);

        g.setColor(new Color(255, 200, 80));
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.drawString("KITCHEN HANDBOOK & LORE GUIDE", infoStartX + 24, infoY + 28);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawLine(infoStartX + 20, infoY + 38, infoStartX + infoW - 20, infoY + 38);

        // Controls
        int col1X = infoStartX + 28;
        int textY = infoY + 72;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(140, 200, 255));
        g.drawString("BASIC CONTROLS", col1X, textY);

        textY += 30;
        UiKit.drawKeyControl(g, "WASD / Arrows", "Move Ghost Chef around the kitchen", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "E / SPACE", "Pick item / Cook / Extinguish Fire", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "C", "Cycle Plate Color (Red / Green / Blue / Yellow)", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "Q", "Drop held item / plate onto floor or counter", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "ESC", "Pause shift / Quit confirmation menu", col1X, textY);

        // Mechanics & lore
        int col2X = infoStartX + 440;
        textY = infoY + 72;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(255, 170, 100));
        g.drawString("LORE & SPECIAL MECHANICS", col2X, textY);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(215, 215, 220));
        textY += 28;
        g.drawString("• Ghost Chef      : You are a friendly ghost running a secret mortal diner!", col2X, textY);
        textY += 26;
        g.setColor(new Color(255, 120, 120));
        g.drawString("• ☠ Inverted Demon: Upside-down demons demand REVERSED orders. Fail = INSTANT DEATH!", col2X, textY);
        g.setColor(new Color(215, 215, 220));
        textY += 26;
        g.drawString("• Plate Colors    : Stack onto matching plate color in recipe order.", col2X, textY);
        textY += 26;
        g.drawString("• Hell Fires      : Overcooked stoves catch fire! Extinguish with (E).", col2X, textY);
        textY += 26;
        g.drawString("• Daily Targets   : Earn enough revenue each shift to keep the restaurant open.", col2X, textY);
        textY += 26;
        g.drawString("• Kitchen Shop    : Purchase speed & cooking upgrades between shifts.", col2X, textY);

        // Footer note
        g.setColor(new Color(255, 220, 130, 200));
        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        Assets.drawCenteredText(g, "★ Lore Tip: Inverted Demons (ปีศาจกลับหัว) must be served before their patience runs out!",
                width / 2, infoY + infoH - 22);
    }
}
