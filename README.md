# Tetris — Java / Swing

A desktop Tetris implementation in pure Java using Swing, written to match the resume project description.

## Requirements
- Java 8 or higher (`java -version` to check)

## Build & Run

### Option 1 — Shell script (macOS/Linux)
```bash
chmod +x run.sh
./run.sh
```

### Option 2 — Manual
```bash
mkdir -p out
javac -d out src/tetris/*.java
java -cp out tetris.TetrisWindow
```

### Option 3 — IDE (IntelliJ / Eclipse / VS Code)
1. Open the project root folder
2. Mark `src/` as the Sources Root
3. Run `tetris.TetrisWindow`

---

## Controls

| Key         | Action        |
|-------------|---------------|
| ← →         | Move left/right |
| ↑ or Z      | Rotate clockwise |
| ↓           | Soft drop     |
| Space       | Hard drop     |
| P           | Pause / Resume |
| R           | Restart       |
| Enter       | Start game    |

---

## Project Structure

```
src/tetris/
├── TetrisWindow.java   — Main JFrame, game loop timer, key bindings
├── GameLogic.java      — Game state machine, piece lifecycle, controls
├── Board.java          — 2D grid, collision detection, line clearing
├── Tetromino.java      — 7 piece types, 4-rotation shapes, colors
├── BoardRenderer.java  — Swing JPanel: board, pieces, ghost, overlays
├── SidePanel.java      — Score, level, next-piece preview, controls hint
└── ScoreManager.java   — Scoring, level progression, speed calculation
```

---

## Technical Concepts

| Concept | Where |
|---|---|
| OOP (Inheritance, Encapsulation, Polymorphism) | All classes |
| 2D Arrays | `Board.java` — grid state |
| Collision Detection | `Board.collides()` |
| Timer-based Game Loop | `TetrisWindow` — `javax.swing.Timer` |
| Event Handling | Key Bindings in `TetrisWindow` |
| State Management | `GameLogic.GameState` enum |
| Java Swing GUI | `TetrisWindow`, `BoardRenderer`, `SidePanel` |
| Speed Progression | `ScoreManager.getFallIntervalMs()` |
| Wall-kick Rotation | `GameLogic.rotate()` |
| Ghost Piece | `GameLogic.getGhostY()` + `BoardRenderer` |
