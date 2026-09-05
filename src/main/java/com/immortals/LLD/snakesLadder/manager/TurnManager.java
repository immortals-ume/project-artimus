package com.immortals.LLD.snakesLadder.manager;

import com.immortals.LLD.snakesLadder.MoveResult;
import com.immortals.LLD.snakesLadder.entity.Player;

public interface TurnManager {
    Player getNextPlayer();
    void completeTurn(Player player, MoveResult result);
}

