package com.lld.lld.snakeAndLadder.models;
import com.lld.lld.snakeAndLadder.enums.CellType;

public class Snake extends Cell {

    public Snake(int start, int end) {
        super(start, end, CellType.SNAKE);
        if (end >= start) {
            throw new IllegalArgumentException("Snake end must be less than start");
        }
    }

    @Override
    public int nextLocation() {
        return end;
    }
}
