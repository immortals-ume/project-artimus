package com.immortals.LLD.parkinglot;

import com.immortals.LLD.parkinglot.entity.ParkingSpot;
import com.immortals.LLD.parkinglot.enums.*;
import com.immortals.LLD.parkinglot.exception.NoSpotAvailableException;
import com.immortals.LLD.parkinglot.exception.TicketNotFoundException;
import com.immortals.LLD.parkinglot.infra.Clock;
import com.immortals.LLD.parkinglot.infra.IdGenerator;
import com.immortals.LLD.parkinglot.repository.TicketRepository;
import com.immortals.LLD.parkinglot.strategy.PricingStrategy;
import com.immortals.LLD.parkinglot.strategy.SpotAllocationStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class ParkingLotService {

    private final Map<SpotId, ParkingSpot> spots;
    private final TicketRepository tickets;
    private final SpotAllocationStrategy allocation;
    private final PricingStrategy pricing;
    private final Clock clock;
    private final IdGenerator ids;

    public ParkingLotService(Collection<ParkingSpot> spots, TicketRepository tickets,
                             SpotAllocationStrategy allocation, PricingStrategy pricing,
                             Clock clock, IdGenerator ids) {
        this.spots = spots.stream().collect(Collectors.toConcurrentMap(ParkingSpot::id, s -> s));
        this.tickets = tickets;
        this.allocation = allocation;
        this.pricing = pricing;
        this.clock = clock;
        this.ids = ids;
    }

    public Ticket park(Vehicle vehicle) {
        for (ParkingSpot candidate : allocation.candidatesFor(vehicle.type(), spots.values())) {
            if (candidate.tryOccupy(vehicle)) {                   // lock-free claim
                Ticket ticket = new Ticket(new TicketId(ids.next()), candidate.id(),
                        vehicle, clock.now());
                tickets.save(ticket);
                return ticket;
            }
        }
        throw new NoSpotAvailableException(vehicle.type());
    }

    public Receipt unpark(TicketId ticketId) {
        Ticket ticket = tickets.remove(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        ParkingSpot spot = spots.get(ticket.spotId());
        spot.release();

        Instant exitAt = clock.now();
        Duration stay = Duration.between(ticket.entryAt(), exitAt);
        Money fee = pricing.price(ticket.vehicle().type(), stay);

        return new Receipt(ticketId, spot.id(), ticket.entryAt(), exitAt, stay, fee);
    }

    public long freeSpots(VehicleType forType) {
        return spots.values().stream().filter(ParkingSpot::isFree)
                .filter(s -> s.canFit(forType)).count();
    }
}
