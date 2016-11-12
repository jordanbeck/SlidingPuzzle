package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

public class PuzzleStore {

    private int height;
    private int width;
    private PuzzlePoint emptyPosition;

    public PuzzleStore(int side, PuzzlePoint emptyPosition) {
        this(side, side, emptyPosition);
    }

    public PuzzleStore(int height, int width, PuzzlePoint emptyPosition) {
        this.height = height;
        this.width = width;
        this.emptyPosition = emptyPosition;
    }

    public void updateEmptyPosition(PuzzlePoint emptyPosition) {
        this.emptyPosition = emptyPosition;
    }

    public boolean canMove(PuzzlePoint tilePosition) {
        return tilePosition.adjacentTo(emptyPosition);
    }
}
