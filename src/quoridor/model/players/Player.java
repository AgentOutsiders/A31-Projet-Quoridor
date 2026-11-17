package quoridor.model.players;

import quoridor.model.board.Position;

public abstract class Player
{
    private final String m_name;
    private Position m_position;
    private int m_remainingWalls;
    private int m_moveCount;
    private final int m_goalCoordinate;

    public Player(String name, Position startPos, int remainingWalls, int goalCoordinate)
    {
        this.m_name = name;
        this.m_position = startPos;
        this.m_remainingWalls = remainingWalls;
        this.m_goalCoordinate = goalCoordinate;
        this.m_moveCount = 0;
    }

    public String getName()
    {
        return this.m_name;
    }

    public Position getPosition()
    {
        return this.m_position;
    }

    public void setPosition(Position position)
    {
        this.m_position = position;
    }

    public int getRemainingWalls()
    {
        return this.m_remainingWalls;
    }

    public void decrementWalls()
    {
        if (this.m_remainingWalls > 0)
        {
            this.m_remainingWalls--;
        }
    }

    public int getMoveCount()
    {
        return this.m_moveCount;
    }

    public void incrementMoveCount()
    {
        this.m_moveCount++;
    }

    public int getGoalCoordinate()
    {
        return this.m_goalCoordinate;
    }

    // Abstract method to know if the player is a computer or a human
    public abstract boolean isBot();
}