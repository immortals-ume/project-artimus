package com.immortals.LLD.parkinglot.repository;

import com.immortals.LLD.parkinglot.enums.Ticket;
import com.immortals.LLD.parkinglot.enums.TicketId;

import java.util.Optional;

public interface TicketRepository {
    void save(Ticket ticket);
    Optional<Ticket> findById(TicketId id);
    Optional<Ticket> remove(TicketId id);
}

