package tetris;
import javax.swing.*;
import java.awt.*;

/**
 * Side panel displaying score, level, lines cleared, and the next piece preview.
 * Demonstrates: Java Swing GUI, Encapsulation
 */
public class SidePanel extends JPanel {

    private static final int CELL_SIZE = 24;
    private static final Color BG        = new Color(20, 20, 35);
    private static final Color PANEL_BG  = new Color(30, 30, 50);
    private static final Color LABEL_FG  = new Color(150, 150, 180);
    private static final Color VALUE_FG  = new Color(230, 230, 255);
    private static final Color ACCENT    = new Color(0, 200, 220);

    private GameLogic game;

    public SidePanel(GameLogic game) {
        this.game = game;
        setPreferredSize(new Dimension(160, Board.ROWS * 30));
        setBackground(BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 20;

        // Title
        g2.setColor(ACCENT);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("TETRIS", 30, y);
        y += 30;

        drawDivider(g2, y); y += 15;

        // Score section
        y = drawStatBox(g2, "SCORE",  String.valueOf(game.getScoreManager().getScore()), y);
        y = drawStatBox(g2, "LEVEL",  String.valueOf(game.getScoreManager().getLevel()), y);
        y = drawStatBox(g2, "LINES",  String.valueOf(game.getScoreManager().getLinesCleared()), y);

        drawDivider(g2, y); y += 15;

        // Next piece label
        g2.setColor(LABEL_FG);
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.drawString("NEXT", 60, y); y += 15;

        drawNextPiece(g2, y); y += 4 * CELL_SIZE + 10;

        drawDivider(g2, y); y += 15;

        // Controls hint
        drawControls(g2, y);
    }

    private int drawStatBox(Graphics2D g2, String label, String value, int y) {
        // Background box
        g2.setColor(PANEL_BG);
        g2.fillRoundRect(10, y, 140, 44, 8, 8);

        g2.setColor(LABEL_FG);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(label, 20, y + 14);

        g2.setColor(VALUE_FG);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(value, 20, y + 36);

        return y + 54;
    }

    private void drawNextPiece(Graphics2D g2, int startY) {
        Tetromino next = game.getNextPiece();
        if (next == null) return;

        int[] shape = next.getShape();
        Color color = next.getColor();

        // Center the 4x4 preview grid in the panel
        int offsetX = (getWidth() - 4 * CELL_SIZE) / 2;
        int offsetY = startY + 5;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int bx = offsetX + col * CELL_SIZE;
                int by = offsetY + row * CELL_SIZE;
                if (shape[row * 4 + col] == 1) {
                    // Filled cell
                    g2.setColor(color);
                    g2.fillRoundRect(bx + 1, by + 1, CELL_SIZE - 2, CELL_SIZE - 2, 4, 4);
                    g2.setColor(color.brighter());
                    g2.fillRect(bx + 2, by + 2, CELL_SIZE - 4, 3);
                } else {
                    // Empty cell hint
                    g2.setColor(PANEL_BG);
                    g2.fillRect(bx + 1, by + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                }
            }
        }
    }

    private void drawDivider(Graphics2D g2, int y) {
        g2.setColor(new Color(60, 60, 90));
        g2.drawLine(10, y, getWidth() - 10, y);
    }

    private void drawControls(Graphics2D g2, int y) {
        g2.setColor(LABEL_FG);
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString("CONTROLS", 45, y); y += 14;

        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        String[][] controls = {
            {"← →", "Move"},
            {"↑ / Z", "Rotate"},
            {"↓", "Soft Drop"},
            {"Space", "Hard Drop"},
            {"P", "Pause"},
            {"R", "Restart"},
            {"Enter", "Start"}
        };
        for (String[] ctrl : controls) {
            g2.setColor(ACCENT);
            g2.drawString(ctrl[0], 14, y);
            g2.setColor(LABEL_FG);
            g2.drawString(ctrl[1], 65, y);
            y += 14;
        }
    }
}
