package com.lld.lld.tictactoe.models;

import java.util.List;

import com.lld.lld.tictactoe.enums.GameStatus;
import com.lld.lld.tictactoe.observer.GameSubject;
import com.lld.lld.tictactoe.strategy.ColumnWinningStrategy;
import com.lld.lld.tictactoe.strategy.DiagonalWinningStrategy;
import com.lld.lld.tictactoe.strategy.RowWinningStrategy;
import com.lld.lld.tictactoe.strategy.WinningStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game extends GameSubject {
    private Board board;
    private Player winner;
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    private GameStatus gameStatus;
    private List<WinningStrategy> winningStrategies;

    public Game(Player p1, Player p2) {
        this.board = new Board(3);
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = p1;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.winningStrategies = List.of(
                new RowWinningStrategy(),
                new ColumnWinningStrategy(),
                new DiagonalWinningStrategy()
        );
    }
    
    public void makeMove(Player p, int row, int col) {
        if(this.board.isFull()) {
            setGameStatus(GameStatus.DRAW);
            notifyObservers();
            return;
        }

        this.board.placeSymbol(row, col, p.getSymbol());
        if(checkWinner(p)) {
            setWinner(p);
            notifyObservers();
            return;
        }
        this.switchPlayer();
    }

    public void switchPlayer() {
        setCurrentPlayer((currentPlayer == p1) ? p2 : p1);
    }

    public boolean checkWinner(Player p) {
        for (WinningStrategy strategy : winningStrategies) {
            if (strategy.checkWinner(board, p)) {
                return true;
            }
        }
        return false;
    }
    
}
