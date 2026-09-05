package com.immortals.LLD.snakesLadder.entity;

public abstract class BoardEntity {
    private final int start;
    private final int end;

    protected BoardEntity(int start, int end) {
        if (start <= 0 || end <= 0) {
            throw new IllegalArgumentException("Positions must be positive");
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public abstract int getDestination();
}
