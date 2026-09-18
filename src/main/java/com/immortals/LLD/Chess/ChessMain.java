package com.immortals.LLD.Chess;

import com.immortals.LLD.Chess.entity.Positions;
import com.immortals.LLD.Chess.exception.InvalidMoveException;



public class ChessMain {
    public static void main(String[] args) {
        Game game = new Game("Alice", "Bob");

        // "Fool's Mate" — the fastest possible checkmate — exercises move validation,
        // turn switching AND checkmate detection in four plies.
        play(game, "f2", "f3");
        play(game, "e7", "e5");
        play(game, "g2", "g4");
        play(game, "d8", "h4"); // Black delivers checkmate

        System.out.println();
        game.getBoard().print();
        System.out.println("\nFinal status: " + game.getStatus());
        System.out.println("Move history: " + game.getMoveHistory());

        try {
            game.makeMove(pos("a2"), pos("a3"), null);
        } catch (InvalidMoveException e) {
            System.out.println("Rejected (game over): " + e.getMessage());
        }
        try {
            Game g2 = new Game("A", "B");
            g2.makeMove(pos("e2"), pos("e5"), null);
        } catch (InvalidMoveException e) {
            System.out.println("Rejected (illegal destination): " + e.getMessage());
        }
    }

    private static void play(Game game, String from, String to) {
        game.makeMove(pos(from), pos(to), null);
        System.out.println(from + " -> " + to + "   | status: " + game.getStatus());
    }

    private static Positions pos(String square) {
        int col = square.charAt(0) - 'a';
        int row = Character.getNumericValue(square.charAt(1)) - 1;
        return new Positions(row, col);
    }
}
