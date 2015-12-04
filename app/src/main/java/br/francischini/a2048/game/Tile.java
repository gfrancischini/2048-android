package br.francischini.a2048.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
public class Tile {
    private static int currentTileId = 0;
    private static int getTileId() {
        return ++currentTileId;
    }

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

    Position position;
    Position previousPosition;
    int value;
    List<Tile> mergedFrom;
    int id;

    public Tile(int x, int y, int value) {
        this.position = new Position(x, y);
        this.value = value;
        this.id = this.getTileId();
        this.previousPosition = null;
        this.mergedFrom       = null; // Tracks tiles that merged together
    }

    public void savePosition() {
        this.previousPosition = new Position(this.position.x, this.position.y);
    };

    public void updatePosition (Position position) {
        this.position = position;
    };

    public int getValue() {
        return value;
    }

    public int getX() {
        return this.position.x;
    }
    public int getY() {
        return this.position.y;
    }

    public Position getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public List<Tile> getMergedFrom() {
        return this.mergedFrom;
    }

    public Position getPreviousPosition() {
        return this.previousPosition;
    }

    public boolean hasChangedPosition () {
        if(this.previousPosition != null) {
            return !(this.previousPosition.x == this.position.x && this.previousPosition.y == this.position.y);
        }
        return false;

    };
}
