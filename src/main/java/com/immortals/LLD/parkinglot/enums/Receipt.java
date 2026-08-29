package com.immortals.LLD.parkinglot.enums;

import java.time.Duration;
import java.time.Instant;

public record Receipt(TicketId ticketId, SpotId spotId, Instant entryAt,
                      Instant exitAt, Duration duration, Money fee) { }