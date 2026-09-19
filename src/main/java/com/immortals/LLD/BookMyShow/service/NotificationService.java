package com.immortals.LLD.BookMyShow.service;

import com.immortals.LLD.BookMyShow.entity.Booking;

public interface NotificationService {
    void sendBookingConfirmation(Booking booking);
}