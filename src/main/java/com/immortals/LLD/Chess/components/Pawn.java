package com.immortals.LLD.Chess.components;

import com.immortals.LLD.Chess.Board;
import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public List<Positions> getAttackSquares(Positions from, Board board) {
        int dir = (color == Color.WHITE) ? 1 : -1;
        List<Positions> squares = new ArrayList<>();
        for (int dc : new int[]{-1, 1}) {
            Positions p = new Positions(from.row + dir, from.col + dc);
            if (p.isValid()) squares.add(p);
        }
        return squares;
    }

    @Override
    public List<Positions> getPseudoLegalMoves(Positions from, Board board) {
        List<Positions> moves = new ArrayList<>();
        int dir = (color == Color.WHITE) ? 1 : -1;
        int startRow = (color == Color.WHITE) ? 1 : 6;

        Positions oneStep = new Positions(from.row + dir, from.col);
        if (oneStep.isValid() && board.getPiece(oneStep) == null) {
            moves.add(oneStep);
            Positions twoStep = new Positions(from.row + 2 * dir, from.col);
            if (from.row == startRow && board.getPiece(twoStep) == null) moves.add(twoStep);
        }
        for (Positions diag : getAttackSquares(from, board)) {
            Piece occ = board.getPiece(diag);
            if ((occ != null && occ.getColor() != color) || diag.equals(board.getEnPassantTarget())) {
                moves.add(diag);
            }
        }
        return moves;
    }


    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public Piece copy() {
        Pawn p = new Pawn(color);
        p.hasMoved = hasMoved;
        return p;
    }
}