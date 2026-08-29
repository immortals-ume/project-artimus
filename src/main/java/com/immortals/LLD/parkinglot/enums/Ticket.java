package com.immortals.LLD.parkinglot.enums;

import java.time.Instant;

public record Ticket(TicketId id, SpotId spotId, Vehicle vehicle, Instant entryAt) { }

