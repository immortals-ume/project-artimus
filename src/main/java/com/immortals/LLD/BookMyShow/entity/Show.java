package com.immortals.LLD.BookMyShow.entity;

import java.time.Instant;

public class Show {
    private final long id;
    private final Movie movie;
    private final Screen screen;
    private final Instant startTime;

    public Show(
            long id,
            Movie movie,
            Screen screen,
            Instant startTime) {

        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
    }

    public long getId() {
        return id;
    }

    public Screen getScreen() {
        return screen;
    }
}