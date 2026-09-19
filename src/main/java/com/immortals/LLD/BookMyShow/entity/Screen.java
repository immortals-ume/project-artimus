package com.immortals.LLD.BookMyShow.entity;

import java.util.List;

public class Screen {

    private final long id;
    private final String name;
    private final List<Seat> seats;

    public Screen(long id, String name, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.seats = seats;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

}
