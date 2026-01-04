## Problem Requirements
* The game is played on a 3x3 grid.
* Two players take alternate turns, identified by markers ‘X’ and ‘O’.
* The game should detect and announce the winner.
* The game should declare a draw if all cells are filled and no player has won.
* The game should reject invalid moves and inform the player.
* The system should maintain a scoreboard across multiple games.
* Moves can be hardcoded in a driver/demo class to simulate gameplay.

## Enteties and Attributes
* `Board`: Represents the 3x3 game grid. Manages a 2D matrix of cells and provides methods to update cell values, validate positions, and check for win or draw conditions.
* `Cell`: Represents an individual square on the board. Each cell can either be empty or contain a symbol `(X or O)`.
* `Player`: Represent a player with a symbol and optionally a name or ID.
* `Symbol`: Represents the value a cell can hold—X, O, or EMPTY.
* `Game`: Controls the overall game flow. Alternates turns, validates moves, updates the board, and checks for winning or draw conditions.
* `GameStatus`: Represents the current state of the game. Possible values include `IN_PROGRESS`, `DRAW`, `WINNER_X`, and `WINNER_O`.
* `Scoreboard`: Tracks cumulative scores and outcomes across multiple game sessions.
* `TicTacToeSystem`: Orchestrates the creation of games and ties together core components like the scoreboard and game engine.

## Use Case Diagram

```plantuml
    @startuml
    left to right direction
    actor Players
    actor GameObserver

    rectangle TicTacToeSystem {
        
        GameObserver -- Game : Observes
        GameObserver -- ScoreCard : Update
        rectangle ScoreCard {

        }
        rectangle Game {
            Players -- [Board] : Make Moves
        }
    }

        
    @enduml
```

## Initial Design

```mermaid
    classDiagram
        class TicTacToeSystem {
            - game: Game
            - socreBoard: ScoreBoard
            + createGame(Player, Player): Game
            + makeMove(Player, int, int): void
            + printScoreBoard(): void
        }

        class Game {
            - board: Board
            - winner: Player
            - winningStrategies: List<WinningStrategy>
            - player1: Player
            - player2: Player
            - gameStatus: GameStatus
            + makeMove(Player, int, int): void
            + switchPlayer(): Player
            + checkWiner(Player): boolean
        }

        class ScoreBoard {
            - score: Map<Player, Integer>
            + printScore(): void
            + update(Game): void
        }

        class Board {
            - board: Cell[][]
            - size: int
            - moveCount: int
            + placeSymbol(int, int, Symbol)
            + isFull(): boolean
            + getCell(int, int): Cell
            + initilizeBoard(): void
            + printBoard(): void
        }

        class Player {
            - name: string
            - symbol: Symbol
        }

        class Cell {
            - symbol: Symbol
        }

        class Symbol {
            <<enumeration>>
            X, O
        }

        class GameStatus {
            <<enumeration>>
            IN_PROGRESS,
            WINNER_X,
            WINNER_O,
            DRAW
        }

        class WinningStrategy {
            <<interface>>
            + checkWinner(Board, Player): boolean
        }

        class RowWinningStrategy {
            + checkWinner(Board, Player): boolean
        }

        class ColumnWinningStrategy {
            + checkWinner(Board, Player): boolean
        }

        class DiagonalWinningStrategy {
            + checkWinner(Board, Player): boolean
        }

        class GameSubject {
            - observers: List<GameObserver>
            + addObserver(GameObserver): void
            + removeObserver(GameObserver): void
            + notifyObservers(): void
        }

        class GameObserver {
            + update(Game): void
        }

        Game --|> GameSubject

        TicTacToeSystem --* Game
        TicTacToeSystem --* ScoreBoard

        GameSubject --* GameObserver

        ScoreBoard ..|> GameObserver

        Game --* Board
        Game --* Player
        Game --o WinningStrategy
        Game --o GameStatus

        Board --* Cell

        Cell --o Symbol

        Player --o Symbol

        WinningStrategy <|.. RowWinningStrategy
        WinningStrategy <|.. ColumnWinningStrategy
        WinningStrategy <|.. DiagonalWinningStrategy
```



