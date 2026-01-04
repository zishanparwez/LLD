package com.lld.lld.tictactoe;

import com.lld.lld.tictactoe.exceptions.InvalidMoveException;
import com.lld.lld.tictactoe.models.Game;
import com.lld.lld.tictactoe.models.Player;
import com.lld.lld.tictactoe.observer.ScoreBoard;

public class TicTacToeSystem {
    private Game game;
    private final ScoreBoard scoreBoard;

    private static volatile TicTacToeSystem instance;

    public TicTacToeSystem() {
        this.scoreBoard = new ScoreBoard();
    }

    public static synchronized TicTacToeSystem getInstance() {
        if(instance == null) {
            instance = new TicTacToeSystem();
        }
        return instance;
    }

    public void createGame(Player p1, Player p2) {
        this.game = new Game(p1, p2);
        this.game.addObserver(this.scoreBoard);

        System.out.printf("Game started between %s (X) and %s (O).%n", p1.getName(), p2.getName());
    }

    public void makeMove(Player p, int row, int col) {
        if (game == null) {
            System.out.println("No game in progress. Please create a game first.");
            return;
        }
        try {
            System.out.printf("%s plays at (%d, %d)%n", p.getName(), row, col);
            game.makeMove(p, row, col);
            printBoard();
            System.out.println("Game Status: " + game.getGameStatus());
            if (game.getWinner() != null) {
                System.out.println("Winner: " + game.getWinner().getName());
            }
        } catch (InvalidMoveException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void printScoreBoard() {
        scoreBoard.printScores();
    }

    public void printBoard() {
        game.getBoard().printBoard();
    }

}
