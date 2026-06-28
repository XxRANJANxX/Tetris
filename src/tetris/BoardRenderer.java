package tetris;
import javax.swing.*;
import java.awt.*;

/**
 * Renders the game board, current piece, ghost piece, and overlays.
 * Demonstrates: Java Swing GUI, Custom Rendering
 */
public class BoardRenderer extends JPanel {

    private static final int CELL_SIZE = 30;
    private static final int BORDER = 1;

    private GameLogic game;

    private static final Color BG_COLOR       = new Color(15, 15, 25);
    private static final Color GRID_COLOR     = new Color(35, 35, 55);
    private static final Color GHOST_COLOR    = new Color(255, 255, 255, 40);
    private static final Color GHOST_BORDER   = new Color(255, 255, 255, 100);
    private static final Color OVERLAY_BG     = new Color(0, 0, 0, 170);
    private static final Color TITLE_COLOR    = new Color(0, 230, 230);
    private static final Color SUBTITLE_COLOR = new Color(200, 200, 200);

    public BoardRenderer(GameLogic game) {
        this.game = game;
        setPreferredSize(new Dimension(Board.COLS * CELL_SIZE, Board.ROWS * CELL_SIZE));
        setBackground(BG_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawLockedCells(g2);

        GameLogic.GameState state = game.getState();
        if (state == GameLogic.GameState.PLAYING || state == GameLogic.GameState.PAUSED) {
            drawGhostPiece(g2);
            drawCurrentPiece(g2);
        }

        if (state == GameLogic.GameState.IDLE)      drawOverlay(g2, "TETRIS", "Press ENTER to Start", TITLE_COLOR);
        if (state == GameLogic.GameState.PAUSED)    drawOverlay(g2, "PAUSED", "Press P to Resume", Color.YELLOW);
        if (state == GameLogic.GameState.GAME_OVER) drawOverlay(g2, "GAME OVER", "Press R to Restart", Color.RED);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(GRID_COLOR);
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                g2.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawLockedCells(Graphics2D g2) {
        Board board = game.getBoard();
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                Color color = board.getCell(r, c);
                if (color != null) {
                    drawCell(g2, c, r, color, true);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics2D g2) {
        Tetromino piece = game.getCurrentPiece();
        if (piece == null) return;
        int[] shape = piece.getShape();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (shape[row * 4 + col] == 0) continue;
                int bx = piece.getX() + col;
                int by = piece.getY() + row;
                if (by >= 0) drawCell(g2, bx, by, piece.getColor(), true);
            }
        }
    }

    private void drawGhostPiece(Graphics2D g2) {
        Tetromino piece = game.getCurrentPiece();
        if (piece == null) return;
        int ghostY = game.getGhostY();
        if (ghostY == piece.getY()) return; // no gap to show ghost

        int[] shape = piece.getShape();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (shape[row * 4 + col] == 0) continue;
                int bx = piece.getX() + col;
                int by = ghostY + row;
                if (by >= 0) {
                    // Fill ghost
                    g2.setColor(GHOST_COLOR);
                    g2.fillRect(bx * CELL_SIZE + BORDER, by * CELL_SIZE + BORDER,
                            CELL_SIZE - BORDER * 2, CELL_SIZE - BORDER * 2);
                    // Ghost border
                    g2.setColor(GHOST_BORDER);
                    g2.drawRect(bx * CELL_SIZE + BORDER, by * CELL_SIZE + BORDER,
                            CELL_SIZE - BORDER * 2 - 1, CELL_SIZE - BORDER * 2 - 1);
                }
            }
        }
    }

    private void drawCell(Graphics2D g2, int col, int row, Color color, boolean shaded) {
        int x = col * CELL_SIZE + BORDER;
        int y = row * CELL_SIZE + BORDER;
        int w = CELL_SIZE - BORDER * 2;
        int h = CELL_SIZE - BORDER * 2;

        // Main fill
        g2.setColor(color);
        g2.fillRect(x, y, w, h);

        if (shaded) {
            // Highlight (top-left)
            g2.setColor(color.brighter().brighter());
            g2.fillRect(x, y, w, 3);
            g2.fillRect(x, y, 3, h);

            // Shadow (bottom-right)
            g2.setColor(color.darker().darker());
            g2.fillRect(x, y + h - 3, w, 3);
            g2.fillRect(x + w - 3, y, 3, h);
        }
    }

    private void drawOverlay(Graphics2D g2, String title, String subtitle, Color titleColor) {
        int w = getWidth(), h = getHeight();

        // Semi-transparent background
        g2.setColor(OVERLAY_BG);
        g2.fillRoundRect(w / 2 - 130, h / 2 - 60, 260, 120, 20, 20);

        // Title
        g2.setColor(titleColor);
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (w - fm.stringWidth(title)) / 2, h / 2 - 10);

        // Subtitle
        g2.setColor(SUBTITLE_COLOR);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        fm = g2.getFontMetrics();
        g2.drawString(subtitle, (w - fm.stringWidth(subtitle)) / 2, h / 2 + 25);
    }
}
