package tetris;

/**
 * Tracks score, level, and lines cleared.
 * Demonstrates: Encapsulation, State Management
 */

public class ScoreManager {

    private static final int[] LINE_POINTS = {0, 100, 300, 500, 800};

    private int score;
    private int level;
    private int linesCleared ;
    private int linesUntilNextLevel ;

    private static int LINES_PER_LEVEL = 10;

    public ScoreManager() {
        reset();
    }

    public void addLines(int lines) {

        if (lines <= 0)
            return;

        int points = LINE_POINTS[Math.min(lines, 4)] * level;
        score += points;
        linesCleared += lines;
        linesUntilNextLevel -= lines;

        if (linesUntilNextLevel <= 0) {
            level++;
            linesUntilNextLevel = LINES_PER_LEVEL;
        }
    }

    public int getFallIntervalMs() {
        // Starts at 800ms, decreases by ~70ms per level, minimum 80ms
        return Math.max(80, 800 - (level - 1) * 70);
    }

    public void reset() {
        score = 0;
        level = 1;
        linesCleared = 0;
        linesUntilNextLevel = LINES_PER_LEVEL;
    }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLinesCleared() { return linesCleared; }
}
