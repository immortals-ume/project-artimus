package com.immortals.LLD.parkinglot.repository;

import com.immortals.LLD.parkinglot.enums.Ticket;
import com.immortals.LLD.parkinglot.enums.TicketId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryTicketRepository implements TicketRepository {

    private final Map<TicketId, Ticket> tickets = new ConcurrentHashMap<>();

    public void save(Ticket ticket) {
        tickets.put(ticket.id(), ticket);
    }

    public Optional<Ticket> findById(TicketId id) {
        return Optional.ofNullable(tickets.get(id));
    }

    public Optional<Ticket> remove(TicketId id) {
        return Optional.ofNullable(tickets.remove(id));
    }
}