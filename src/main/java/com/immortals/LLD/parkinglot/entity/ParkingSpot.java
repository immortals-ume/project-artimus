package com.immortals.LLD.parkinglot.entity;

import com.immortals.LLD.parkinglot.enums.SpotId;
import com.immortals.LLD.parkinglot.enums.SpotType;
import com.immortals.LLD.parkinglot.enums.Vehicle;
import com.immortals.LLD.parkinglot.enums.VehicleType;
import com.immortals.LLD.parkinglot.exception.SpotNotOccupiedException;
import com.immortals.LLD.parkinglot.exception.SpotTypeMismatchException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class ParkingSpot {
    private final SpotId id;
    private final SpotType type;
    private final AtomicReference<Vehicle> occupant = new AtomicReference<>();

    public ParkingSpot(SpotId id, SpotType type) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
    }

    public SpotId id()      { return id; }
    public SpotType type()  { return type; }
    public boolean isFree() { return occupant.get() == null; }

    public boolean canFit(VehicleType vehicleType) { return type.accommodates(vehicleType); }

    /** Atomically claims the spot. Returns false if another thread won the race. */
    public boolean tryOccupy(Vehicle vehicle) {
        if (!canFit(vehicle.type()))
            throw new SpotTypeMismatchException(id, vehicle.type(), type);
        return occupant.compareAndSet(null, vehicle);
    }

    public Vehicle release() {
        Vehicle previous = occupant.getAndSet(null);
        if (previous == null) throw new SpotNotOccupiedException(id);
        return previous;
    }
}
