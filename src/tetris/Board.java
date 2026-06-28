package tetris;
import java.awt.Color;

/**
 * Manages the game board state using a 2D array.
 * Handles collision detection, line clearing, and board rendering data.
 * Demonstrates: 2D Arrays, Collision Detection, Encapsulation
 */
public class Board {

    public static final int COLS = 15;
    public static final int ROWS = 15;

    // cells: null = empty, Color = filled
    private Color[][] grid;

    public Board() {
        grid = new Color[ROWS][COLS];
    }

    public Color getCell(int row, int col) {

        if (row < 0 || row >= ROWS || col < 0 || col >= COLS)
            return null;

        return grid[row][col];
    }

    public boolean isOccupied(int row, int col) {
        if (col < 0 || col >= COLS)
            return true;  // wall
        if (row >= ROWS)
            return true;   // floor
        if (row < 0)
            return false;  // above board is OK

        return grid[row][col] != null;
    }
    /**
     * Checks whether placing a tetromino at (px, py) with given shape causes a collision.
     * Demonstrates: Collision Detection
     */
    public boolean collides(int[] shape, int px, int py) {

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                if (shape[row * 4 + col] == 0)
                    continue;

                int boardRow = py + row;
                int boardCol = px + col;

                if (isOccupied(boardRow, boardCol))
                    return true;
            }
        }
        return false;
    }

    /**
     * Locks a tetromino into the board grid.
     */

    public void lockPiece(Tetromino piece) {

        int[] shape = piece.getShape();
        Color color = piece.getColor();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {

                if (shape[row * 4 + col] == 0)
                    continue;

                int boardRow = piece.getY() + row;
                int boardCol = piece.getX() + col;

                if (boardRow >= 0 && boardRow < ROWS && boardCol >= 0 && boardCol < COLS) {
                    grid[boardRow][boardCol] = color;
                }
            }
        }
    }

    /**
     * Clears completed lines and drops rows above.
     * @return number of lines cleared
     * Demonstrates: Line Completion and Removal
     */
    public int clearLines() {
        int cleared = 0;

        for (int row = ROWS - 1; row >= 0; row--) {

            if (isLineFull(row)) {
                removeLine(row);
                row++; // re-check same row index after shift
                cleared++;
            }
        }
        return cleared;
    }

    private boolean isLineFull(int row) {
        for (int col = 0; col < COLS; col++) {
            if (grid[row][col] == null)
                return false;
        }
        return true;
    }

    private void removeLine(int clearedRow) {
        // Shift every row above down by one
        for (int row = clearedRow; row > 0; row--) {

            grid[row] = grid[row - 1].clone();
        }

        grid[0] = new Color[COLS]; // new empty top row
    }

    public boolean isTopReached() {

        for (int col = 0; col < COLS; col++) {
            if (grid[0][col] != null || grid[1][col] != null)
                return true;
        }
        return false;
    }

    public void reset() {
        grid = new Color[ROWS][COLS];
    }
}
