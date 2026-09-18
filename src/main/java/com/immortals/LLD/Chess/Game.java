package com.immortals.LLD.Chess;

import com.immortals.LLD.Chess.components.*;
import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.enums.Color;
import com.immortals.LLD.Chess.enums.GameStatus;
import com.immortals.LLD.Chess.enums.MoveType;
import com.immortals.LLD.Chess.exception.InvalidMoveException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Game {
    private final Board board = new Board();
    private final Player white, black;
    private Color currentTurn = Color.WHITE;
    private final List<Move> moveHistory = new ArrayList<>();
    private GameStatus status = GameStatus.ACTIVE;
 
    public Game(String whiteName, String blackName) {
        white = new Player(whiteName, Color.WHITE);
        black = new Player(blackName, Color.BLACK);
    }
 
    public GameStatus getStatus() { return status; }
    public List<Move> getMoveHistory() { return moveHistory; }
    public Board getBoard() { return board; }

    public void makeMove(Positions from, Positions to, Character promotionChoice) {
        if (status == GameStatus.CHECKMATE || status == GameStatus.STALEMATE || status == GameStatus.DRAW)
            throw new InvalidMoveException("Game is already over (" + status + ")");
 
        Piece piece = board.getPiece(from);
        if (piece == null) throw new InvalidMoveException("No piece at " + from);
        if (piece.getColor() != currentTurn) throw new InvalidMoveException("It's " + currentTurn + "'s turn");
        if (!getLegalMoves(from).contains(to)) throw new InvalidMoveException("Illegal move " + from + "-" + to);
 
        Move move = buildMoveOnBoard(board, from, to, piece, promotionChoice);
        applyMove(move);
        moveHistory.add(move);
 
        currentTurn = currentTurn.opposite();
        updateStatus();
    }

    public List<Positions> getLegalMoves(Positions from) {
        Piece piece = board.getPiece(from);
        if (piece == null) return Collections.emptyList();
 
        List<Positions> candidates = new ArrayList<>(piece.getPseudoLegalMoves(from, board));
        if (piece instanceof King) candidates.addAll(getCastlingMoves(from, piece.getColor()));
 
        List<Positions> legal = new ArrayList<>();
        for (Positions to : candidates) {
            Board sim = board.copy();
            Move m = buildMoveOnBoard(sim, from, to, sim.getPiece(from), null);
            applyMoveToBoard(sim, m);
            if (!sim.isSquareAttacked(sim.findKing(piece.getColor()), piece.getColor().opposite())) {
                legal.add(to);
            }
        }
        return legal;
    }
 
    private List<Positions> getCastlingMoves(Positions kingPos, Color color) {
        List<Positions> moves = new ArrayList<>();
        Piece king = board.getPiece(kingPos);
        if (king.hasMoved || board.isSquareAttacked(kingPos, color.opposite())) return moves;
 
        int row = kingPos.row;
        Piece kRook = board.getPiece(new Positions(row, 7));
        if (kRook instanceof Rook && !kRook.hasMoved
                && board.getPiece(new Positions(row, 5)) == null && board.getPiece(new Positions(row, 6)) == null
                && !board.isSquareAttacked(new Positions(row, 5), color.opposite())
                && !board.isSquareAttacked(new Positions(row, 6), color.opposite())) {
            moves.add(new Positions(row, 6));
        }
        Piece qRook = board.getPiece(new Positions(row, 0));
        if (qRook instanceof Rook && !qRook.hasMoved
                && board.getPiece(new Positions(row, 1)) == null && board.getPiece(new Positions(row, 2)) == null
                && board.getPiece(new Positions(row, 3)) == null
                && !board.isSquareAttacked(new Positions(row, 2), color.opposite())
                && !board.isSquareAttacked(new Positions(row, 3), color.opposite())) {
            moves.add(new Positions(row, 2));
        }
        return moves;
    }
 
    private Move buildMoveOnBoard(Board b, Positions from, Positions to, Piece piece, Character promotionChoice) {
        MoveType type = MoveType.NORMAL;
        Piece captured = b.getPiece(to);
 
        if (piece instanceof King && Math.abs(to.col - from.col) == 2) {
            type = (to.col == 6) ? MoveType.CASTLE_KINGSIDE : MoveType.CASTLE_QUEENSIDE;
        } else if (piece instanceof Pawn && to.equals(b.getEnPassantTarget()) && captured == null) {
            type = MoveType.EN_PASSANT;
            captured = b.getPiece(new Positions(from.row, to.col));
        } else if (piece instanceof Pawn && (to.row == 0 || to.row == 7)) {
            type = MoveType.PROMOTION;
        } else if (captured != null) {
            type = MoveType.CAPTURE;
        }
 
        Piece promotedTo = null;
        if (type == MoveType.PROMOTION) {
            char choice = (promotionChoice == null) ? 'Q' : Character.toUpperCase(promotionChoice);
            promotedTo = switch (choice) {
                case 'R' -> new Rook(piece.getColor());
                case 'B' -> new Bishop(piece.getColor());
                case 'N' -> new Knight(piece.getColor());
                default -> new Queen(piece.getColor());
            };
        }
        return new Move(from, to, piece, captured, type, promotedTo);
    }
 
    private void applyMove(Move move) {
        applyMoveToBoard(board, move);
        if (move.capturedPiece != null) {
            (move.movedPiece.getColor() == Color.WHITE ? white : black).capturedPieces.add(move.capturedPiece);
        }
    }
 
    private void applyMoveToBoard(Board b, Move move) {
        b.removePiece(move.from);
        if (move.type == MoveType.EN_PASSANT) {
            b.removePiece(new Positions(move.from.row, move.to.col));
        }
 
        Piece placed = (move.type == MoveType.PROMOTION) ? move.promotedTo : move.movedPiece;
        placed.hasMoved = true;
        b.setPiece(move.to, placed);
 
        if (move.type == MoveType.CASTLE_KINGSIDE || move.type == MoveType.CASTLE_QUEENSIDE) {
            int row = move.from.row;
            int rookFromCol = (move.type == MoveType.CASTLE_KINGSIDE) ? 7 : 0;
            int rookToCol = (move.type == MoveType.CASTLE_KINGSIDE) ? 5 : 3;
            Piece rook = b.getPiece(new Positions(row, rookFromCol));
            b.removePiece(new Positions(row, rookFromCol));
            rook.hasMoved = true;
            b.setPiece(new Positions(row, rookToCol), rook);
        }
 
        if (move.movedPiece instanceof Pawn && Math.abs(move.to.row - move.from.row) == 2) {
            b.setEnPassantTarget(new Positions((move.to.row + move.from.row) / 2, move.from.col));
        } else {
            b.setEnPassantTarget(null);
        }
    }
 
    private void updateStatus() {
        boolean inCheck = board.isSquareAttacked(board.findKing(currentTurn), currentTurn.opposite());
        boolean hasMoves = hasAnyLegalMove(currentTurn);
        if (inCheck && !hasMoves) status = GameStatus.CHECKMATE;
        else if (!inCheck && !hasMoves) status = GameStatus.STALEMATE;
        else status = inCheck ? GameStatus.CHECK : GameStatus.ACTIVE;
    }
 
    private boolean hasAnyLegalMove(Color color) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(new Positions(r, c));
                if (p != null && p.getColor() == color && !getLegalMoves(new Positions(r, c)).isEmpty()) return true;
            }
        return false;
    }
}