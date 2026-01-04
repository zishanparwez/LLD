package com.lld.lld.tictactoe.models;
import com.lld.lld.tictactoe.enums.Symbol;
import com.lld.lld.tictactoe.exceptions.InvalidMoveException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    private Cell[][] board;
    private int size;
    private int moveCount;

    public Board(int size) {
        this.size = size;
        this.board = new Cell[size][size];
        this.moveCount = 0;
        initilizeBoard();
    }
    
    public boolean placeSymbol(int row, int col, Symbol symbol) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new InvalidMoveException("Invalid position: out of bounds.");
        }
        if (board[row][col].getSymbol() != Symbol.EMPTY) {
            throw new InvalidMoveException("Invalid position: cell is already occupied.");
        }
        board[row][col].setSymbol(symbol);
        this.moveCount++;
        return true;
    }

    public boolean isFull() {
        return this.moveCount == size * size;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null;
        }
        return board[row][col];
    }

    public void initilizeBoard() {
        for(int row=0;row<this.size;row++) {
            for(int col=0;col<this.size;col++) {
                board[row][col] = new Cell(Symbol.EMPTY);
            }
        }
    }

    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < size; i++) {
            System.out.print("| ");
            for (int j = 0; j < size; j++) {
                Symbol symbol = board[i][j].getSymbol();
                System.out.print(symbol + " | ");
            }
            System.out.println("\n-------------");
        }
    }
}
