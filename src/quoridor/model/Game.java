package quoridor.model;

import quoridor.model.board.*;

import quoridor.model.players.HumanPlayer;
import quoridor.model.players.Player;

import java.util.List;
import java.util.ArrayList;

public class Game
{
    private final Board m_board;
    private final List<Player> m_players;
    private final List<GameObserver> m_observers;
    private int m_currentPlayerIndex;
    private boolean m_isFinished;
    private final int m_wallsPerPlayer;

    public Game(int boardSize, int wallsPerPlayer, GameMode mode)
    {
        this.m_board = new Board(boardSize);
        this.m_players = new ArrayList<>();
        this.m_observers = new ArrayList<>();
        this.m_currentPlayerIndex = 0;
        this.m_isFinished = false;
        this.m_wallsPerPlayer = wallsPerPlayer;

        initPlayers(boardSize, wallsPerPlayer);
    }

    private void initPlayers(int boardSize, int wallsPerPlayer)
    {
        int middle = boardSize / 2;

        // Player 1 (Bottom): Starts at (middle, size - 1), goal is row 0
        Position p1Pos = new Position(middle, boardSize - 1);
        this.m_players.add(new HumanPlayer("Player 1", p1Pos, wallsPerPlayer, 0));

        // Player 2 (Top): Starts at (middle, 0), goal is row size - 1
        Position p2Pos = new Position(middle, 0);
        this.m_players.add(new HumanPlayer("Player 2", p2Pos, wallsPerPlayer, boardSize - 1));

        // Note: For 4 players or vs Bot, you can adapt this initialization later easily
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

    // Moves the current player to a target position if valid
    public boolean moveCurrentPlayer(Position target)
    {
        if (this.m_isFinished)
        {
            return false;
        }

        Player current = getCurrentPlayer();

        // Check if the move is adjacent and not blocked by a wall
        if (current.getPosition().isAdjacentTo(target) && this.m_board.canPass(current.getPosition(), target))
        {
            current.setPosition(target);
            current.incrementMoveCount();

            // Check if this player has won
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

        // 1. Check basic placement rules (bounds and overlaps)
        if (!this.m_board.canPlaceWall(wall))
        {
            return false;
        }

        // 2. Simulate wall placement to check if it blocks paths
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

        // 3. If it traps a player, cancel the wall placement
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
        return player.getPosition().getY() == player.getGoalCoordinate();
    }

    private void nextTurn()
    {
        this.m_currentPlayerIndex = (this.m_currentPlayerIndex + 1) % this.m_players.size();
    }
}