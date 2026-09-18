package com.immortals.LLD.Chess.entity;


import javax.swing.text.Position;
import java.util.Objects;

public final class Positions {
    public final int row; // 0..7  (rank 1..8)
    public final int col; // 0..7  (file a..h)

    public Positions(int row, int col) { this.row = row; this.col = col; }

    public boolean isValid() { return row >= 0 && row < 8 && col >= 0 && col < 8; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Positions)) return false;
        Positions p = (Positions) o;
        return row == p.row && col == p.col;
    }
    @Override
    public int hashCode() { return Objects.hash(row, col); }
    @Override
    public String toString() { return "" + (char) ('a' + col) + (row + 1); }
}