package com.immortals.LLD.parkinglot.enums;

import java.util.Arrays;
import java.util.Comparator;

public enum SpotType {

    SMALL(1), MEDIUM(2), LARGE(3);

    private final int capacity;

    SpotType(int capacity) { this.capacity = capacity; }

    public static SpotType smallestFor(VehicleType type) {
        return Arrays.stream(values())
                .filter(s -> s.accommodates(type))
                .min(Comparator.comparingInt(s -> s.capacity))
                .orElseThrow();
    }

    /** A vehicle fits a spot of its own size or larger. */
    public boolean accommodates(VehicleType type) { return capacity >= type.size(); }
}
