package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.Stack;

import static org.hamcrest.core.CombinableMatcher.either;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PuzzleStoreTest {

    @Test
    public void test_pointRelativeTo() {
        PuzzlePoint p1 = new PuzzlePoint(0, 0);
        PuzzlePoint p2 = new PuzzlePoint(1, 0);
        assertEquals(PuzzlePoint.Direction.RIGHT, p1.relativeTo(p2));
        assertEquals(PuzzlePoint.Direction.LEFT, p2.relativeTo(p1));

        p1 = new PuzzlePoint(0, 0);
        p2 = new PuzzlePoint(0, 1);
        assertEquals(PuzzlePoint.Direction.BELOW, p1.relativeTo(p2));
        assertEquals(PuzzlePoint.Direction.ABOVE, p2.relativeTo(p1));

        p1 = new PuzzlePoint(0, 0);
        p2 = new PuzzlePoint(0, 2);
        assertEquals(PuzzlePoint.Direction.NONE, p1.relativeTo(p2));
    }

    @Test
    public void test_canMove() {
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(2, 2));
        assertTrue(puzzleStore.canMove(new PuzzlePoint(1, 2)));

        puzzleStore = new PuzzleStore(3, new PuzzlePoint(0, 0));
        assertTrue(puzzleStore.canMove(new PuzzlePoint(1, 0)));
    }

    @Test
    public void test_cannotMove() {
        PuzzleStore puzzleStore = new PuzzleStore(3);
        assertFalse(puzzleStore.canMove(new PuzzlePoint(0, 0)));

        puzzleStore = new PuzzleStore(3, new PuzzlePoint(0, 0));
        assertFalse(puzzleStore.canMove(new PuzzlePoint(2, 2)));

        // Test if the empty is in the middle and the corner is selected
        puzzleStore = new PuzzleStore(3, new PuzzlePoint(1, 1));
        assertFalse(puzzleStore.canMove(new PuzzlePoint(2, 2)));
    }

    @Test
    public void test_move() {
        PuzzleStore puzzleStore = new PuzzleStore(3);
        // Testing move that won't work
        assertFalse(puzzleStore.move(new PuzzlePoint(0, 0)));

        // Testing move that will work
        final PuzzlePoint point = new PuzzlePoint(1, 2);
        assertTrue(puzzleStore.move(point));
        assertEquals(point, puzzleStore.getEmptyPoint());
    }

    @Test
    public void test_defaultPuzzleSolved() {
        PuzzleStore puzzleStore = new PuzzleStore(3);
        assertTrue(puzzleStore.isSolved());
    }

    @Test
    public void test_simplePuzzleNotSolved() {
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(0, 0));
        assertFalse(puzzleStore.isSolved());
    }

    @Test
    public void test_simpleShuffle() {
        // Since this is a 2x2 puzzle, there is always only two possible ways to move the empty space
        // without repeating.
        Stack<PuzzlePoint> expectedSolution1 = new Stack<>();
        expectedSolution1.push(new PuzzlePoint(1, 1));
        expectedSolution1.push(new PuzzlePoint(1, 0));
        expectedSolution1.push(new PuzzlePoint(0, 0));

        Stack<PuzzlePoint> expectedSolution2 = new Stack<>();
        expectedSolution2.push(new PuzzlePoint(1, 1));
        expectedSolution2.push(new PuzzlePoint(0, 1));
        expectedSolution2.push(new PuzzlePoint(0, 0));

        PuzzleStore puzzleStore = new PuzzleStore(2);
        puzzleStore.shufflePuzzle(3);
        assertFalse(puzzleStore.isSolved());
        assertThat(expectedSolution1, either(is(expectedSolution1)).or(is(expectedSolution2)));
    }

    @Test
    public void test_emptyBackHome() {
        /**
         * There was a bug where the empty space where be correct (bottom right) and the puzzle was not
         *  solved, but the store said it was solved. This test will create a 2x2 grid and shuffle it
         *  four times. By design, this will bring the empty space back home, but the puzzle will not
         *  be solved.
         */
        PuzzleStore puzzleStore = new PuzzleStore(2);
        puzzleStore.shufflePuzzle(4);
        assertFalse(puzzleStore.isSolved());
    }

    @Test
    public void test_solvingPuzzle() {
        /**
         * This test will build the board, shuffle it with 5 moves, and then
         *  verify that the moves solve the board when reversed.
         */
        PuzzleStore puzzleStore = new PuzzleStore(3);
        puzzleStore.shufflePuzzle(5);
        // Make sure the puzzle is not already solved
        assertFalse(puzzleStore.isSolved());

        final int moves = puzzleStore.getSolution().size() - 1;
        for (int i = moves; i >= 0; i--) {
            PuzzlePoint puzzlePoint = puzzleStore.getSolution().peek();
            boolean moved = puzzleStore.move(puzzlePoint);
            // Make sure we moved the piece
            assertTrue(moved);

            if (i != 0) {
                assertFalse(puzzleStore.isSolved());
            }
        }

        // After all moves, the puzzle should be solved
        assertTrue(puzzleStore.isSolved());
    }

    @Test
    public void test_solvingPuzzleAfterWrongMove() {
        /**
         * This test will build the board, shuffle it, attempt a move that is not part of the solution,
         *  and then attempt to solve the puzzle with that incorrect move added.
         */
        PuzzleStore puzzleStore = new PuzzleStore(3);
        puzzleStore.shufflePuzzle(5);
        // Make sure the puzzle is not already solved
        assertFalse(puzzleStore.isSolved());

        // Find a move that will be added to the solution
        final PuzzlePoint empty = puzzleStore.getEmptyPoint();
        boolean hasMoved = false;
        while (!hasMoved) {
            final PuzzlePoint movablePoint = debug_generateMovablePoint(empty, 3);
            // Make sure the random point does not match the solution, then attempt to move there.
            hasMoved = !puzzleStore.getSolution().peek().equals(movablePoint) && puzzleStore.move(movablePoint);
        }

        final int moves = puzzleStore.getSolution().size() - 1;
        for (int i = moves; i >= 0; i--) {
            PuzzlePoint puzzlePoint = puzzleStore.getSolution().peek();
            boolean moved = puzzleStore.move(puzzlePoint);
            // Make sure we moved the piece
            assertTrue(moved);

            if (i != 0) {
                assertFalse(puzzleStore.isSolved());
            }
        }

        // After all moves, the puzzle should be solved
        assertTrue(puzzleStore.isSolved());
    }

    private PuzzlePoint debug_generateMovablePoint(PuzzlePoint emptyPoint, int size) {
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

    /**
     * Utility function that will print the puzzle board for debugging.
     *
     * @param puzzleStore
     * @param size
     */
    private void debug_printPuzzle(PuzzleStore puzzleStore, int size) {
        System.out.println("-------------------");

        StringBuilder builder = new StringBuilder();
        final Map<PuzzlePoint, Integer> puzzle = puzzleStore.getPuzzleMap();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                PuzzlePoint puzzlePoint = new PuzzlePoint(x, y);
                builder.append("| ");
                builder.append(puzzle.get(puzzlePoint) < 0 ? "X" : puzzle.get(puzzlePoint));
                builder.append(" |");
                builder.append("  ");
            }

            System.out.println(builder.toString());
            builder = new StringBuilder();
        }

        System.out.println("-------------------");
    }

    /**
     * Utility function that will print out the current solution for debugging.
     *
     * @param puzzleStore
     */
    private void debug_printSolution(PuzzleStore puzzleStore) {
        Stack<PuzzlePoint> solution = puzzleStore.getSolution();
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0, size = solution.size(); i < size; i++) {
            PuzzlePoint puzzlePoint = solution.get(i);
            builder.append("(").append(puzzlePoint.x).append(",").append(puzzlePoint.y).append(")");
            if (i < size - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        System.out.println(builder.toString());
    }
}
