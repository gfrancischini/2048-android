package br.francischini.a2048.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
public class Grid {
    int size;

    public int getSize() {
        return size;
    }

    public static class Cell {
        int x;
        int y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    Tile[][] cells;

    public Grid(int size) {
        this.size = size;

        // Build a grid of the specified size
        this.cells = new Tile[this.size][this.size];
    }

    public Tile[][] getAllCells() {
        return cells;
    }

    // Find the a random available random position
    public Cell randomAvailableCell() {
        List<Cell> cells = this.availableCells();

        if (cells.size() != 0) {
            return cells.get((int) (Math.floor(Math.random() * cells.size())));
        }
        return null;
    }

    public List<Tile> occupiedCells() {
        List<Tile> occupiedCells = new ArrayList<>();

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                Tile cell = cells[x][y];
                if(cell != null) {
                    occupiedCells.add(cell);
                }
            }
        }

        return occupiedCells;
    }

    // List all available cells
    public List<Cell> availableCells() {
        List<Cell> availableCells = new ArrayList<>();

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                Tile cell = cells[x][y];
                if(cell == null) {
                    availableCells.add(new Cell(x, y));
                }
            }
        }

        return availableCells;
    }

    // Check if there are any cells available
    public boolean isAnyCellAvailable() {
        return this.availableCells().size() != 0;
    }

    // Check if the specified cell is taken
    public boolean isCellAvailable(int x, int y) {
        return !isCellOccupied(x, y);
    }

    public boolean isCellOccupied(int x, int y) {
        return getCellContent(x, y) != null;
    }

    public Tile getCellContent(int x, int y) {
        if(withinBounds(x, y)) {
            Tile cell = this.cells[x][y];
            return cell;
        }
        return null;
    }

   public boolean withinBounds(int x, int y){
        return x >= 0 && x < this.size &&
                y >= 0 && y < this.size;
    };

    // Inserts a tile at its position
    public void insertTile (Tile tile) {
        this.cells[tile.getX()][tile.getY()] = tile;
    };

    public void removeTile (Tile tile) {
        this.cells[tile.getX()][tile.getY()] = null;
    };

    // Save all tile positions and remove merger info
    public void prepareTiles() {
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                Tile tile = cells[x][y];
                if(tile != null) {
                    tile.mergedFrom = null;
                    tile.previousPosition = null;
                    tile.savePosition();
                }
            }
        }
    };
}








