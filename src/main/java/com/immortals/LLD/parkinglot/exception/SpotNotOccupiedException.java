package com.immortals.LLD.parkinglot.exception;

import com.immortals.LLD.parkinglot.enums.SpotId;

public final class SpotNotOccupiedException extends ParkingException {
    public SpotNotOccupiedException(SpotId id) {
        super("SPOT_NOT_OCCUPIED", "spot already free: " + id);
    }
}
