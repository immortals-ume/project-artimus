package com.immortals.LLD.parkinglot.infra;


import java.time.Duration;
import java.time.Instant;

public final class FixedClock implements Clock {
    private Instant current;

    public FixedClock(Instant start) {
        this.current = start;
    }

    public Instant now() {
        return current;
    }

    public void advance(Duration d) {
        current = current.plus(d);
    }
}