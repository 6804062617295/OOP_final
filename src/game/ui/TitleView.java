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
        Assets.drawCenteredText(g, "★ HELL'S KITCHEN: RESTAURANT RUSH ★", width / 2, 34);

        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        g.setColor(new Color(0, 0, 0, 220));
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2 + 2, 73);
        g.setColor(UiKit.TEXT_FLAME);
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2, 72);
        g.setColor(UiKit.TEXT_CREAM);
        Assets.drawCenteredText(g, "HELL'S KITCHEN", width / 2, 71);

        g.setFont(UiKit.FONT_SUBTITLE);
        g.setColor(new Color(210, 190, 170));
        Assets.drawCenteredText(g, "RESTAURANT RUSH", width / 2, 96);

        // Start button
        int btnW = 380;
        int btnH = 38;
        int btnX = (width - btnW) / 2;
        int btnY = 112;
        UiKit.drawButton(g, btnX, btnY, btnW, btnH, "PRESS  [ ENTER ]  OR  [ SPACE ]  TO START",
                new Color(46, 125, 50), new Color(129, 199, 132), Color.WHITE);

        // Info panel
        int infoY = 160;
        int infoW = 672;
        int infoH = height - infoY - 14;
        int infoStartX = (width - infoW) / 2;

        g.setColor(UiKit.PANEL_BG);
        g.fillRoundRect(infoStartX, infoY, infoW, infoH, 14, 14);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawRoundRect(infoStartX, infoY, infoW, infoH, 14, 14);

        g.setColor(new Color(255, 200, 80));
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.drawString("KITCHEN HANDBOOK & LORE GUIDE", infoStartX + 20, infoY + 24);
        g.setColor(UiKit.BORDER_SUBTLE);
        g.drawLine(infoStartX + 16, infoY + 34, infoStartX + infoW - 16, infoY + 34);

        // Controls
        int col1X = infoStartX + 20;
        int textY = infoY + 58;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(140, 200, 255));
        g.drawString("BASIC CONTROLS", col1X, textY);

        textY += 26;
        UiKit.drawKeyControl(g, "WASD / Arrows", "Move chef around kitchen", col1X, textY);
        textY += 26;
        UiKit.drawKeyControl(g, "E / SPACE", "Pick item / Cook / Extinguish", col1X, textY);
        textY += 26;
        UiKit.drawKeyControl(g, "C", "Cycle Plate (White/Red/Gold)", col1X, textY);
        textY += 26;
        UiKit.drawKeyControl(g, "Q", "Alternate interact / Swap", col1X, textY);
        textY += 26;
        UiKit.drawKeyControl(g, "ESC", "Pause game / Main menu", col1X, textY);

        // Mechanics & lore
        int col2X = infoStartX + 340;
        textY = infoY + 58;
        g.setFont(UiKit.FONT_BODY_BOLD);
        g.setColor(new Color(255, 170, 100));
        g.drawString("LORE & MECHANICS", col2X, textY);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(215, 215, 220));
        textY += 24;
        g.drawString("• Ghost Chef   : Run the kitchen rush!", col2X, textY);
        textY += 22;
        g.setColor(new Color(255, 120, 120));
        g.drawString("• Inverted Demon: REVERSED order! Fail = OVER", col2X, textY);
        textY += 22;
        g.setColor(new Color(215, 215, 220));
        g.drawString("• Plate Color  : Match recipe plate color (C)", col2X, textY);
        textY += 22;
        g.drawString("• Stove Fire   : Burned food catches fire! Use (E)", col2X, textY);
        textY += 22;
        g.drawString("• Upgrades     : Buy speed & cooking gear in shop", col2X, textY);
        textY += 22;
        g.drawString("• Daily Goals  : Reach target revenue to pass day", col2X, textY);

        // Footer note
        g.setColor(new Color(255, 220, 130, 200));
        g.setFont(new Font("SansSerif", Font.ITALIC, 11));
        Assets.drawCenteredText(g, "★ Tip: Prioritize Inverted Demons (ปีศาจกลับหัว) before patience runs out!",
                width / 2, infoY + infoH - 14);
    }
}
