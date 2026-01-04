package com.lld.lld.snakeAndLadder.models;

import com.lld.lld.snakeAndLadder.enums.CellType;

public class Ladder extends Cell {

    public Ladder(int start, int end) {
        super(start, end, CellType.LADDER);
        if (end <= start) {
            throw new IllegalArgumentException("Ladder end must be greater than start");
        }
    }

    @Override
    public int nextLocation() {
        return end;
    }
}
