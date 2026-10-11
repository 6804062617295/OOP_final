package game.ui;

import game.Assets;

import java.awt.*;

// UI helpers and styling constants
public final class UiKit {

    // Theme Colors
    public static final Color BG_DARK = new Color(10, 8, 14, 235);
    public static final Color CARD_BG = new Color(24, 26, 38, 240);
    public static final Color PANEL_BG = new Color(22, 24, 34, 235);
    public static final Color BORDER_SUBTLE = new Color(255, 255, 255, 30);
    public static final Color TEXT_GOLD = new Color(255, 215, 0);
    public static final Color TEXT_FLAME = new Color(255, 80, 20);
    public static final Color TEXT_AMBER = new Color(255, 140, 50);
    public static final Color TEXT_CREAM = new Color(255, 230, 140);
    public static final Color TEXT_MUTED = new Color(180, 180, 190);
    public static final Color TEXT_LIGHT = new Color(210, 210, 220);

    // Font Presets
    public static final Font FONT_TITLE_LARGE = new Font("SansSerif", Font.BOLD, 46);
    public static final Font FONT_TITLE_MEDIUM = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BODY_BOLD = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_KEY = new Font("Monospaced", Font.BOLD, 12);

    private UiKit() {
        // Utility class
    }

    // Draws dark background with top glow
    public static void drawBackdropWithGlow(Graphics2D g, int width, int height) {
        g.setColor(BG_DARK);
        g.fillRect(0, 0, width, height);

        GradientPaint topGlow = new GradientPaint(
                0, 0, new Color(200, 50, 20, 75),
                0, 160, new Color(0, 0, 0, 0));
        g.setPaint(topGlow);
        g.fillRect(0, 0, width, 160);
    }

    // Draws a rounded modal panel
    public static void drawModalCard(Graphics2D g, int x, int y, int w, int h, Color borderColor) {
        g.setColor(PANEL_BG);
        g.fillRoundRect(x, y, w, h, 18, 18);
        g.setColor(borderColor);
        g.drawRoundRect(x, y, w, h, 18, 18);
    }

    // Draws difficulty/mode selection card
    public static void drawSelectionCard(Graphics2D g, int x, int y, int w, int h,
                                         String keyNum, String title, String subtitle,
                                         String[] bullets, String actionHint,
                                         Color baseColor, Color accentColor) {
        g.setColor(CARD_BG);
        g.fillRoundRect(x, y, w, h, 14, 14);

        g.setColor(baseColor);
        g.fillRoundRect(x, y, w, 6, 14, 14);
        g.fillRect(x, y + 3, w, 3);

        g.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 90));
        g.drawRoundRect(x, y, w, h, 14, 14);

        int badgeW = 34;
        int badgeH = 26;
        int badgeX = x + 16;
        int badgeY = y + 16;
        g.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 60));
        g.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 6, 6);
        g.setColor(accentColor);
        g.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 6, 6);
        g.setFont(FONT_KEY);
        Assets.drawCenteredText(g, keyNum, badgeX + badgeW / 2, badgeY + badgeH / 2);

        g.setFont(new Font("SansSerif", Font.BOLD, 17));
        g.setColor(Color.WHITE);
        g.drawString(title, badgeX + badgeW + 12, badgeY + 18);

        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(TEXT_MUTED);
        g.drawString(subtitle, badgeX + badgeW + 12, badgeY + 34);

        g.setFont(FONT_BODY);
        g.setColor(TEXT_LIGHT);
        int textY = y + 78;
        for (String bullet : bullets) {
            g.drawString(bullet, x + 18, textY);
            textY += 20;
        }

        int btnW = w - 32;
        int btnH = 32;
        int btnX = x + 16;
        int btnY = y + h - btnH - 14;
        g.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 120));
        g.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);
        g.setColor(accentColor);
        g.drawRoundRect(btnX, btnY, btnW, btnH, 8, 8);

        g.setFont(FONT_BODY_BOLD);
        g.setColor(Color.WHITE);
        Assets.drawCenteredText(g, actionHint, btnX + btnW / 2, btnY + btnH / 2);
    }

    // Draws a key badge and label
    public static void drawKeyControl(Graphics2D g, String key, String desc, int x, int y) {
        g.setFont(FONT_KEY);
        FontMetrics fm = g.getFontMetrics();
        int kw = fm.stringWidth(key) + 12;
        int kh = 18;

        g.setColor(new Color(45, 50, 70));
        g.fillRoundRect(x, y - 14, kw, kh, 6, 6);
        g.setColor(new Color(100, 120, 160));
        g.drawRoundRect(x, y - 14, kw, kh, 6, 6);

        g.setColor(TEXT_CREAM);
        g.drawString(key, x + 6, y);

        g.setFont(FONT_BODY);
        g.setColor(TEXT_LIGHT);
        g.drawString("  :  " + desc, x + kw, y);
    }

    // Draws a styled button
    public static void drawButton(Graphics2D g, int x, int y, int w, int h, String text,
                                  Color bg, Color border, Color textCol) {
        g.setColor(bg);
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setColor(border);
        g.drawRoundRect(x, y, w, h, 10, 10);
        g.setFont(FONT_SUBTITLE);
        g.setColor(textCol);
        Assets.drawCenteredText(g, text, x + w / 2, y + h / 2);
    }
}
