package com.immortals.LLD.BookMyShow.entity;

import java.time.Instant;

public class Show {
    private final long id;
    private final Movie movie;
    private final Screen screen;
    private final Theater theater;
    private final Instant startTime;

    public Show(
            long id,
            Movie movie,
            Screen screen,
            Instant startTime) {
        this(id, movie, screen, null, startTime);
    }

    public Show(
            long id,
            Movie movie,
            Screen screen,
            Theater theater,
            Instant startTime) {

        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.theater = theater;
        this.startTime = startTime;
    }

    public long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public Theater getTheater() {
        return theater;
    }

    public Instant getStartTime() {
        return startTime;
    }
}