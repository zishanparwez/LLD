package com.lld.lld.tictactoe.strategy;

import com.lld.lld.tictactoe.models.Board;
import com.lld.lld.tictactoe.models.Player;

public interface WinningStrategy {
    public boolean checkWinner(Board board, Player p);
}
