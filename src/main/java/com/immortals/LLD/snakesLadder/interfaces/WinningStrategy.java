package com.immortals.LLD.snakesLadder.interfaces;

import com.immortals.LLD.snakesLadder.Board;
import com.immortals.LLD.snakesLadder.entity.Player;

public interface WinningStrategy {
    boolean hasWon(Player player, int newPosition, Board board);
}