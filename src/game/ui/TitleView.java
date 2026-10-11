package game.ui;

import game.Assets;

import java.awt.*;

public final class TitleView {

    private TitleView() {
    }

    public static void draw(Graphics2D g, int width, int height) {
        UiKit.drawBackdropWithGlow(g, width, height);

        // Main Title Branding
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(UiKit.TEXT_AMBER);
        Assets.drawCenteredText(g, "★ HELL'S KITCHEN RESTAURANT RUSH ★", width / 2, 42);

        g.setFont(new Font("SansSerif", Font.BOLD, 52));
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2 + 3, 87);
        g.setColor(UiKit.TEXT_FLAME);
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2, 85);
        g.setColor(UiKit.TEXT_CREAM);
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2, 84);

        g.setFont(UiKit.FONT_SUBTITLE);
        g.setColor(new Color(210, 190, 170));
        Assets.drawCenteredText(g, "CULINARY SURVIVAL", width / 2, 116);

        // Start Prompt Banner
        int btnW = 440;
        int btnH = 48;
        int btnX = (width - btnW) / 2;
        int btnY = 142;
        UiKit.drawButton(g, btnX, btnY, btnW, btnH, "PRESS  [ ENTER ]  OR  [ SPACE ]  TO START",
                new Color(46, 125, 50), new Color(129, 199, 132), Color.WHITE);

        // Instructions & Controls Handbook Panel
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
        g.drawString("KITCHEN HANDBOOK & CONTROLS", infoStartX + 24, infoY + 28);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawLine(infoStartX + 20, infoY + 38, infoStartX + infoW - 20, infoY + 38);

        // Left Column: Basic Controls
        int col1X = infoStartX + 28;
        int textY = infoY + 72;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(140, 200, 255));
        g.drawString("BASIC CONTROLS", col1X, textY);

        textY += 30;
        UiKit.drawKeyControl(g, "WASD / Arrows", "Move Chef around the kitchen", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "E / SPACE", "Pick item / Cook / Extinguish Fire", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "C", "Cycle Plate Color (Red / Green / Blue / Yellow)", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "Q", "Drop held item / plate onto floor or counter", col1X, textY);
        textY += 30;
        UiKit.drawKeyControl(g, "ESC", "Pause shift / Quit confirmation menu", col1X, textY);

        // Right Column: Special Mechanics
        int col2X = infoStartX + 440;
        textY = infoY + 72;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(255, 170, 100));
        g.drawString("HELLISH KITCHEN MECHANICS", col2X, textY);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(215, 215, 220));
        textY += 28;
        g.drawString("• Plate Colors   : Stack onto matching plate color in recipe order.", col2X, textY);
        textY += 26;
        g.drawString("• Mirror Demons  : Purple glow orders require REVERSED recipe order!", col2X, textY);
        textY += 26;
        g.drawString("• Hell Fires     : Overcooked stoves catch fire! Use extinguisher stand (E).", col2X, textY);
        textY += 26;
        g.drawString("• Daily Targets  : Earn enough revenue each shift to advance.", col2X, textY);
        textY += 26;
        g.drawString("• Kitchen Shop   : Purchase chef speed & stove upgrades between shifts.", col2X, textY);

        // Handbook Footer Note
        g.setColor(new Color(255, 220, 130, 200));
        g.setFont(new Font("SansSerif", Font.ITALIC, 12));
        Assets.drawCenteredText(g, "★ Tip: Chop vegetables and cook patties ahead of time to beat the lunch rush!",
                width / 2, infoY + infoH - 22);
    }
}
