package com.immortals.LLD.Chess.entity;


import com.immortals.LLD.Chess.Board;
import com.immortals.LLD.Chess.enums.Color;

import java.util.List;


public abstract class Piece {
    protected final Color color;
    public boolean hasMoved;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract List<Positions> getPseudoLegalMoves(Positions from, Board board);

    public abstract List<Positions> getAttackSquares(Positions from, Board board);

    public abstract char getSymbol();

    public abstract Piece copy();
}