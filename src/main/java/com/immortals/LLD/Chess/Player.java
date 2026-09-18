package com.immortals.LLD.Chess;

import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public final String name;
    public final Color color;
    public final List<Piece> capturedPieces = new ArrayList<>();
    public Player(String name, Color color) { this.name = name; this.color = color; }
}
 
