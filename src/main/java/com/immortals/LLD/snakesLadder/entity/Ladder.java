package com.immortals.LLD.snakesLadder.entity;

public class Ladder extends BoardEntity {

    public Ladder(int bottom, int top) {
        super(bottom, top);

        if (bottom >= top) {
            throw new IllegalArgumentException(
                    "Ladder bottom must be below its top"
            );
        }
    }

    @Override
    public int getDestination() {
        return getEnd();
    }
}