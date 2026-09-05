package com.immortals.LLD.snakesLadder.interfaces;

import java.util.Random;

public class StandardDice implements Dice {

    private final Random random;
    private final int numberOfDice;
    private final int faces;

    public StandardDice(int numberOfDice, int faces) {
        if (numberOfDice <= 0 || faces <= 0) {
            throw new IllegalArgumentException();
        }

        this.numberOfDice = numberOfDice;
        this.faces = faces;
        this.random = new Random();
    }

    @Override
    public int roll() {
        int total = 0;
        for (int i = 0; i < numberOfDice; i++) {
            total += random.nextInt(faces) + 1;
        }
        return total;
    }
}
