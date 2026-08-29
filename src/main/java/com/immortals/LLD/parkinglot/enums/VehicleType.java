package com.immortals.LLD.parkinglot.enums;

public enum VehicleType {
    MOTORCYCLE(1), CAR(2), TRUCK(3);

    private final int size;
    VehicleType(int size) { this.size = size; }
    int size() { return size; }
}
