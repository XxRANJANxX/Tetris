package tetris;

import java.util.Random;

/**
 * Central game logic controller.
 * Manages the game loop state, piece lifecycle, and coordinates Board + ScoreManager.
 * Demonstrates: Game Loop Design, State Management, OOP
 */
public class GameLogic {

    public enum GameState { IDLE, PLAYING, PAUSED, GAME_OVER }

    private Board board;
    private ScoreManager scoreManager;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private GameState state;
    private Random random;

    // Listener so the renderer/UI can be notified of state changes
    public interface GameListener {
        void onStateChanged();
        void onLinesCleared(int lines);
    }

    private GameListener listener;

    public GameLogic(GameListener listener) {
        this.listener = listener;
        board = new Board();
        scoreManager = new ScoreManager();
        random = new Random();
        state = GameState.IDLE;
    }

    // Game lifecycle

    public void startGame() {
        board.reset();
        scoreManager.reset();
        nextPiece = randomPiece();
        spawnNextPiece();
        state = GameState.PLAYING;
        notifyStateChange();
    }

    public void togglePause() {
        if (state == GameState.PLAYING) state = GameState.PAUSED;
        else if (state == GameState.PAUSED) state = GameState.PLAYING;
        notifyStateChange();
    }

    public void restartGame() {
        startGame();
    }

    // Piece spawning

    private void spawnNextPiece() {
        currentPiece = nextPiece;
        nextPiece = randomPiece();

        // If the newly spawned piece immediately collides → game over
        if (board.collides(currentPiece.getShape(), currentPiece.getX(), currentPiece.getY())) {
            state = GameState.GAME_OVER;
            notifyStateChange();
        }
    }

    private Tetromino randomPiece() {
        Tetromino.Type[] types = Tetromino.Type.values();
        return new Tetromino(types[random.nextInt(types.length)]);
    }

    // Timer-driven automatic drop

    /**
     * Called by the game timer on each tick.
     * Moves the piece down; locks it if it can't move further.
     * Demonstrates: Timer-based Animation, Game Loop Design
     */
    public void tick() {
        if (state != GameState.PLAYING) return;
        if (!tryMoveDown()) {
            lockAndSpawn();
        }
        notifyStateChange();
    }

    //  Player controls

    public void moveLeft() {
        if (state != GameState.PLAYING) return;
        currentPiece.moveLeft();
        if (board.collides(currentPiece.getShape(), currentPiece.getX(), currentPiece.getY())) {
            currentPiece.moveRight(); // undo
        }
        notifyStateChange();
    }

    public void moveRight() {
        if (state != GameState.PLAYING) return;
        currentPiece.moveRight();
        if (board.collides(currentPiece.getShape(), currentPiece.getX(), currentPiece.getY())) {
            currentPiece.moveLeft(); // undo
        }
        notifyStateChange();
    }

    public void softDrop() {
        if (state != GameState.PLAYING) return;
        if (!tryMoveDown()) {
            lockAndSpawn();
        }
        notifyStateChange();
    }

    public void hardDrop() {
        if (state != GameState.PLAYING) return;
        while (tryMoveDown()) { /* drop all the way */ }
        lockAndSpawn();
        notifyStateChange();
    }

    /**
     * Rotates the piece clockwise with wall-kick: tries nudging left/right if blocked.
     * Demonstrates: Rotation with boundary and collision checks
     */
    public void rotate() {
        if (state != GameState.PLAYING) return;
        currentPiece.rotateClockwise();
        int[] shape = currentPiece.getShape();
        int px = currentPiece.getX();
        int py = currentPiece.getY();

        if (!board.collides(shape, px, py)) {
            notifyStateChange();
            return;
        }
        // Wall kick: try nudging left or right

        int[] kicks = {-1, 1, -2, 2};
        for (int kick : kicks) {
            if (!board.collides(shape, px + kick, py)) {
                currentPiece.setX(px + kick);
                notifyStateChange();
                return;
            }
        }
        // All kicks failed , undo rotation
        currentPiece.rotateCounterClockwise();
    }

    // Internal helpers

    private boolean tryMoveDown() {
        currentPiece.moveDown();
        if (board.collides(currentPiece.getShape(), currentPiece.getX(), currentPiece.getY())) {
            currentPiece.moveUp(); // undo
            return false;
        }
        return true;
    }

    private void lockAndSpawn() {
        board.lockPiece(currentPiece);
        int lines = board.clearLines();
        if (lines > 0) {
            scoreManager.addLines(lines);
            if (listener != null) listener.onLinesCleared(lines);
        }
        if (board.isTopReached()) {
            state = GameState.GAME_OVER;
            notifyStateChange();
            return;
        }
        spawnNextPiece();
    }

    private void notifyStateChange() {
        if (listener != null) listener.onStateChanged();
    }

    /**
     * Calculates the ghost piece Y position (where piece would land).
     */
    public int getGhostY() {
        int ghostY = currentPiece.getY();
        while (!board.collides(currentPiece.getShape(), currentPiece.getX(), ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    public Board getBoard()              { return board; }
    public ScoreManager getScoreManager(){ return scoreManager; }
    public Tetromino getCurrentPiece()   { return currentPiece; }
    public Tetromino getNextPiece()      { return nextPiece; }
    public GameState getState()          { return state; }
    public boolean isPlaying()           { return state == GameState.PLAYING; }
}
