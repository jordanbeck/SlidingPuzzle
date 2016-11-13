package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class PuzzleStore {

    private static final int EMPTY = -1;

    private int size;
    private PuzzlePoint emptyPoint;

    private Map<PuzzlePoint, Integer> puzzleMap;
    private Stack<PuzzlePoint> solution;

    public PuzzleStore(int size) {
        // Create a puzzle with the last spot empty
        this(size, new PuzzlePoint(size - 1, size - 1));
    }

    /**
     * This constructor should really only be used for unit tests.
     *
     * @param size
     * @param emptyPoint
     */
    public PuzzleStore(int size, PuzzlePoint emptyPoint) {
        if (size < 2) {
            throw new IllegalStateException("This puzzle is too small. Please choose a size of 2 or greater.");
        }

        this.size = size;
        this.emptyPoint = emptyPoint;
        this.solution = new Stack<>();
        initMap();
    }

    private void initMap() {
        int tileCount = 1;
        puzzleMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final PuzzlePoint puzzlePoint = new PuzzlePoint(j, i);
                puzzleMap.put(puzzlePoint, puzzlePoint.equals(emptyPoint) ? EMPTY : tileCount++);
            }
        }
    }

    public PuzzlePoint getEmptyPoint() {
        return emptyPoint;
    }

    public Stack<PuzzlePoint> getSolution() {
        return solution;
    }

    public Map<PuzzlePoint, Integer> getPuzzleMap() {
        return puzzleMap;
    }

    public boolean canMove(PuzzlePoint tilePosition) {
        return tilePosition.adjacentTo(emptyPoint);
    }

    public boolean move(PuzzlePoint tilePosition) {
        if (!canMove(tilePosition)) {
            return false;
        }
        processMove(tilePosition);
        return true;
    }

    private void processMove(PuzzlePoint selectedPoint) {
        // Adjust the puzzle map
        puzzleMap.put(emptyPoint, puzzleMap.get(selectedPoint));
        puzzleMap.put(selectedPoint, EMPTY);

        // Update solution by adding or removing move
        if (solution.size() == 0 || !solution.peek().equals(selectedPoint)) {
            solution.push(emptyPoint);
        } else {
            solution.pop();
        }

        // Update the new empty point
        emptyPoint = selectedPoint;
    }

    public boolean isSolved() {
        int tileCount = 1;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                final PuzzlePoint puzzlePoint = new PuzzlePoint(x, y);
                if (y == size - 1 && x == size - 1) {
                    if (puzzleMap.get(puzzlePoint) != EMPTY) {
                        return false;
                    }
                } else if (puzzleMap.get(puzzlePoint) != tileCount) {
                    return false;
                }

                tileCount++;
            }
        }

        return true;
    }

    public void shufflePuzzle(int moves) {
        solution = new Stack<>();
        for (int i = 0; i < moves; i++) {
            PuzzlePoint updatedEmptyPoint = null;
            boolean success = false;
            while (!success) {
                updatedEmptyPoint = generateNewEmptyPoint();
                // Check that we can move to this point and that we're not just moving back and forth
                success = canMove(updatedEmptyPoint) && (solution.size() == 0 || !updatedEmptyPoint.equals(solution.peek()));
            }

            processMove(updatedEmptyPoint);
        }
    }

    private PuzzlePoint generateNewEmptyPoint() {
        /**
         * Random values translated:
         *  0 = x - 1 OR x + 1 if x == 0
         *  1 = x + 1 OR x - 1 if x == size - 1
         *  2 = y - 1 OR y + 1 if y == 0
         *  3 = y + 1 OR y - 1 if y == size - 1
         */
        int x = emptyPoint.x;
        int y = emptyPoint.y;
        final Random random = new Random();
        final int change = random.nextInt(4);

        if (change == 0) {
            x = (x == size - 1) ? x - 1 : x + 1;
        } else if (change == 1) {
            x = (x == 0) ? x + 1 : x - 1;
        } else if (change == 2) {
            y = (y == size - 1) ? y - 1 : y + 1;
        } else if (change == 3) {
            y = (y == 0) ? y + 1 : y - 1;
        }

        return new PuzzlePoint(x, y);
    }
}
