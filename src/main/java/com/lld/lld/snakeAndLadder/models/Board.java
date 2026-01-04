package com.lld.lld.snakeAndLadder.models;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int size;
    private final Map<Integer, Cell> board;

    private Board(int size, Snake[] snakes, Ladder[] ladders) {
        this.size = size;
        this.board = new HashMap<>();

        for (Snake snake : snakes) {
            board.put(snake.start, snake);
        }

        for (Ladder ladder : ladders) {
            board.put(ladder.start, ladder);
        }
    }

    public int getSize() {
        return size;
    }

    public int getNextPosition(int position) {
        if (board.containsKey(position)) {
            return board.get(position).nextLocation();
        }
        return position;
    }

    public static class Builder {
        private int size = 100;
        private Snake[] snakes;
        private Ladder[] ladders;

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder snakes(Snake[] snakes) {
            this.snakes = snakes;
            return this;
        }

        public Builder ladders(Ladder[] ladders) {
            this.ladders = ladders;
            return this;
        }

        public Board build() {
            if (size <= 0) throw new IllegalArgumentException("Board size must be positive");
            return new Board(size, snakes, ladders);
        }
    }
}
