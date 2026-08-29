package com.immortals.LLD.parkinglot.exception;

import com.immortals.LLD.parkinglot.enums.SpotId;
import com.immortals.LLD.parkinglot.enums.SpotType;
import com.immortals.LLD.parkinglot.enums.VehicleType;

public final class SpotTypeMismatchException extends ParkingException {
    public SpotTypeMismatchException(SpotId id, VehicleType v, SpotType s) {
        super("SPOT_TYPE_MISMATCH", "%s cannot hold %s (spot is %s)".formatted(id, v, s));
    }
}