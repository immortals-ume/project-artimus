package com.immortals.LLD.BookMyShow.observer;

import com.immortals.LLD.BookMyShow.entity.Booking;

public class EmailNotificationObserver
        implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println(
            "Email sent for " + booking.getId()
        );
    }
}