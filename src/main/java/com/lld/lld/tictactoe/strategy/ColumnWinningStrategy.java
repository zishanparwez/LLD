package com.lld.lld.tictactoe.strategy;

import com.lld.lld.tictactoe.models.Board;
import com.lld.lld.tictactoe.models.Player;

public class ColumnWinningStrategy implements WinningStrategy {

    @Override
    public boolean checkWinner(Board board, Player p) {
        for (int col = 0; col < board.getSize(); col++) {
            boolean colWin = true;
            for (int row = 0; row < board.getSize(); row++) {
                if (board.getCell(row, col).getSymbol() != p.getSymbol()) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }
        return false;
    }
    
}
