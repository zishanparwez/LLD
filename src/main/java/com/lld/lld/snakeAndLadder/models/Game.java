package com.lld.lld.snakeAndLadder.models;
import java.util.List;

import com.lld.lld.snakeAndLadder.enums.GameStatus;


public class Game {
    private Board board;
    private List<Player> players;
    private List<Dice> dices;
    private LeaderBoard leaderBoard;
    private GameStatus status;

    public void createGame(List<Player> players, int boardSize, Snake[] snakes, Ladder[] ladders) {
        this.board = new Board.Builder()
                            .size(boardSize)
                            .snakes(snakes)
                            .ladders(ladders)
                            .build();
        this.players = players;
        this.dices = List.of(new Dice(6)); // default single dice
        this.leaderBoard = new LeaderBoard();
        this.status = GameStatus.IN_PROGRESS;
    }

    public int rollDice() {
        return dices.stream().mapToInt(Dice::roll).sum();
    }

    public void makeMove(Player player, int roll) {
        if (status == GameStatus.OVER) return;

        int newPos = player.getPosition() + roll;

        if (newPos > board.getSize()) {
            System.out.println(player.getName() + " rolled " + roll + " but stays at " + player.getPosition());
            return;
        }

        int finalPos = board.getNextPosition(newPos);
        player.setPosition(finalPos);
        leaderBoard.update(player, finalPos);

        System.out.println(player.getName() + " rolled " + roll + " -> moved to " + finalPos);

        if (finalPos == board.getSize()) {
            System.out.println(player.getName() + " wins the game!");
            status = GameStatus.OVER;
        }
    }

    public void printLeaderBoard() {
        leaderBoard.print();
    }

    public GameStatus getStatus() {
        return status;
    }
}
