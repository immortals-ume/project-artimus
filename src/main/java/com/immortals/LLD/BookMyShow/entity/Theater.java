package com.immortals.LLD.BookMyShow.entity;

import java.util.List;

public class Theater {
    private final long id;
    private final String name;
    private final String city;
    private final List<Screen> screens;

    public Theater(long id, String name, String city, List<Screen> screens) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = screens;
    }
}
