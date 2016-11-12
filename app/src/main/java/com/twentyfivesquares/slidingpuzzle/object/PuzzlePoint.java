package com.twentyfivesquares.slidingpuzzle.object;


public class PuzzlePoint {

    public int x;
    public int y;

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
}
