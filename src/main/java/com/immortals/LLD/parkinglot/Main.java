package com.immortals.LLD.parkinglot;

import com.immortals.LLD.parkinglot.entity.ParkingSpot;
import com.immortals.LLD.parkinglot.enums.*;
import com.immortals.LLD.parkinglot.exception.NoSpotAvailableException;
import com.immortals.LLD.parkinglot.exception.TicketNotFoundException;
import com.immortals.LLD.parkinglot.infra.FixedClock;
import com.immortals.LLD.parkinglot.infra.IdGenerator;
import com.immortals.LLD.parkinglot.repository.InMemoryTicketRepository;
import com.immortals.LLD.parkinglot.strategy.HourlyPricingStrategy;
import com.immortals.LLD.parkinglot.strategy.NearestSmallestFitStrategy;
import com.immortals.LLD.parkinglot.strategy.PricingStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class Main {

    public static void main(String[] args) throws Exception {
        FixedClock clock = new FixedClock(Instant.parse("2026-01-01T08:00:00Z"));

        List<ParkingSpot> spots = List.of(
                new ParkingSpot(new SpotId(1, 1), SpotType.SMALL),
                new ParkingSpot(new SpotId(1, 2), SpotType.MEDIUM),
                new ParkingSpot(new SpotId(1, 3), SpotType.MEDIUM),
                new ParkingSpot(new SpotId(2, 1), SpotType.LARGE));

        PricingStrategy pricing = new HourlyPricingStrategy(
                Map.of(VehicleType.MOTORCYCLE, Money.rupees(10),
                        VehicleType.CAR,        Money.rupees(30),
                        VehicleType.TRUCK,      Money.rupees(50)),
                Duration.ofMinutes(15));

        ParkingLotService lot = new ParkingLotService(spots, new InMemoryTicketRepository(),
                new NearestSmallestFitStrategy(), pricing, clock, IdGenerator.sequential("TKT"));

        System.out.println("--- 1. smallest fitting spot wins ---");
        Ticket car = lot.park(new Vehicle("KA-01-1111", VehicleType.CAR));
        System.out.println("car  -> " + car.spotId() + "  (MEDIUM chosen, LARGE preserved)");

        Ticket bike = lot.park(new Vehicle("KA-02-2222", VehicleType.MOTORCYCLE));
        System.out.println("bike -> " + bike.spotId() + "  (SMALL chosen)");

        System.out.println("\n--- 2. grace period ---");
        clock.advance(Duration.ofMinutes(10));
        System.out.println("bike fee after 10 min: " + lot.unpark(bike.id()).fee());

        System.out.println("\n--- 3. rounding up ---");
        clock.advance(Duration.ofMinutes(51));
        Receipt carReceipt = lot.unpark(car.id());
        System.out.println("car stayed " + carReceipt.duration().toMinutes()
                + " min, billed " + carReceipt.fee() + "  (2 hours)");

        System.out.println("\n--- 4. double unpark rejected ---");
        try { lot.unpark(car.id()); }
        catch (TicketNotFoundException e) { System.out.println("rejected: " + e.code()); }

        System.out.println("\n--- 5. capacity exhausted ---");
        lot.park(new Vehicle("A", VehicleType.CAR));
        lot.park(new Vehicle("B", VehicleType.CAR));
        lot.park(new Vehicle("C", VehicleType.CAR));
        System.out.println("free car-capable spots: " + lot.freeSpots(VehicleType.CAR));
        try { lot.park(new Vehicle("D", VehicleType.CAR)); }
        catch (NoSpotAvailableException e) { System.out.println("rejected: " + e.code()); }

        System.out.println("\n--- 6. concurrency: 200 threads, 50 spots ---");
        runConcurrencyCheck();
    }
    static void runConcurrencyCheck() throws Exception {
        int capacity = 50, threads = 200;

        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++)
            spots.add(new ParkingSpot(new SpotId(1, i), SpotType.MEDIUM));

        ParkingLotService lot = new ParkingLotService(spots, new InMemoryTicketRepository(),
                new NearestSmallestFitStrategy(),
                (type, duration) -> Money.rupees(30),
                Instant::now, IdGenerator.sequential("TKT"));

        ExecutorService pool = Executors.newFixedThreadPool(32);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<SpotId>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            int n = i;
            futures.add(pool.submit(() -> {
                start.await();
                try { return lot.park(new Vehicle("V" + n, VehicleType.CAR)).spotId(); }
                catch (NoSpotAvailableException e) { return null; }
            }));
        }
        start.countDown();

        List<SpotId> assigned = new ArrayList<>();
        for (Future<SpotId> f : futures) {
            SpotId id = f.get();
            if (id != null) assigned.add(id);
        }
        pool.shutdown();

        System.out.println("succeeded : " + assigned.size()          + "  (expected " + capacity + ")");
        System.out.println("distinct  : " + new HashSet<>(assigned).size() + "  (expected " + capacity + ")");
        System.out.println(assigned.size() == capacity
                && new HashSet<>(assigned).size() == capacity
                ? "PASS -- no double assignment" : "FAIL");
    }
}