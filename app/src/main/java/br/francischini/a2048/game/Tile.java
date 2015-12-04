package br.francischini.a2048.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 12/2/15.
 */
public class Tile {
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

    public Tile(int x, int y, int value) {
        this.position = new Position(x, y);
        this.value = value;

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
}
