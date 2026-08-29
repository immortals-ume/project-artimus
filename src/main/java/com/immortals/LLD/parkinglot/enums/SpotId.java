package com.immortals.LLD.parkinglot.enums;

public record SpotId(int floor, int number) {
    @Override public String toString() { return "F%d-S%d".formatted(floor, number); }
}

