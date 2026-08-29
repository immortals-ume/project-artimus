package com.immortals.LLD.parkinglot.enums;

import java.util.Objects;

public record Vehicle(String registration, VehicleType type) {
    public Vehicle {
        if (registration == null || registration.isBlank())
            throw new IllegalArgumentException("registration required");
        Objects.requireNonNull(type);
    }
}