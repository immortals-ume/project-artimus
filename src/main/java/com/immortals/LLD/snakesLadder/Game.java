package com.immortals.LLD.snakesLadder;

import com.immortals.LLD.snakesLadder.entity.Player;
import com.immortals.LLD.snakesLadder.enums.GameStatus;
import com.immortals.LLD.snakesLadder.enums.MoveStatus;
import com.immortals.LLD.snakesLadder.interfaces.Dice;
import com.immortals.LLD.snakesLadder.interfaces.WinningStrategy;
import com.immortals.LLD.snakesLadder.manager.TurnManager;

import java.util.Objects;

public class Game {

    private final Board board;
    private final Dice dice;
    private final WinningStrategy winningStrategy;
    private final TurnManager turnManager;
    private GameStatus status;
    private Player winner;

    public Game(
            Board board,
            Dice dice,
            WinningStrategy winningStrategy,
            TurnManager turnManager
    ) {

        Objects.requireNonNull(board, "Board must not be null");
        Objects.requireNonNull(dice, "Dice must not be null");
        Objects.requireNonNull(winningStrategy, "WinningStrategy must not be null");

        this.board = board;
        this.dice = dice;
        this.winningStrategy = winningStrategy;

        this.turnManager = turnManager;
        this.status = GameStatus.NOT_STARTED;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }


    public void start() {
        if (status != GameStatus.NOT_STARTED) {
            throw new IllegalStateException("Game already started");
        }

        status = GameStatus.IN_PROGRESS;
    }


    public MoveResult playTurn() {

        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Game is not in progress"
            );
        }

        Player player = turnManager.getNextPlayer();

        assert player != null;

        int oldPosition = player.getPosition();
        int diceValue = dice.roll();

        int tentativePosition = oldPosition + diceValue;

        /*
         * Exact landing rule:
         * If player crosses final square,
         * they don't move.
         */

        MoveResult result;

        if (tentativePosition > board.getSize() - 1) {

            result = new MoveResult(
                    player,
                    diceValue,
                    oldPosition,
                    oldPosition,
                    MoveStatus.INVALID
            );
        } else {
            int finalPosition =
                    board.resolvePosition(tentativePosition);

            player.moveTo(finalPosition);

            if (winningStrategy.hasWon(player, finalPosition, board)) {

                winner = player;
                status = GameStatus.FINISHED;

                result = new MoveResult(
                        player,
                        diceValue,
                        oldPosition,
                        finalPosition,
                        MoveStatus.WON
                );
            } else {
                result = new MoveResult(
                        player,
                        diceValue,
                        oldPosition,
                        finalPosition,
                        MoveStatus.MOVED);
            }
        }

        turnManager.completeTurn(player, result);

        return result;
    }

}
