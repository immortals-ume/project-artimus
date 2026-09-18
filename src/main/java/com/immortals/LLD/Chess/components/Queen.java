package com.immortals.LLD.Chess.components;

import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.SlidingPiece;
import com.immortals.LLD.Chess.enums.Color;

public class Queen extends SlidingPiece {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    public Queen(Color color) {
        super(color);
    }

    @Override
    protected int[][] directions() {
        return DIRS;
    }

    @Override
    public char getSymbol() {
        return 'Q';
    }

    @Override
    public Piece copy() {
        Queen q = new Queen(color);
        q.hasMoved = hasMoved;
        return q;
    }
}