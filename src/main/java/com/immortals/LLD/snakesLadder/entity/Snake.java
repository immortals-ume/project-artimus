package com.immortals.LLD.snakesLadder.entity;

public class Snake extends BoardEntity {

    public Snake(int head, int tail) {
        super(head, tail);
        if (head <= tail) {
            throw new IllegalArgumentException(
                    "Snake head must be above its tail"
            );
        }
    }

    @Override
    public int getDestination() {
        return super.getEnd();
    }
}
