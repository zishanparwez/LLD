package com.lld.lld.tictactoe.strategy;

import com.lld.lld.tictactoe.models.Board;
import com.lld.lld.tictactoe.models.Player;

public class RowWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, Player p) {
        for (int row = 0; row < board.getSize(); row++) {
            boolean rowWin = true;
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getCell(row, col).getSymbol() != p.getSymbol()) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }
        return false;
    }
    
}
