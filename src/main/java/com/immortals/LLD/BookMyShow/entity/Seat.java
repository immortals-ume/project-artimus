package com.immortals.LLD.BookMyShow.entity;


import com.immortals.LLD.BookMyShow.enums.SeatType;

public class Seat {
    private final long id;
    private final String row;
    private final int number;
    private final SeatType type;

    public Seat(long id, String row, int number, SeatType type) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public SeatType getType() {
        return type;
    }

    @Override
    public String toString() {
        return row + number;
    }
}