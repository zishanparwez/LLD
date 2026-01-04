## Requirements

* A board for playing Snake and Ladder game
* Players will roll dice, and palce pawn according to number on dice.
* Board will have Snakes and Ladders at different position.
* The first to reach at last wins the game.
* Start Rule, player can only start when they get 6.
* If player get 6, then he can roll dice again.
* If player get 3 times 6 then his turn will cancel.
* Leaderboard to show current pisition of players.



## Entities and Attributes

* Game
    - Board
    - Players
    - Dice
    - LeaderBoard
    - status

* Board
    - Cell
        - number
        - nextPosOfCell
        - type
* Player
    - name
    - position
    - cellToMove

* Dice
    - 1 to 6 number
    - cellToMove

* Leaderboard
    - player score

```plantuml
    @startuml
    left to right direction
    actor Players
    actor GameObserver

    rectangle Snake&LadderGame {
        
        GameObserver -- Game : Observes
        GameObserver -- LeaderBoard : Update
        rectangle LeaderBoard {

        }
        rectangle Dice {

        }
        rectangle Game {
            Players --> Dice : Roll Dice 
            Dice ..> [Board] : Make Move
        }
    }
```

## Class Diagram

```mermaid
    classDiagram
        class Game {
            - board: Board
            - players: Player []
            - dices: Dice []
            - leaderBoard: LeaderBoard
            - status: GameStatus
            + rollDice(): int
            + makeMove(Player, int)
            + createGame(Player[], int, Snake[], Ladder[])
            + printLeaderBoard()
        }

        class Board {
            - size: int
            - snakes: Snake []
            - ladders: Ladder []
            - board: Cell[]
        }

        class Cell {
            <<abstract>>
            - start: int
            - end: int
            - cellType: CellType
            + nextLocation(): int
        }

        class Player {
            - name: String
            - position: int
        }

        class Dice {
            - faces: int
            + roll(): int
        }

        class LeaderBoard {
            - postions: Map<Stirng, int>
            + update(Player, int)
        }

        class Snake {
            + nextLocation(): int
        }

        class Ladder {
            + nextLocation(): int
        }

        class GameStatus {
            IN_PROGRESS,
            OVER
        }

        class CellType {
            SNAKE,
            LADDER,
            EMPTY
        }

        Game --* Board
        Game --* Player
        Game --* LeaderBoard
        Game --* Dice
        Game --o GameStatus

        Board --* Snake
        Board --* Ladder
        Board --* Cell

        Snake --|> Cell
        Ladder --|> Cell

        Cell --o CellType
```
