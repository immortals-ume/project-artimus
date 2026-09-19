package com.immortals.LLD.Chess.entity;

import com.immortals.LLD.Chess.Board;
import com.immortals.LLD.Chess.enums.Color;


import java.util.ArrayList;
import java.util.List;

public abstract class SlidingPiece extends Piece {
    protected SlidingPiece(Color color) { super(color); }
 
    protected abstract int[][] directions();
 
    private List<Positions> slide(Positions from, Board board, boolean includeOwnBlockers) {
        List<Positions> squares = new ArrayList<>();
        for (int[] d : directions()) {
            int r = from.row + d[0], c = from.col + d[1];
            while (new Positions(r, c).isValid()) {
                Positions p = new Positions(r, c);
                Piece occupant = board.getPiece(p);
                if (occupant == null) {
                    squares.add(p);
                } else {
                    if (includeOwnBlockers || occupant.getColor() != color) squares.add(p);
                    break;
                }
                r += d[0]; c += d[1];
            }
        }
        return squares;
    }
 
    @Override
    public List<Positions> getPseudoLegalMoves(Positions from, Board board) { return slide(from, board, false); }

    @Override
    public List<Positions> getAttackSquares(Positions from, Board board) { return slide(from, board, true); }
}