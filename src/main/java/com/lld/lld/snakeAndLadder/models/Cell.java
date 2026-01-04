package com.lld.lld.snakeAndLadder.models;

import com.lld.lld.snakeAndLadder.enums.CellType;

public abstract class Cell {
    protected int start;
    protected int end;
    protected CellType cellType;

    public Cell(int start, int end, CellType type) {
        this.start = start;
        this.end = end;
        this.cellType = type;
    }

    public abstract int nextLocation();

    public CellType getCellType() {
        return cellType;
    }
}
