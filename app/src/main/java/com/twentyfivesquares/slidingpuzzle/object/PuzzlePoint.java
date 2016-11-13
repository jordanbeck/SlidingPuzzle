package com.twentyfivesquares.slidingpuzzle.object;


public class PuzzlePoint {
    public enum Direction {
        ABOVE, BELOW, LEFT, RIGHT, NONE
    }

    public int x;
    public int y;

    public PuzzlePoint(PuzzlePoint other) {
        this(other.x, other.y);
    }

    public PuzzlePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Function that lets you know if a given point is next to this point.
     *
     * @param other
     * @return
     */
    public boolean adjacentTo(PuzzlePoint other) {
        return other.y == y && (other.x - 1 == x || other.x + 1 == x) ||
                other.x == x && (other.y - 1 == y || other.y + 1 == y);
    }

    /**
     * Function that tells what direction one point is relative to another. This function is phrased
     * like "other point is to the *direction* from this one". If the point is not reachable,
     * {@link Direction#NONE} is returned.
     *
     * @param other
     * @return
     */
    public Direction relativeTo(PuzzlePoint other) {
        if (other.y == y) {
            if (other.x - 1 == x) {
                return Direction.RIGHT;
            } else if (other.x + 1 == x) {
                return Direction.LEFT;
            }
        } else if (other.x == x) {
            if (other.y - 1 == y) {
                return Direction.BELOW;
            } else if (other.y + 1 == y) {
                return Direction.ABOVE;
            }
        }

        return Direction.NONE;
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
