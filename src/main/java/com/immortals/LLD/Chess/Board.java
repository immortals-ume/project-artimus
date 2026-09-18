package com.immortals.LLD.Chess;


import com.immortals.LLD.Chess.components.*;
import com.immortals.LLD.Chess.entity.Piece;
import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.enums.Color;

public class Board {
    private final Piece[][] grid = new Piece[8][8];
    private Positions enPassantTarget;

    Board() { setupInitialPositions(); }
    private Board(boolean empty) { /* used by copy() */ }

    private void setupInitialPositions() {
        for (int c = 0; c < 8; c++) {
            grid[1][c] = new Pawn(Color.WHITE);
            grid[6][c] = new Pawn(Color.BLACK);
        }
        placeBackRank(0, Color.WHITE);
        placeBackRank(7, Color.BLACK);
    }

    private void placeBackRank(int row, Color color) {
        grid[row][0] = new Rook(color);
        grid[row][1] = new Knight(color);
        grid[row][2] = new Bishop(color);
        grid[row][3] = new Queen(color);
        grid[row][4] = new King(color);
        grid[row][5] = new Bishop(color);
        grid[row][6] = new Knight(color);
        grid[row][7] = new Rook(color);
    }

    public Piece getPiece(Positions p) { return grid[p.row][p.col]; }
    public void setPiece(Positions p, Piece piece) { grid[p.row][p.col] = piece; }
    public void removePiece(Positions p) { grid[p.row][p.col] = null; }

    public Positions getEnPassantTarget() { return enPassantTarget; }
    public void setEnPassantTarget(Positions p) { enPassantTarget = p; }

    Positions findKing(Color color) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (grid[r][c] instanceof King && grid[r][c].getColor() == color) return new Positions(r, c);
        throw new IllegalStateException("King not found for " + color);
    }

    Board copy() {
        Board b = new Board(true);
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                b.grid[r][c] = (grid[r][c] == null) ? null : grid[r][c].copy();
        b.enPassantTarget = enPassantTarget;
        return b;
    }

    boolean isSquareAttacked(Positions target, Color byColor) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == byColor
                        && p.getAttackSquares(new Positions(r, c), this).contains(target)) {
                    return true;
                }
            }
        return false;
    }

    public void print() {
        for (int r = 7; r >= 0; r--) {
            StringBuilder sb = new StringBuilder().append(r + 1).append("  ");
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p == null) sb.append(". ");
                else sb.append(p.getColor() == Color.WHITE ? p.getSymbol() : Character.toLowerCase(p.getSymbol())).append(' ');
            }
            System.out.println(sb);
        }
        System.out.println("   a b c d e f g h");
    }
}
