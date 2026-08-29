package com.immortals.LLD.parkinglot.exception;

import com.immortals.LLD.parkinglot.enums.VehicleType;

public final class NoSpotAvailableException extends ParkingException {
    public NoSpotAvailableException(VehicleType t) {
        super("NO_SPOT_AVAILABLE", "no free spot for " + t);
    }
}
