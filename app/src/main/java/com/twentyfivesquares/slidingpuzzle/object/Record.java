package com.twentyfivesquares.slidingpuzzle.object;

public class Record {

    public int totalWins;
    public int leastMoves;
    public long bestTimeMillis;

    public Record() {
        this.totalWins = 0;
        this.leastMoves = 0;
        this.bestTimeMillis = 0;
    }

    public Record(int totalWins, int leastMoves, long bestTimeMillis) {
        this.totalWins = totalWins;
        this.leastMoves = leastMoves;
        this.bestTimeMillis = bestTimeMillis;
    }
}
