package com.immortals.LLD.BookMyShow.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Theater {
    private final long id;
    private final String name;
    private final String city;
    private final List<Screen> screens;

    public Theater(long id, String name, String city) {
        this(id, name, city, new ArrayList<>());
    }

    public Theater(long id, String name, String city, List<Screen> screens) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = new ArrayList<>(screens);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public List<Screen> getScreens() {
        return Collections.unmodifiableList(screens);
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }
}
