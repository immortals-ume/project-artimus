package com.immortals.LLD.Chess.components;

import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.SlidingPiece;
import com.immortals.LLD.Chess.enums.Color;

public class Bishop extends SlidingPiece {
    private static final int[][] DIRS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    public Bishop(Color color) {
        super(color);
    }

    @Override
    protected int[][] directions() {
        return DIRS;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }

    @Override
    public Piece copy() {
        Bishop b = new Bishop(color);
        b.hasMoved = hasMoved;
        return b;
    }
}