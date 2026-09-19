package com.immortals.LLD.BookMyShow.observer;

import com.immortals.LLD.BookMyShow.entity.Booking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookingEventPublisher {

    private final List<BookingObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(BookingObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notifyBookingConfirmed(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }
}