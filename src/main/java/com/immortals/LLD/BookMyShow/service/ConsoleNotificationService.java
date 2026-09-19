package com.immortals.LLD.BookMyShow.service;

import com.immortals.LLD.BookMyShow.entity.Booking;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendBookingConfirmation(Booking booking) {
        System.out.println("Notification sent for booking " + booking.getId());
    }
}