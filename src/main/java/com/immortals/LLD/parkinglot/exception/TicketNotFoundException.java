package com.immortals.LLD.parkinglot.exception;

import com.immortals.LLD.parkinglot.enums.TicketId;

public final class TicketNotFoundException extends ParkingException {
    public TicketNotFoundException(TicketId id) {
        super("TICKET_NOT_FOUND", "unknown or already used ticket: " + id);
    }
}
