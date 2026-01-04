package com.lld.lld.snakeAndLadder;

import java.util.List;

import com.lld.lld.snakeAndLadder.enums.GameStatus;
import com.lld.lld.snakeAndLadder.models.Game;
import com.lld.lld.snakeAndLadder.models.Ladder;
import com.lld.lld.snakeAndLadder.models.Player;
import com.lld.lld.snakeAndLadder.models.Snake;

public class SnakeAndLadderGame {

    public static void main(String[] args) {
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");

        Snake[] snakes = {
            new Snake(99, 54),
            new Snake(70, 55),
            new Snake(52, 42),
            new Snake(25, 2)
        };

        Ladder[] ladders = {
            new Ladder(6, 25),
            new Ladder(11, 40),
            new Ladder(60, 85),
            new Ladder(46, 90)
        };

        Game game = new Game();
        game.createGame(List.of(p1, p2), 100, snakes, ladders);

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            for (Player player : List.of(p1, p2)) {
                int roll = game.rollDice();
                game.makeMove(player, roll);
                if (game.getStatus() == GameStatus.OVER) break;
            }
        }

        game.printLeaderBoard();
    }
    
}
