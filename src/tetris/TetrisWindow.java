package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main application window.
 * Wires together the game loop (Timer), keyboard input (KeyListener),
 * and the rendering components (BoardRenderer, SidePanel).
 * Demonstrates: Event Handling, Timer-based Animation, Java Swing GUI
 */
public class TetrisWindow extends JFrame implements GameLogic.GameListener {

    private GameLogic game;
    private BoardRenderer boardRenderer;
    private SidePanel sidePanel;
    private Timer gameTimer;

    public TetrisWindow() {
        game = new GameLogic(this);
        boardRenderer = new BoardRenderer(game);
        sidePanel = new SidePanel(game);

        setupUI();
        setupKeyBindings();
        setupTimer();

        setVisible(true);
    }

    private void setupUI()
    {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(4, 0));
        mainPanel.setBackground(new Color(10, 10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mainPanel.add(boardRenderer, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // center on screen
    }

    /**
     * Keyboard controls using Key Bindings (preferred over KeyListener for focus independence).
     * Demonstrates: Event-driven programming, Event Handling
     */
    private void setupKeyBindings() {
        JPanel content = (JPanel) getContentPane();
        InputMap im = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = content.getActionMap();

        bindKey(im, am, KeyEvent.VK_LEFT,   "left",       () -> game.moveLeft());
        bindKey(im, am, KeyEvent.VK_RIGHT,  "right",      () -> game.moveRight());
        bindKey(im, am, KeyEvent.VK_DOWN,   "softDrop",   () -> game.softDrop());
        bindKey(im, am, KeyEvent.VK_SPACE,  "hardDrop",   () -> game.hardDrop());
        bindKey(im, am, KeyEvent.VK_UP,     "rotate",     () -> game.rotate());
        bindKey(im, am, KeyEvent.VK_Z,      "rotateZ",    () -> game.rotate());
        bindKey(im, am, KeyEvent.VK_P,      "pause",      () -> game.togglePause());
        bindKey(im, am, KeyEvent.VK_R,      "restart",    () -> { game.restartGame(); restartTimer(); });
        bindKey(im, am, KeyEvent.VK_ENTER,  "start",      () -> { game.startGame(); restartTimer(); });
    }

    private void bindKey(InputMap im, ActionMap am, int keyCode, String name, Runnable action) {
        im.put(KeyStroke.getKeyStroke(keyCode, 0), name);
        am.put(name, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }

    /**
     * Game timer drives automatic block falling.
     * Interval is refreshed after each tick to support speed progression.
     * Demonstrates: Game Loop Design, Timer-based Animation
     */
    private void setupTimer() {
        gameTimer = new Timer(game.getScoreManager().getFallIntervalMs(), e -> {
            game.tick();
            // Adjust timer interval as level changes
            int newInterval = game.getScoreManager().getFallIntervalMs();
            if (gameTimer.getDelay() != newInterval) {
                gameTimer.setDelay(newInterval);
            }
            // Stop timer on game over
            if (game.getState() == GameLogic.GameState.GAME_OVER) {
                gameTimer.stop();
            }
        });
    }

    private void restartTimer() {
        gameTimer.stop();
        gameTimer.setDelay(game.getScoreManager().getFallIntervalMs());
        gameTimer.start();
    }

    // GameListener callbacks

    @Override
    public void onStateChanged() {
        boardRenderer.repaint();
        sidePanel.repaint();
    }

    @Override
    public void onLinesCleared(int lines) {
        // Flash effect or sound could go here
    }

    public static void main(String[] args) {
        // Use system look and feel, run on EDT
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(TetrisWindow::new);
    }
}
