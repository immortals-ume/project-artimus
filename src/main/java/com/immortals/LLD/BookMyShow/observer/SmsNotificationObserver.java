package com.immortals.LLD.BookMyShow.observer;

import com.immortals.LLD.BookMyShow.entity.Booking;

public class SmsNotificationObserver
        implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println(
            "SMS sent for " + booking.getId()
        );
    }
}