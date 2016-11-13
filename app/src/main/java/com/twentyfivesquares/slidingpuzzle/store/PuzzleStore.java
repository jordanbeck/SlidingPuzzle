package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

import java.util.HashMap;
import java.util.Map;

public class PuzzleStore {

    private static final int EMPTY = -1;
    
    private int height;
    private int width;
    private PuzzlePoint emptyPoint;

    private Map<PuzzlePoint, Integer> puzzleMap;

    public PuzzleStore(int side) {
        // Create a puzzle with the last spot empty
        this(side, new PuzzlePoint(side - 1, side - 1));
    }

    public PuzzleStore(int side, PuzzlePoint emptyPoint) {
        this(side, side, emptyPoint);
    }

    public PuzzleStore(int height, int width, PuzzlePoint emptyPoint) {
        this.height = height;
        this.width = width;
        this.emptyPoint = emptyPoint;
        initMap();
    }

    private void initMap() {
        int tileCount = 1;
        puzzleMap = new HashMap<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final PuzzlePoint puzzlePoint = new PuzzlePoint(j, i);
                puzzleMap.put(puzzlePoint, puzzlePoint.equals(emptyPoint) ? EMPTY : tileCount++);
            }
        }
    }

    public PuzzlePoint getEmptyPoint() {
        return emptyPoint;
    }

    public boolean canMove(PuzzlePoint tilePosition) {
        return tilePosition.adjacentTo(emptyPoint);
    }

    public boolean move(PuzzlePoint tilePosition) {
        if (!canMove(tilePosition)) {
            return false;
        }

        emptyPoint = tilePosition;
        return true;
    }

    public boolean checkSolved() {
        int tileCount = 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final PuzzlePoint puzzlePoint = new PuzzlePoint(j, i);
                if (puzzleMap.get(puzzlePoint) != tileCount &&
                        (i == height - 1 && j == width - 1 && puzzleMap.get(puzzlePoint) != EMPTY)) {
                    return false;
                }
                tileCount++;
            }
        }

        return true;
    }
}
