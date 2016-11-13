package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PuzzleStoreTest {

    @Before
    public void before() {

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
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(2, 2));
        assertFalse(puzzleStore.canMove(new PuzzlePoint(0, 0)));

        puzzleStore = new PuzzleStore(3, new PuzzlePoint(0, 0));
        assertFalse(puzzleStore.canMove(new PuzzlePoint(2, 2)));
    }

    @Test
    public void test_move() {
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(2, 2));
        // Testing move that won't work
        assertFalse(puzzleStore.move(new PuzzlePoint(0, 0)));

        // Testing move that will work
        final PuzzlePoint point = new PuzzlePoint(1, 2);
        assertTrue(puzzleStore.move(point));
        assertEquals(point, puzzleStore.getEmptyPoint());
    }

    @Test
    public void test_checkDefaultPuzzleSolved() {
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(2, 2));
        assertTrue(puzzleStore.checkSolved());
    }

    @Test
    public void test_checkSimplePuzzleNotSolved() {
        PuzzleStore puzzleStore = new PuzzleStore(3, new PuzzlePoint(0, 0));
        assertFalse(puzzleStore.checkSolved());
    }
}
