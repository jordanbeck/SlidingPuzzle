package com.twentyfivesquares.slidingpuzzle.object;


public class PuzzlePoint {
    public int x;
    public int y;

    public PuzzlePoint(PuzzlePoint other) {
        this(other.x, other.y);
    }

    public PuzzlePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean adjacentTo(PuzzlePoint other) {
        return other.x - 1 == x ||
                other.x + 1 == x ||
                other.y - 1 == y ||
                other.y + 1 == y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PuzzlePoint) {
            PuzzlePoint other = (PuzzlePoint) obj;
            return x == other.x && y == other.y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (y * 10) + x;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
