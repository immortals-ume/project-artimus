package com.immortals.LLD.BookMyShow.observer;

import com.immortals.LLD.BookMyShow.entity.Booking;

public interface BookingObserver {
    void onBookingConfirmed(Booking booking);
}