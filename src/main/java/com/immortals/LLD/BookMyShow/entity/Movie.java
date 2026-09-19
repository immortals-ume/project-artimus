package com.immortals.LLD.BookMyShow.entity;

public class Movie {
    private final long id;
    private final String title;
    private final int durationMinutes;

    public Movie(long id, String title, int durationMinutes) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }
}