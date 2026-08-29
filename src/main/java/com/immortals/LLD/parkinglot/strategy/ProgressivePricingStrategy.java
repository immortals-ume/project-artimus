package com.immortals.LLD.parkinglot.strategy;

import com.immortals.LLD.parkinglot.enums.Money;
import com.immortals.LLD.parkinglot.enums.VehicleType;

import java.time.Duration;

/** First hour flat, subsequent hours cheaper -- shows the interface bends. */
public final class ProgressivePricingStrategy implements PricingStrategy {

    private final Money firstHour;
    private final Money subsequentHour;

    public ProgressivePricingStrategy(Money firstHour, Money subsequentHour) {
        this.firstHour = firstHour; this.subsequentHour = subsequentHour;
    }

    @Override
    public Money price(VehicleType type, Duration duration) {
        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));
        return firstHour.plus(subsequentHour.times(hours - 1));
    }
}