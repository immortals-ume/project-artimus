package com.immortals.LLD.Chess;

import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.enums.MoveType;




final class Move {
    final Positions from, to;
    final Piece movedPiece;
    final Piece capturedPiece; // null if none
    final MoveType type;
    final Piece promotedTo;    // null unless type == PROMOTION
 
    Move(Positions from, Positions to, Piece movedPiece, Piece capturedPiece, MoveType type, Piece promotedTo) {
        this.from = from; this.to = to; this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece; this.type = type; this.promotedTo = promotedTo;
    }
 
    @Override public String toString() {
        if (type == MoveType.CASTLE_KINGSIDE) return "O-O";
        if (type == MoveType.CASTLE_QUEENSIDE) return "O-O-O";
        String s = movedPiece.getSymbol() + " " + from + "-" + to;
        if (capturedPiece != null) s += "x" + capturedPiece.getSymbol();
        if (type == MoveType.PROMOTION) s += "=" + promotedTo.getSymbol();
        return s;
    }
}