package game;

import game.ui.*;
import upgrade.Upgrade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public final class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    public static final int WIDTH = Restaurant.COLS * Restaurant.TILE;
    public static final int HEIGHT = Restaurant.ROWS * Restaurant.TILE + HudView.HUD_HEIGHT;

    public enum Screen {
        TITLE,
        DIFFICULTY_SELECT,
        PLAYING,
        PAUSED,
        SHOP,
        GAME_OVER
    }

    private final Timer timer;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private Restaurant restaurant = new Restaurant(currentDifficulty);
    private Screen screen = Screen.TITLE;
    private String shopMessage = "";
    private long lastTime;

    public GamePanel() {
        this.timer = new Timer(16, this); // ~60 FPS
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
    }

    public void start() {
        lastTime = System.nanoTime();
        timer.start();
        requestFocusInWindow();
        SoundManager.playBgm();
    }

    // ===================== Game Loop =====================

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        double dt = Math.min(0.05, (now - lastTime) / 1_000_000_000.0);
        lastTime = now;

        if (screen == Screen.PLAYING) {
            updateGame(dt);
        }
        repaint();
    }

    private void updateGame(double dt) {
        double dirX = 0;
        double dirY = 0;
        if (isDown(KeyEvent.VK_A) || isDown(KeyEvent.VK_LEFT)) dirX -= 1;
        if (isDown(KeyEvent.VK_D) || isDown(KeyEvent.VK_RIGHT)) dirX += 1;
        if (isDown(KeyEvent.VK_W) || isDown(KeyEvent.VK_UP)) dirY -= 1;
        if (isDown(KeyEvent.VK_S) || isDown(KeyEvent.VK_DOWN)) dirY += 1;

        if (dirX != 0 && dirY != 0) {
            dirX *= 0.7071;
            dirY *= 0.7071;
        }

        restaurant.update(dt, dirX, dirY);

        if (restaurant.isGameOver()) {
            screen = Screen.GAME_OVER;
            SoundManager.stopBgm();
        } else if (restaurant.isDayOver()) {
            screen = restaurant.reachedTarget() ? Screen.SHOP : Screen.GAME_OVER;
            shopMessage = "";
        }
    }

    private boolean isDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    // ===================== Keyboard Input =====================

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        boolean firstPress = pressedKeys.add(key);
        if (!firstPress) return;

        switch (screen) {
            case TITLE -> handleTitleKey(key);
            case DIFFICULTY_SELECT -> handleDifficultyKey(key);
            case PLAYING -> handlePlayingKey(key);
            case PAUSED -> handlePausedKey(key);
            case SHOP -> handleShopKey(key);
            case GAME_OVER -> handleGameOverKey(key);
        }
    }

    private void handleTitleKey(int key) {
        if (key == KeyEvent.VK_1 || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
            screen = Screen.DIFFICULTY_SELECT;
        }
    }

    private void handleDifficultyKey(int key) {
        Difficulty diff = switch (key) {
            case KeyEvent.VK_1 -> Difficulty.EASY;
            case KeyEvent.VK_2 -> Difficulty.NORMAL;
            case KeyEvent.VK_3 -> Difficulty.HARD;
            default -> null;
        };

        if (diff != null) {
            currentDifficulty = diff;
            restaurant = new Restaurant(currentDifficulty);
            screen = Screen.PLAYING;
            SoundManager.playBgm();
        } else if (key == KeyEvent.VK_ESCAPE) {
            screen = Screen.TITLE;
        }
    }

    private void handlePlayingKey(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            pressedKeys.clear();
            screen = Screen.PAUSED;
            return;
        }

        if (key == KeyEvent.VK_E || key == KeyEvent.VK_SPACE) restaurant.interact();
        if (key == KeyEvent.VK_C) restaurant.cyclePlateColor();
        if (key == KeyEvent.VK_Q) restaurant.alternateInteract();
    }

    private void handlePausedKey(int key) {
        if (key == KeyEvent.VK_Y || key == KeyEvent.VK_ENTER) {
            SoundManager.stopBgm();
            screen = Screen.TITLE;
        } else if (key == KeyEvent.VK_N || key == KeyEvent.VK_ESCAPE) {
            screen = Screen.PLAYING;
            lastTime = System.nanoTime();
        }
    }

    private void handleShopKey(int key) {
        if (key == KeyEvent.VK_ESCAPE) {
            screen = Screen.TITLE;
            return;
        }
        if (key == KeyEvent.VK_ENTER) {
            restaurant.nextDay();
            screen = Screen.PLAYING;
            return;
        }

        int index = key - KeyEvent.VK_1;
        List<Upgrade> upgrades = restaurant.getUpgrades();
        if (index >= 0 && index < upgrades.size()) {
            Upgrade upgrade = upgrades.get(index);
            if (upgrade.isMaxed()) {
                shopMessage = upgrade.getName() + " is already max level.";
            } else if (upgrade.buy(restaurant)) {
                shopMessage = "Bought " + upgrade.getName() + "!";
            } else {
                shopMessage = "Not enough money.";
            }
        }
    }

    private void handleGameOverKey(int key) {
        if (key == KeyEvent.VK_R) {
            restaurant = new Restaurant(currentDifficulty);
            screen = Screen.PLAYING;
            SoundManager.playBgm();
        } else if (key == KeyEvent.VK_ESCAPE) {
            SoundManager.stopBgm();
            screen = Screen.TITLE;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // ===================== Mouse Input =====================

    @Override
    public void mouseClicked(MouseEvent e) {
        if (screen == Screen.TITLE) {
            screen = Screen.DIFFICULTY_SELECT;
        } else if (screen == Screen.PAUSED) {
            int mx = e.getX();
            int my = e.getY();
            if (PauseView.isQuitButtonClicked(mx, my, WIDTH, HEIGHT)) {
                SoundManager.stopBgm();
                screen = Screen.TITLE;
            } else if (PauseView.isResumeButtonClicked(mx, my, WIDTH, HEIGHT)) {
                screen = Screen.PLAYING;
                lastTime = System.nanoTime();
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    // ===================== Rendering =====================

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // The kitchen world is rendered below the HUD
        Graphics2D world = (Graphics2D) g.create();
        world.translate(0, HudView.HUD_HEIGHT);
        restaurant.draw(world);
        world.dispose();

        // Top HUD Bar
        HudView.draw(g, WIDTH, restaurant);

        // Screen Overlays
        switch (screen) {
            case TITLE -> TitleView.draw(g, WIDTH, HEIGHT);
            case DIFFICULTY_SELECT -> DifficultyView.draw(g, WIDTH, HEIGHT);
            case PAUSED -> PauseView.draw(g, WIDTH, HEIGHT);
            case SHOP -> ShopView.draw(g, WIDTH, HEIGHT, restaurant, shopMessage);
            case GAME_OVER -> GameOverView.draw(g, WIDTH, HEIGHT, restaurant);
            case PLAYING -> { }
        }
    }
}
