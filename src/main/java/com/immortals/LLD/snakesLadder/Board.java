package com.immortals.LLD.snakesLadder;

import com.immortals.LLD.snakesLadder.entity.BoardEntity;
import com.immortals.LLD.snakesLadder.entity.Ladder;
import com.immortals.LLD.snakesLadder.entity.Snake;

import java.util.*;

public class Board {
    private final int size;
    private final Map<Integer, BoardEntity> entities;

    public Board(int size, List<BoardEntity> entities) {
        if (size <= 1) {
            throw new IllegalArgumentException("Invalid board size");
        }

        this.size = size;
        this.entities = new HashMap<>();

        validateAndAddEntities(entities);
    }

    public int getSize() {
        return size;
    }

    private void validateAndAddEntities(List<BoardEntity> entities) {
        for (BoardEntity entity : entities) {

            if (entity.getStart() >= size || entity.getEnd() > size) {
                throw new IllegalArgumentException(
                        "Entity outside board"
                );
            }

            if (this.entities.containsKey(entity.getStart())) {
                throw new IllegalArgumentException(
                        "Multiple entities at same start position"
                );
            }

            this.entities.put(entity.getStart(), entity);
        }
    }

    public int resolvePosition(int position) {
        Set<Integer> visited = new HashSet<>();
        while (entities.containsKey(position)) {
            if (!visited.add(position)) {
                throw new IllegalStateException(
                        "Cycle detected in board entities"
                );
            }
            position = entities.get(position).getDestination();
        }
        return position;
    }

    /**
     * Prints the board to stdout as a zig-zag (boustrophedon) grid,
     * the way physical snake-and-ladder boards are laid out:
     * bottom row goes left-to-right, next row right-to-left, and so on.
     * Falls back to a simple linear print if size isn't a perfect square.
     */
    public void printBoard() {

        int width = (int) Math.round(Math.sqrt(size));

        if (width * width != size) {
            printLinearBoard();
            return;
        }

        StringBuilder sb = new StringBuilder();

        // Print from the top row down, since row 0 is the bottom row.
        for (int row = width - 1; row >= 0; row--) {

            for (int col = 0; col < width; col++) {
                int position = boustrophedonPosition(row, col, width);
                sb.append(formatCell(position));
            }

            sb.append(System.lineSeparator());
        }

        System.out.println(sb);
        printLegend();
    }

    private int boustrophedonPosition(int row, int col, int width) {

        // Even rows (0, 2, 4, ...) fill left to right;
        // odd rows fill right to left, matching how real boards snake.
        if (row % 2 == 0) {
            return row * width + col;
        } else {
            return row * width + (width - 1 - col);
        }
    }

    private void printLinearBoard() {

        for (int position = 0; position < size; position++) {

            System.out.print(formatCell(position));

            if ((position + 1) % 10 == 0) {
                System.out.println();
            }
        }

        System.out.println();
        printLegend();
    }

    private String formatCell(int position) {

        BoardEntity entity = entities.get(position);
        String label;

        if (entity instanceof Snake) {
            label = "S" + position + ">" + entity.getDestination();
        } else if (entity instanceof Ladder) {
            label = "L" + position + ">" + entity.getDestination();
        } else {
            label = String.valueOf(position);
        }

        return String.format("[%-6s]", label);
    }

    private void printLegend() {
        System.out.println(
                "Legend: [n] plain square | [Sx>y] snake head x -> tail y | [Lx>y] ladder bottom x -> top y"
        );
    }
}
