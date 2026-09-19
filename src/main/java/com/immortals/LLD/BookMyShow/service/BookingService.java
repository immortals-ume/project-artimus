package com.immortals.LLD.BookMyShow.service;

import com.immortals.LLD.BookMyShow.entity.Booking;
import com.immortals.LLD.BookMyShow.entity.Payment;
import com.immortals.LLD.BookMyShow.entity.ShowSeat;
import com.immortals.LLD.BookMyShow.entity.User;
import com.immortals.LLD.BookMyShow.enums.BookingStatus;
import com.immortals.LLD.BookMyShow.enums.PaymentStatus;
import com.immortals.LLD.BookMyShow.enums.SeatStatus;
import com.immortals.LLD.BookMyShow.observer.BookingEventPublisher;
import com.immortals.LLD.BookMyShow.repositories.BookingRepository;
import com.immortals.LLD.BookMyShow.repositories.ShowSeatRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {

    private final ShowSeatRepository showSeatRepository;

    private final BookingRepository bookingRepository;

    private final PaymentGateway paymentGateway;

    private final NotificationService notificationService;

    private final ConcurrentHashMap<Long, Object> showLocks = new ConcurrentHashMap<>();

    private final long lockDurationSeconds;
    private final BookingEventPublisher bookingEventPublisher;

    public BookingService(ShowSeatRepository showSeatRepository, BookingRepository bookingRepository, PaymentGateway paymentGateway, NotificationService notificationService, long lockDurationSeconds, BookingEventPublisher bookingEventPublisher) {
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
        this.lockDurationSeconds = lockDurationSeconds;
        this.bookingEventPublisher = bookingEventPublisher;
    }

    public Booking createBooking(User user, long showId, List<Long> seatIds, String idempotencyKey) {
        Booking existing = bookingRepository.findByIdempotencyKey(idempotencyKey);

        if (existing != null) {
            return existing;
        }
        Object lock = showLocks.computeIfAbsent(showId, ignored -> new Object());

        synchronized (lock) {
            existing = bookingRepository.findByIdempotencyKey(idempotencyKey);
            if (existing != null) {
                return existing;
            }
            List<ShowSeat> showSeats = showSeatRepository.findAll(showId, seatIds);
            if (showSeats.size() != seatIds.size()) {
                throw new IllegalArgumentException("Invalid seat");
            }
            for (ShowSeat showSeat : showSeats) {
                releaseIfExpired(showSeat);
                if (showSeat.getStatus() != SeatStatus.AVAILABLE) {
                    throw new IllegalStateException("Seat " + showSeat.getSeatId() + " is not available");
                }
            }

            BigDecimal amount = BigDecimal.valueOf(seatIds.size() * 250L);

            String bookingId = UUID.randomUUID().toString();

            Booking booking = new Booking(bookingId, user.getId(), showId, seatIds, amount);

            Instant expiry = Instant.now().plusSeconds(lockDurationSeconds);

            booking.setExpiresAt(expiry);
            for (ShowSeat showSeat : showSeats) {
                showSeat.setStatus(SeatStatus.LOCKED);
                showSeat.setBookingId(bookingId);
                showSeat.setLockExpiry(expiry);
            }
            bookingRepository.save(booking, idempotencyKey);
            return booking;
        }
    }

    public void makePayment(String bookingId, String paymentIdempotencyKey) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking is not payable");
        }
        if (Instant.now().isAfter(booking.getExpiresAt())) {
            expireBooking(booking);
            throw new IllegalStateException("Booking expired");
        }

        Payment payment = paymentGateway.pay(booking.getId(), booking.getAmount(), paymentIdempotencyKey);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            cancelBooking(booking);
            throw new IllegalStateException("Payment failed");
        }

        Object lock = showLocks.computeIfAbsent(booking.getShowId(), ignored -> new Object());
        synchronized (lock) {
            if (Instant.now().isAfter(booking.getExpiresAt())) {
                expireBooking(booking);
                throw new IllegalStateException("Booking expired");
            }

            for (Long seatId : booking.getSeatIds()) {
                ShowSeat showSeat = showSeatRepository.find(booking.getShowId(), seatId);
                if (showSeat == null || showSeat.getStatus() != SeatStatus.LOCKED || !booking.getId().equals(showSeat.getBookingId())) {
                    throw new IllegalStateException("Seat ownership lost");
                }
                showSeat.setStatus(SeatStatus.BOOKED);
                showSeat.setLockExpiry(null);
            }
            booking.setStatus(BookingStatus.CONFIRMED);
        }
        bookingEventPublisher
                .notifyBookingConfirmed(booking);
    }

    public void cancelBooking(Booking booking) {
        Object lock = showLocks.computeIfAbsent(booking.getShowId(), ignored -> new Object());
        synchronized (lock) {
            if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
                return;
            }
            for (Long seatId : booking.getSeatIds()) {
                ShowSeat showSeat = showSeatRepository.find(booking.getShowId(), seatId);
                if (showSeat != null && booking.getId().equals(showSeat.getBookingId())) {
                    showSeat.setStatus(SeatStatus.AVAILABLE);
                    showSeat.setBookingId(null);
                    showSeat.setLockExpiry(null);
                }
            }
            booking.setStatus(BookingStatus.CANCELLED);
        }
    }

    private void expireBooking(Booking booking) {
        Object lock = showLocks.computeIfAbsent(booking.getShowId(), ignored -> new Object());
        synchronized (lock) {
            if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
                return;
            }
            for (Long seatId : booking.getSeatIds()) {
                ShowSeat showSeat = showSeatRepository.find(booking.getShowId(), seatId);
                if (showSeat != null && booking.getId().equals(showSeat.getBookingId())) {
                    showSeat.setStatus(SeatStatus.AVAILABLE);
                    showSeat.setBookingId(null);
                    showSeat.setLockExpiry(null);
                }
            }
            booking.setStatus(BookingStatus.EXPIRED);
        }
    }

    private void releaseIfExpired(ShowSeat showSeat) {
        if (showSeat.getStatus() == SeatStatus.LOCKED && showSeat.getLockExpiry() != null && Instant.now().isAfter(showSeat.getLockExpiry())) {
            showSeat.setStatus(SeatStatus.AVAILABLE);
            showSeat.setBookingId(null);
            showSeat.setLockExpiry(null);
        }
    }
}