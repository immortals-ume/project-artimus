package com.immortals.LLD.snakesLadder.manager;

import com.immortals.LLD.snakesLadder.MoveResult;
import com.immortals.LLD.snakesLadder.entity.Player;
import com.immortals.LLD.snakesLadder.enums.MoveStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinTurnManager implements TurnManager {
    private final Queue<Player> players;

    public RoundRobinTurnManager(List<Player> players) {

        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException(
                    "At least two players required"
            );
        }

        this.players = new LinkedList<>(players);
    }

    @Override
    public Player getNextPlayer() {

        if (players.isEmpty()) {
            throw new IllegalStateException(
                    "No players left in rotation"
            );
        }

        // Removed from the front here and only re-added in
        // completeTurn, so the same player can't be handed out
        // twice while their turn is still being resolved.
        return players.poll();
    }

    @Override
    public void completeTurn(Player player, MoveResult result) {

        // A player who just won drops out of the rotation
        // instead of being sent to the back of the queue.
        if (result.getStatus() != MoveStatus.WON) {
            players.offer(player);
        }
    }
}
