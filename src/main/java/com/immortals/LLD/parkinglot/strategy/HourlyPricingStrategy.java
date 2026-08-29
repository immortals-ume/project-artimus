package com.immortals.LLD.parkinglot.strategy;

import com.immortals.LLD.parkinglot.enums.Money;
import com.immortals.LLD.parkinglot.enums.VehicleType;

import java.time.Duration;
import java.util.Map;

/** Hourly, rounded up, with a per-type rate and a free grace period. */
public final class HourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Money> hourlyRates;
    private final Duration gracePeriod;

    public HourlyPricingStrategy(Map<VehicleType, Money> hourlyRates, Duration gracePeriod) {
        this.hourlyRates = Map.copyOf(hourlyRates);
        this.gracePeriod = gracePeriod;
    }

    public Money price(VehicleType type, Duration duration) {
        if (duration.isNegative())
            throw new IllegalArgumentException("negative duration");
        if (duration.compareTo(gracePeriod) <= 0)
            return Money.ZERO;

        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));  // round up
        Money rate = hourlyRates.get(type);
        if (rate == null) throw new IllegalStateException("no rate configured for " + type);
        return rate.times(hours);
    }
}
