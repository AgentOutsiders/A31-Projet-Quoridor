package quoridor.model;

import quoridor.model.board.*;
import quoridor.model.players.*;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Game
{
    private final Board m_board;
    private final List<Player> m_players;
    private final List<GameObserver> m_observers;
    private int m_currentPlayerIndex;
    private boolean m_isFinished;
    private final int m_wallsPerPlayer;
    private final GameMode m_mode;
    private final int m_playerCount;
    private final boolean m_computerOpponent;

    public Game(int boardSize, int wallsPerPlayer, GameMode mode, int playerCount, boolean computerOpponent)
    {
        this.m_board = new Board(boardSize);
        this.m_players = new ArrayList<>();
        this.m_observers = new ArrayList<>();
        this.m_currentPlayerIndex = 0;
        this.m_isFinished = false;
        this.m_wallsPerPlayer = wallsPerPlayer;
        this.m_mode = mode;
        this.m_playerCount = playerCount;
        this.m_computerOpponent = computerOpponent;

        initPlayers(boardSize, wallsPerPlayer);
    }

    private void initPlayers(int boardSize, int wallsPerPlayer)
    {
        int middle = boardSize / 2;

        // Player 1 (Bottom): Starts at (middle, size - 1), goal is row 0
        Position p1Pos = new Position(middle, boardSize - 1);
        this.m_players.add(new HumanPlayer("Joueur 1", p1Pos, wallsPerPlayer, 0, GoalAxis.GOAL_Y));


        Position p2Pos = new Position(middle, 0);
        if (this.m_computerOpponent)
        {
            this.m_players.add(new BotPlayer("Ordinateur", p2Pos, wallsPerPlayer, boardSize - 1, GoalAxis.GOAL_Y, new ShortestPathBotStrategy()));
        }
        else
        {
            this.m_players.add(new HumanPlayer("Joueur 2", p2Pos, wallsPerPlayer, boardSize - 1, GoalAxis.GOAL_Y));
        }

        if (this.m_playerCount == 4)
        {
            Position p3Pos = new Position(boardSize - 1, middle);
            this.m_players.add(new HumanPlayer("Joueur 3", p3Pos, this.m_wallsPerPlayer, 0, GoalAxis.GOAL_X));

            Position p4Pos = new Position(0, middle);
            this.m_players.add(new HumanPlayer("Joueur 4", p4Pos, this.m_wallsPerPlayer, boardSize - 1, GoalAxis.GOAL_X));
        }
    }

    public void addObserver(GameObserver observer)
    {
        if (observer != null)
        {
            this.m_observers.add(observer);
        }
    }

    public void notifyBoardUpdated()
    {
        for (GameObserver observer : this.m_observers)
        {
            observer.onBoardUpdated();
        }
    }

    public void notifyGameOver(String winnerName)
    {
        for (GameObserver observer : this.m_observers)
        {
            observer.onGameOver(winnerName);
        }
    }

    public Player getCurrentPlayer()
    {
        return this.m_players.get(this.m_currentPlayerIndex);
    }

    public Board getBoard()
    {
        return this.m_board;
    }

    public List<Player> getPlayers()
    {
        return this.m_players;
    }

    public boolean isFinished()
    {
        return this.m_isFinished;
    }

    public void resetGame()
    {
        this.m_board.clear();
        this.m_players.clear();
        this.m_currentPlayerIndex = 0;
        this.m_isFinished = false;

        initPlayers(this.m_board.getSize(), this.m_wallsPerPlayer);
        notifyBoardUpdated();
    }

    // Returns all valid destination cells for the current player
    public List<Position> getValidMovesForCurrentPlayer()
    {
        if (this.m_isFinished)
        {
            return new ArrayList<>();
        }

        Player current = getCurrentPlayer();
        Set<Position> occupied = new HashSet<>();
        for (Player player : this.m_players)
        {
            if (player != current)
            {
                occupied.add(player.getPosition());
            }
        }

        return this.m_board.getValidMoves(current.getPosition(), occupied);
    }

    // Moves the current player to a target position if valid
    public boolean moveCurrentPlayer(Position target)
    {
        if (this.m_isFinished || target == null)
        {
            return false;
        }

        Player current = getCurrentPlayer();

        if (getValidMovesForCurrentPlayer().contains(target))
        {
            current.setPosition(target);
            current.incrementMoveCount();

            if (checkVictory(current))
            {
                this.m_isFinished = true;
                notifyBoardUpdated();
                notifyGameOver(current.getName());
                return true;
            }

            nextTurn();
            notifyBoardUpdated();
            return true;
        }

        return false;
    }

    // Places a wall for the current player if valid (no overlap & doesn't trap anyone)
    public boolean placeWallForCurrentPlayer(Wall wall)
    {
        if (this.m_isFinished)
        {
            return false;
        }

        Player current = getCurrentPlayer();
        if (current.getRemainingWalls() <= 0)
        {
            return false; // No walls left
        }

        // Check basic placement rules (bounds and overlaps)
        if (!this.m_board.canPlaceWall(wall))
        {
            return false;
        }

        // Simulate wall placement to check if it blocks paths
        this.m_board.addWall(wall);

        boolean pathExistsForAll = true;
        for (Player player : this.m_players)
        {
            if (!this.m_board.hasPathToGoal(player))
            {
                pathExistsForAll = false;
                break;
            }
        }

        // If it traps a player, cancel the wall placement
        if (!pathExistsForAll)
        {
            this.m_board.removeWall(wall);
            return false;
        }

        // Valid wall placement
        current.decrementWalls();
        nextTurn();
        notifyBoardUpdated();
        return true;
    }

    private boolean checkVictory(Player player)
    {
        return player.hasReachedGoal();
    }

    private void nextTurn()
    {
        this.m_currentPlayerIndex = (this.m_currentPlayerIndex + 1) % this.m_players.size();

        Player nextPlayer = getCurrentPlayer();
        if (!this.m_isFinished && nextPlayer.isBot())
        {
            ((BotPlayer) nextPlayer).computeNextTurn(this);
        }
    }
}