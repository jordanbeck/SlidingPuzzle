package com.twentyfivesquares.slidingpuzzle.store;

import com.twentyfivesquares.slidingpuzzle.object.PuzzlePoint;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PuzzleStoreTest {

    private PuzzleStore puzzleStore;

    @Before
    public void before() {
        puzzleStore = new PuzzleStore(3, new PuzzlePoint(2, 2));
    }

    @Test
    public void test_canMove() {
        assertTrue(puzzleStore.canMove(new PuzzlePoint(1, 2)));

        puzzleStore.updateEmptyPosition(new PuzzlePoint(0, 0));
        assertTrue(puzzleStore.canMove(new PuzzlePoint(1, 0)));
    }

    @Test
    public void test_cannotMove() {
        assertFalse(puzzleStore.canMove(new PuzzlePoint(0, 0)));

        puzzleStore.updateEmptyPosition(new PuzzlePoint(0, 0));
        assertFalse(puzzleStore.canMove(new PuzzlePoint(2, 2)));
    }
}
