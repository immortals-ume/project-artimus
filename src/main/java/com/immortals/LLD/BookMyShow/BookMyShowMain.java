package com.immortals.LLD.BookMyShow;


import com.immortals.LLD.BookMyShow.entity.*;
import com.immortals.LLD.BookMyShow.enums.SeatType;
import com.immortals.LLD.BookMyShow.observer.BookingEventPublisher;
import com.immortals.LLD.BookMyShow.observer.EmailNotificationObserver;
import com.immortals.LLD.BookMyShow.observer.SmsNotificationObserver;
import com.immortals.LLD.BookMyShow.repositories.BookingRepository;
import com.immortals.LLD.BookMyShow.repositories.InMemoryShowSeatRepository;
import com.immortals.LLD.BookMyShow.service.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BookMyShowMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("\n===== BOOKMYSHOW LLD =====\n");

        Seat seatA1 = new Seat(1, "A", 1, SeatType.PREMIUM);

        Seat seatA2 = new Seat(2, "A", 2, SeatType.PREMIUM);
        Screen screen = new Screen(1, "Screen 1", List.of(seatA1, seatA2));

        Movie movie = new Movie(1, "Example Movie", 150);
        Show show = new Show(1, movie, screen, Instant.now());
        InMemoryShowSeatRepository showSeatRepository = new InMemoryShowSeatRepository();

        showSeatRepository.save(new ShowSeat(show.getId(), seatA1.getId()));

        showSeatRepository.save(new ShowSeat(show.getId(), seatA2.getId()));

        BookingRepository bookingRepository = new BookingRepository();

        PaymentGateway paymentGateway = new MockPaymentGateway();

        NotificationService notificationService = new ConsoleNotificationService();

        BookingEventPublisher bookingEventPublisher = new BookingEventPublisher();

        BookingService bookingService = new BookingService(showSeatRepository, bookingRepository, paymentGateway, notificationService, 30, bookingEventPublisher);

        User user1 = new User(1, "Kapil");

        Booking booking = bookingService.createBooking(user1, show.getId(), List.of(seatA1.getId()), "request-001");

        System.out.println("Booking created:");

        System.out.println(booking);

        bookingService.makePayment(booking.getId(), "payment-request-001");

        bookingEventPublisher.subscribe(
                new EmailNotificationObserver()
        );

        bookingEventPublisher.subscribe(
                new SmsNotificationObserver()
        );

        System.out.println("Final booking:");

        System.out.println(booking);

        System.out.println("\n===== CONCURRENCY TEST =====");

        long raceShowId = 999;

        ShowSeat raceSeat = new ShowSeat(raceShowId, 100);

        showSeatRepository.save(raceSeat);

        int users = 99;

        ExecutorService executor = Executors.newFixedThreadPool(20);

        CountDownLatch ready = new CountDownLatch(users);

        CountDownLatch start = new CountDownLatch(1);

        AtomicInteger success = new AtomicInteger(0);

        AtomicInteger failure = new AtomicInteger(0);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < users; i++) {

            final int userNumber = i;

            futures.add(executor.submit(() -> {

                ready.countDown();

                try {

                    start.await();

                    User user = new User(userNumber, "User-" + userNumber);

                    String key = "race-request-" + userNumber;

                    Booking book = bookingService.createBooking(user, raceShowId, List.of(100L), key);

                    bookingService.makePayment(book.getId(), "payment-" + userNumber);

                    success.incrementAndGet();

                    System.out.println("SUCCESS: " + user.getName());

                } catch (Exception e) {
                    failure.incrementAndGet();
                    System.out.println("FAILED: User-" + userNumber + " -> " + e.getMessage());
                }
            }));
        }

        ready.await();
        start.countDown();
        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();

        System.out.println("\nSuccessful bookings = " + success.get());

        System.out.println("Failed bookings = " + failure.get());

        System.out.println("Final seat state = " + showSeatRepository.find(raceShowId, 100).getStatus());

        System.out.println("\n===== TEST COMPLETE =====");
    }
}
