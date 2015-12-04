package br.francischini.a2048.game;

import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
public class Tile {
    /**
     * static integer to manage the tiles id
     */
    private static int currentTileId = 0;

    /**
     * @return the next tile id
     */
    private static int getTileId() {
        return ++currentTileId;
    }

    /**
     * Class to hold the tile position
     * We need to merge the Grid.Cell class with this
     */
    public static class Position {
        int x;
        int y;

        public Position(int x, int y) {
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

    /**
     * Position of this tile on the Grid
     */
    Position position;

    /**
     * Previous position of this tile on the Grid
     */
    Position previousPosition;

    /**
     * Value of this tile
     */
    int value;

    /**
     * Previous tiles merged to construct this
     */
    List<Tile> mergedFrom;

    /**
     * Tile Id
     */
    int id;

    /**
     * Construct a new tile
     *
     * @param x     Position of this tile on the Grid
     * @param y     Position of this tile on the Grid
     * @param value value of the tile
     */
    public Tile(int x, int y, int value) {
        this.position = new Position(x, y);
        this.value = value;
        this.id = this.getTileId();
        this.previousPosition = null;
        this.mergedFrom = null; // Tracks tiles that merged together
    }

    /**
     * Save the current position to previous position
     */
    public void savePosition() {
        this.previousPosition = new Position(this.position.x, this.position.y);
    }


    /**
     * Update the actual tile position
     *
     * @param position the new position
     */
    public void updatePosition(Position position) {
        this.position = position;
    }


    /**
     * @return the tile value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return tile position on grid
     */
    public int getX() {
        return this.position.x;
    }

    /**
     * @return tile position on grid
     */
    public int getY() {
        return this.position.y;
    }

    /**
     * @return tile position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @return tile id
     */
    public int getId() {
        return id;
    }

    /**
     * @return get a list of tiles merged to construct this
     */
    public List<Tile> getMergedFrom() {
        return this.mergedFrom;
    }

    /**
     * @return previous tile position
     */
    public Position getPreviousPosition() {
        return this.previousPosition;
    }

    /**
     * @return if tile has changed the position since last move
     */
    public boolean hasChangedPosition() {
        if (this.previousPosition != null) {
            return !(this.previousPosition.x == this.position.x && this.previousPosition.y == this.position.y);
        }
        return false;
    }
}
