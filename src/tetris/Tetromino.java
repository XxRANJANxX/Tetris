package tetris;
import java.awt.Color;

/**
 * Represents a Tetromino piece with its shape, color, and rotation states.
 * Demonstrates: Encapsulation, OOP design
 */
public class Tetromino {

    public enum Type {
        I, O, T, S, Z, J, L
    }
    private static final int[][][] SHAPES = {
        // I
        {
            {0,0,0,0, 1,1,1,1, 0,0,0,0, 0,0,0,0},
            {0,0,1,0, 0,0,1,0, 0,0,1,0, 0,0,1,0},
            {0,0,0,0, 0,0,0,0, 1,1,1,1, 0,0,0,0},
            {0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0}
        },
        // O
        {
            {0,1,1,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,1,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,1,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,1,0, 0,1,1,0, 0,0,0,0, 0,0,0,0}
        },
        // T
        {
            {0,1,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,1,0, 0,1,0,0, 0,0,0,0},
            {0,0,0,0, 1,1,1,0, 0,1,0,0, 0,0,0,0},
            {0,1,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0}
        },
        // S
        {
            {0,1,1,0, 1,1,0,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,1,0, 0,0,1,0, 0,0,0,0},
            {0,0,0,0, 0,1,1,0, 1,1,0,0, 0,0,0,0},
            {1,0,0,0, 1,1,0,0, 0,1,0,0, 0,0,0,0}
        },
        // Z
        {
            {1,1,0,0, 0,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,0,1,0, 0,1,1,0, 0,1,0,0, 0,0,0,0},
            {0,0,0,0, 1,1,0,0, 0,1,1,0, 0,0,0,0},
            {0,1,0,0, 1,1,0,0, 1,0,0,0, 0,0,0,0}
        },
        // L
        {
            {1,0,0,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,1,0, 0,1,0,0, 0,1,0,0, 0,0,0,0},
            {0,0,0,0, 1,1,1,0, 0,0,1,0, 0,0,0,0},
            {0,1,0,0, 0,1,0,0, 1,1,0,0, 0,0,0,0}
        },
        // J
        {
            {0,0,1,0, 1,1,1,0, 0,0,0,0, 0,0,0,0},
            {0,1,0,0, 0,1,0,0, 0,1,1,0, 0,0,0,0},
            {0,0,0,0, 1,1,1,0, 1,0,0,0, 0,0,0,0},
            {1,1,0,0, 0,1,0,0, 0,1,0,0, 0,0,0,0}
        }
    };

    private static final Color[] COLORS = {
        new Color(0 , 240, 240),   // I - Cyan
        new Color(240, 240, 0),   // O - Yellow
        new Color(160, 0, 240),   // T - Purple
        new Color(0, 240, 0),     // S - Green
        new Color(240, 0, 0),     // Z - Red
        new Color(0, 0, 240),     // L - Blue
        new Color(240, 160, 0)    // J - Orange
    };

    private Type type;
    private int rotation;
    private int x, y; // board position (column, row)


    public Tetromino(Type type) {
        this.type = type;
        this.rotation = 0;
        this.x = 3; // start near center
        this.y = 0;
    }

    public int[] getShape() {
        return SHAPES[type.ordinal()][rotation];
    }

    public void rotateClockwise() {
        rotation = (rotation + 1) % 4;
    }

    public void rotateCounterClockwise() {
        rotation = (rotation + 3) % 4;
    }

    public Color getColor() {
        return COLORS[type.ordinal()];
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void moveLeft()  { x--; }
    public void moveRight() { x++; }
    public void moveDown()  { y++; }
    public void moveUp()    { y--; }
}
