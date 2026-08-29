package com.immortals.LLD.parkinglot.enums;

import java.util.Objects;

public record Money(long amountMinor, String currency) implements Comparable<Money> {
    public static final Money ZERO = new Money(0, "INR");

    public Money {
        Objects.requireNonNull(currency);
        if (amountMinor < 0) throw new IllegalArgumentException("negative amount");
    }

    public static Money rupees(long major) {
        return new Money(major * 100, "INR");
    }

    public Money plus(Money o) {
        require(o);
        return new Money(amountMinor + o.amountMinor, currency);
    }

    public Money times(long n) {
        return new Money(amountMinor * n, currency);
    }

    private void require(Money o) {
        if (!currency.equals(o.currency))
            throw new IllegalArgumentException("currency mismatch: " + currency + " vs " + o.currency);
    }

    @Override
    public int compareTo(Money o) {
        require(o);
        return Long.compare(amountMinor, o.amountMinor);
    }

    @Override
    public String toString() {
        return "%s %d.%02d".formatted(currency, amountMinor / 100, Math.abs(amountMinor % 100));
    }
}