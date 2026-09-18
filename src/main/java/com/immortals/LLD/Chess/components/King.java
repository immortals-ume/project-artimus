package com.immortals.LLD.Chess.components;

import com.immortals.LLD.Chess.Board;
import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.enums.Color;


import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private static final int[][] OFFSETS = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
    public King(Color color) { super(color); }
 
    @Override
    public List<Positions> getAttackSquares(Positions from, Board board) {
        List<Positions> squares = new ArrayList<>();
        for (int[] o : OFFSETS) {
            Positions p = new Positions(from.row + o[0], from.col + o[1]);
            if (p.isValid()) squares.add(p);
        }
        return squares;
    }
    @Override public List<Positions> getPseudoLegalMoves(Positions from, Board board) {
        List<Positions> moves = new ArrayList<>();
        for (Positions p : getAttackSquares(from, board)) {
            Piece occ = board.getPiece(p);
            if (occ == null || occ.getColor() != color) moves.add(p);
        }
        return moves; // castling is appended by Game — it needs "no square in the path is attacked"
    }
    @Override public char getSymbol() { return 'K'; }
    @Override public Piece copy() { King k = new King(color); k.hasMoved = hasMoved; return k; }
}