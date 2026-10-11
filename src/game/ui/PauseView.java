package game.ui;

import game.Assets;

import java.awt.*;

/**
 * Renders the pause menu and quit confirmation dialog.
 * Provides hit testing for mouse clicks on the Quit and Resume buttons.
 */
public final class PauseView {

    public static final int MODAL_W = 500;
    public static final int MODAL_H = 260;
    public static final int BTN_W = 195;
    public static final int BTN_H = 46;

    private PauseView() {
    }

    public static void draw(Graphics2D g, int width, int height) {
        // Dark frosted backdrop over the frozen kitchen
        g.setColor(new Color(0, 0, 0, 185));
        g.fillRect(0, 0, width, height);

        int modalX = (width - MODAL_W) / 2;
        int modalY = (height - MODAL_H) / 2;

        // Modal card
        UiKit.drawModalCard(g, modalX, modalY, MODAL_W, MODAL_H, new Color(255, 120, 60, 120));

        // Header Title
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.setColor(UiKit.TEXT_GOLD);
        Assets.drawCenteredText(g, "PAUSED  •  QUIT SHIFT?", width / 2, modalY + 45);

        // Subtitle prompt
        g.setFont(new Font("SansSerif", Font.PLAIN, 15));
        g.setColor(Color.WHITE);
        Assets.drawCenteredText(g, "Are you sure you want to return to the Title Menu?", width / 2, modalY + 85);

        g.setFont(UiKit.FONT_BODY);
        g.setColor(new Color(220, 170, 170));
        Assets.drawCenteredText(g, "Current shift earnings and recipe progress will be discarded.", width / 2, modalY + 115);

        // Button dimensions
        int btnY = modalY + 155;
        int btn1X = modalX + 38;
        int btn2X = modalX + MODAL_W - BTN_W - 38;

        // Button 1: YES, QUIT (Crimson)
        UiKit.drawButton(g, btn1X, btnY, BTN_W, BTN_H, "[ Y ]  YES, QUIT",
                new Color(180, 40, 40), new Color(255, 120, 120), Color.WHITE);

        // Button 2: RESUME (Forest Green)
        UiKit.drawButton(g, btn2X, btnY, BTN_W, BTN_H, "[ ESC / N ]  RESUME",
                new Color(40, 140, 60), new Color(120, 240, 140), Color.WHITE);

        // Keyboard hint
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(new Color(170, 170, 180));
        Assets.drawCenteredText(g, "Click button or press [Y] / [ENTER] to Quit, [ESC] / [N] to Resume",
                width / 2, modalY + 230);
    }

    public static boolean isQuitButtonClicked(int mx, int my, int width, int height) {
        int modalX = (width - MODAL_W) / 2;
        int modalY = (height - MODAL_H) / 2;
        int btnY = modalY + 155;
        int btn1X = modalX + 38;
        return (mx >= btn1X && mx <= btn1X + BTN_W && my >= btnY && my <= btnY + BTN_H);
    }

    public static boolean isResumeButtonClicked(int mx, int my, int width, int height) {
        int modalX = (width - MODAL_W) / 2;
        int modalY = (height - MODAL_H) / 2;
        int btnY = modalY + 155;
        int btn2X = modalX + MODAL_W - BTN_W - 38;
        return (mx >= btn2X && mx <= btn2X + BTN_W && my >= btnY && my <= btnY + BTN_H);
    }
}
