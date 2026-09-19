package com.immortals.LLD.BookMyShow.repositories;

import com.immortals.LLD.BookMyShow.entity.Booking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingRepository {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    private final Map<String, String> idempotency = new ConcurrentHashMap<>();

    public void save(Booking booking, String idempotencyKey) {

        bookings.put(booking.getId(), booking);

        idempotency.put(idempotencyKey, booking.getId());
    }

    public Booking findById(String id) {
        return bookings.get(id);
    }

    public Booking findByIdempotencyKey(String key) {

        String bookingId = idempotency.get(key);

        if (bookingId == null) {
            return null;
        }

        return bookings.get(bookingId);
    }
}