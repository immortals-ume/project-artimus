package com.immortals.LLD.snakesLadder.entity;

import java.util.Objects;

public class Player {
    private final String id;
    private final String name;
    private int position;

    public Player(String id, String name, int position){
        this.id = id;
        this.name = name;
        this.position = position;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(position, player.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void moveTo(int position) {
        this.position = position;
    }
}
