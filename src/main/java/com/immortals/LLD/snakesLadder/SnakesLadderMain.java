package com.immortals.LLD.snakesLadder;

import com.immortals.LLD.snakesLadder.entity.BoardEntity;
import com.immortals.LLD.snakesLadder.entity.Ladder;
import com.immortals.LLD.snakesLadder.entity.Player;
import com.immortals.LLD.snakesLadder.entity.Snake;
import com.immortals.LLD.snakesLadder.enums.GameStatus;
import com.immortals.LLD.snakesLadder.interfaces.Dice;
import com.immortals.LLD.snakesLadder.interfaces.ExactPositionWinningStrategy;
import com.immortals.LLD.snakesLadder.interfaces.StandardDice;
import com.immortals.LLD.snakesLadder.interfaces.WinningStrategy;
import com.immortals.LLD.snakesLadder.manager.RoundRobinTurnManager;
import com.immortals.LLD.snakesLadder.manager.TurnManager;

import java.util.Arrays;
import java.util.List;

/**
 * Snakes and Ladders Game
 * <p>
 * The game Consists of a Board that has square, from 1 to 100, and dice and snakes and ladder.
 * Dice Is 6 faced dice and, using Random Number generator, we can implement and rolling of dice
 * Snakes and Ladder,should be stored in the start and end position placed in the board can be taken
 * as jump spots using Map<Integer, Integer>  we can store the positions of snakes and ladder , if the current postion
 * of the player is present in the map, we can jump to the end position , using Map functions
 * <p>
 * once the players reaches 100th sqaure the person/player is declared as winner and other player is declared as looser
 * <p>
 * For Two Player and Game is launched with specific configuration of board size, number of snakes and ladder, and dice
 * one by one turn player simulates the dice throwing. and plays the game
 * <p>
 *
 *                     +-------------------+
 *                     | SnakeAndLadderGame|
 *                     +---------+---------+
 *                               |
 *               +---------------+----------------+
 *               |               |                |
 *               v               v                v
 *            Board             Dice       WinningStrategy
 *               |
 *        +------+------+
 *        |             |
 *      Snake         Ladder
 * <p>
 *
 * Game
 *  |
 *  +---- Queue<Player>
 *  |
 *  +---- Board
 *  |
 *  +---- Dice
 *  |
 *  +---- WinningStrategy
 *
 */
public class SnakesLadderMain {
    public static void main(String[] args) {
        List<BoardEntity> entities = Arrays.asList(

                new Snake(95, 54),
                new Snake(87, 75),
                new Snake(70, 55),
                new Snake(52, 42),

                new Ladder(2, 38),
                new Ladder(7, 14),
                new Ladder(8, 31),
                new Ladder(21, 42),
                new Ladder(28, 84),
                new Ladder(36, 44)
        );

        Game game = getGame(entities);

        while (game.getStatus() == GameStatus.IN_PROGRESS) {

            MoveResult result = game.playTurn();

            System.out.println(result);
        }

        System.out.println("Winner = " + game.getWinner());
    }

    private static Game getGame(List<BoardEntity> entities) {
        Board board =
                new Board(100, entities);
        Dice dice =
                new StandardDice(1, 6);

        WinningStrategy winningStrategy =
                new ExactPositionWinningStrategy();

        Player p1 =
                new Player("1", "Alice",0);

        Player p2 =
                new Player("2", "Bob",0);

        TurnManager turnManager =
                new RoundRobinTurnManager(Arrays.asList(p1, p2));
        Game game = new Game(board, dice, winningStrategy, turnManager);
        game.start();
        return game;
    }
}
