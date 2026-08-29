package com.immortals.LLD.parkinglot.enums;

import java.util.Objects;

public record TicketId(String value) {
    public TicketId { Objects.requireNonNull(value); }
    @Override public String toString() { return value; }
}

