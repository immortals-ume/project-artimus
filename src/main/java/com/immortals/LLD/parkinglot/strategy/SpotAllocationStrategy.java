package com.immortals.LLD.parkinglot.strategy;

import com.immortals.LLD.parkinglot.entity.ParkingSpot;
import com.immortals.LLD.parkinglot.enums.VehicleType;

import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface SpotAllocationStrategy {
    /** Ordered candidates to attempt, the best first. Callers race for them in order. */
    List<ParkingSpot> candidatesFor(VehicleType type, Collection<ParkingSpot> allSpots);
}
