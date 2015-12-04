package br.francischini.a2048.game;

import android.graphics.Point;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
public class Manager {
    int size;
    int startTiles = 2;
    boolean keepPlaying;
    boolean over;
    boolean won;
    int score;
    Grid grid;

    public Manager(int size) {
        this.size = size;
        this.setup();
    }

    public Grid getGrid() {
        return grid;
    }


    // Restart the game
    public void restart() {
        //this.storageManager.clearGameState();
        //this.actuator.continueGame(); // Clear the game won/lost message
        this.setup();
    }

    ;

    // Keep playing after winning (allows going over 2048)
    public void keepPlaying() {
        this.keepPlaying = true;
        //this.actuator.continueGame(); // Clear the game won/lost message
    }

    ;

    // Return true if the game is lost, or has won and the user hasn't kept playing
    public boolean isGameTerminated() {
        return this.over || (this.won && !this.keepPlaying);
    }

    ;

    // Set up the game
    public void setup() {
        /*boolean previousState = this.storageManager.getGameState();

        // Reload the game from a previous game if present
        if (previousState) {
            this.grid        = new Grid(previousState.grid.size,
                    previousState.grid.cells); // Reload grid
            this.score       = previousState.score;
            this.over        = previousState.over;
            this.won         = previousState.won;
            this.keepPlaying = previousState.keepPlaying;
        } else {
            this.grid        = new Grid(this.size);
            this.score       = 0;
            this.over        = false;
            this.won         = false;
            this.keepPlaying = false;

            // Add the initial tiles
            this.addStartTiles();
        }*/

        this.grid = new Grid(this.size);
        this.score = 0;
        this.over = false;
        this.won = false;
        this.keepPlaying = false;

        // Add the initial tiles
        this.addStartTiles();

        // Update the actuator
        this.actuate();
    }

    ;

    // Set up the initial tiles to start the game with
    public void addStartTiles() {
        for (int i = 0; i < this.startTiles; i++) {
            this.addRandomTile();
        }
    }

    ;

    // Adds a tile in a random position
    public void addRandomTile() {
        if (this.grid.isAnyCellAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Grid.Cell cell = this.grid.randomAvailableCell();
            Tile tile = new Tile(cell.getX(), cell.getY(), value);

            this.grid.insertTile(tile);
        }
    }

    ;

    // Sends the updated grid to the actuator
    public void actuate() {
        /*if (this.storageManager.getBestScore() < this.score) {
            this.storageManager.setBestScore(this.score);
        }

        // Clear the state when the game is over (game over only, not win)
        if (this.over) {
            this.storageManager.clearGameState();
        } else {
            this.storageManager.setGameState(this.serialize());
        }

        this.actuator.actuate(this.grid, {
                score:      this.score,
                over:       this.over,
                won:        this.won,
                bestScore:  this.storageManager.getBestScore(),
                terminated: this.isGameTerminated()
        });*/

    }

    ;

    // Save all tile positions and remove merger info
    public void prepareTiles() {
        this.grid.prepareTiles();
    }

    ;

    // Move a tile and its representation
    public void moveTile(Tile tile, Tile.Position cell) {
        this.grid.cells[tile.getX()][tile.getY()] = null;
        this.grid.cells[cell.x][cell.y] = tile;
        tile.updatePosition(cell);
    }

    ;

    // Get the vector representing the chosen direction
    public Point getVector(int direction) {
        switch (direction) {
            case 0:
                return new Point(0, -1); // Up
            case 1:
                return new Point(1, 0); // Right
            case 2:
                return new Point(0, 1); // Down
            case 3:
                return new Point(-1, 0); // Left
        }

        return null;
    }


    public class Traversals {
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
    }

    // Build a list of positions to traverse in the right order
    public Traversals buildTraversals(Point vector) {
        Traversals traversals = new Traversals();

        for (int pos = 0; pos < this.size; pos++) {
            traversals.x.add(pos);
            traversals.y.add(pos);
        }

        // Always traverse from the farthest cell in the chosen direction
        if (vector.x == 1) Collections.reverse(traversals.x);
        if (vector.y == 1) Collections.reverse(traversals.y);

        return traversals;
    }

    public Pair<Grid.Cell, Grid.Cell> findFarthestPosition(Grid.Cell cell, Point vector) {
        Grid.Cell previous;

        // Progress towards the vector direction until an obstacle is found
        do {
            previous = cell;
            cell = new Grid.Cell(previous.getX() + vector.x, previous.getY() + vector.y);
        } while (this.grid.withinBounds(cell.getX(), cell.getY()) &&
                this.grid.isCellAvailable(cell.getX(), cell.getY()));

        //farthest: previous
        //next: cell
        return new Pair<>(previous, cell);
    };

    public boolean positionsEqual (Tile.Position first, Tile.Position second) {
        return first.x == second.x && first.y == second.y;
    };

    // Move tiles on the grid in the specified direction
    public void move (int direction) {
        // 0: up, 1: right, 2: down, 3: left

        if (this.isGameTerminated()) return; // Don't do anything if the game's over

        Grid.Cell cell;
        Tile tile;

        Point vector     = this.getVector(direction);
        Traversals traversals = this.buildTraversals(vector);
        boolean moved      = false;

        // Save the current tile positions and remove merger information
        this.prepareTiles();

        // Traverse the grid in the right direction and move tiles
        for(int x : traversals.x) {
            for(int y : traversals.y) {
                cell = new Grid.Cell(x, y);
                tile = grid.getCellContent(x, y);


                if (tile != null) {
                    Pair<Grid.Cell, Grid.Cell> pair = this.findFarthestPosition(cell, vector);
                    Grid.Cell positionsFarthest = pair.first;
                    Grid.Cell positionsNext = pair.second;

                    Tile next = this.grid.getCellContent(positionsNext.getX(), positionsNext.getY());

                    // Only one merger per row traversal?
                    if (next != null && next.value == tile.value && next.mergedFrom == null) {
                        Tile merged = new Tile(positionsNext.getX(), positionsNext.getY(), tile.value * 2);
                        merged.mergedFrom = new ArrayList<>();
                        merged.mergedFrom.add(tile);
                        merged.mergedFrom.add(next);

                        this.grid.insertTile(merged);
                        this.grid.removeTile(tile);

                        // Converge the two tiles' positions
                        tile.updatePosition(new Tile.Position(positionsNext.getX(), positionsNext.getY()));

                        // Update the score
                        this.score += merged.value;

                        // The mighty 2048 tile
                        if (merged.value == 2048) this.won = true;
                    } else {
                        this.moveTile(tile, new Tile.Position(positionsFarthest.getX(), positionsFarthest.getY()));
                    }

                    if (!this.positionsEqual(new Tile.Position(cell.getX(), cell.getY()), tile.getPosition())) {
                        moved = true; // The tile moved from its original cell!
                    }
                }
            }
        }

        if (moved) {
            this.addRandomTile();

            if (!this.movesAvailable()) {
                this.over = true; // Game over!
            }

            this.actuate();
        }


    }

    public boolean movesAvailable () {
        return this.grid.isAnyCellAvailable() || this.tileMatchesAvailable();
    };


    // Check for available matches between tiles (more expensive check)
    public boolean tileMatchesAvailable () {

        Tile tile;

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                tile = this.grid.getCellContent(x, y);

                if (tile != null) {
                    for (int direction = 0; direction < 4; direction++) {
                        Point vector = this.getVector(direction);
                        Grid.Cell cell = new Grid.Cell(x + vector.x, y + vector.y);
                        Tile other  = this.grid.getCellContent(cell.getX(), cell.getY());

                        if (other != null && other.value == tile.value) {
                            return true; // These two tiles can be merged
                        }
                    }
                }
            }
        }

        return false;
    };

}
