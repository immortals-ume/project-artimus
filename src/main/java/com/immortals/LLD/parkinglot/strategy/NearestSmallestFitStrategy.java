package com.immortals.LLD.parkinglot.strategy;

import com.immortals.LLD.parkinglot.entity.ParkingSpot;
import com.immortals.LLD.parkinglot.enums.VehicleType;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/** Smallest fitting spot, then lowest floor, then lowest number. */
public final class NearestSmallestFitStrategy implements SpotAllocationStrategy {

    @Override
    public List<ParkingSpot> candidatesFor(VehicleType type, Collection<ParkingSpot> allSpots) {
        return allSpots.stream()
                .filter(ParkingSpot::isFree)
                .filter(s -> s.canFit(type))
                .sorted(Comparator
                        .comparingInt((ParkingSpot s) -> s.type().ordinal())
                        .thenComparingInt(s -> s.id().floor())
                        .thenComparingInt(s -> s.id().number()))
                .toList();
    }
}