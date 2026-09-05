package com.immortals.LLD.snakesLadder.interfaces;

import com.immortals.LLD.snakesLadder.Board;
import com.immortals.LLD.snakesLadder.entity.Player;

public class ExactPositionWinningStrategy implements WinningStrategy {
 
    @Override
    public boolean hasWon(Player player, int newPosition, Board board) {
        return newPosition == board.getSize() - 1;
    }
}