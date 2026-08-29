package com.immortals.LLD.parkinglot.strategy;

import com.immortals.LLD.parkinglot.enums.Money;
import com.immortals.LLD.parkinglot.enums.VehicleType;

import java.time.Duration;

public interface PricingStrategy {
    Money price(VehicleType type, Duration duration);
}

