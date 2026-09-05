package com.immortals.LLD.snakesLadder;

import com.immortals.LLD.snakesLadder.entity.Player;
import com.immortals.LLD.snakesLadder.enums.MoveStatus;

public class MoveResult {
 
    private final Player player;
    private final int diceValue;
    private final int oldPosition;
    private final int newPosition;
    private final MoveStatus status;
 
    public MoveResult(
            Player player,
            int diceValue,
            int oldPosition,
            int newPosition,
            MoveStatus status
    ) {
        this.player = player;
        this.diceValue = diceValue;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.status = status;
    }
 
    public Player getPlayer() {
        return player;
    }
 
    public int getDiceValue() {
        return diceValue;
    }
 
    public int getOldPosition() {
        return oldPosition;
    }
 
    public int getNewPosition() {
        return newPosition;
    }
 
    public MoveStatus getStatus() {
        return status;
    }
 
    @Override
    public String toString() {
        return player.getName()
                + " rolled " + diceValue
                + " : "
                + oldPosition
                + " -> "
                + newPosition
                + " : "
                + status;
    }
}
 