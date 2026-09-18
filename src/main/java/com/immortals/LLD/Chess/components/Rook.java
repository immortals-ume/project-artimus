package com.immortals.LLD.Chess.components;

import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.SlidingPiece;
import com.immortals.LLD.Chess.enums.Color;

public class Rook extends SlidingPiece {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    public Rook(Color color) { super(color); }
    @Override protected int[][] directions() { return DIRS; }
    @Override public char getSymbol() { return 'R'; }
    @Override
    public Piece copy() { Rook r = new Rook(color); r.hasMoved = hasMoved; return r; }
}