package quoridor.model.board;

import java.util.Objects;

public class Position
{
    private final int m_x;
    private final int m_y;

    public Position(int x, int y)
    {
        this.m_x = x;
        this.m_y = y;
    }

    public int getX()
    {
        return this.m_x;
    }

    public int getY()
    {
        return this.m_y;
    }

    // Checks if two positions are orthogonally (in + form) adjacent (distance of 1)
    public boolean isAdjacentTo(Position other)
    {
        if (other == null)
        {
            return false;
        }
        int dx = Math.abs(this.m_x - other.m_x);
        int dy = Math.abs(this.m_y - other.m_y);
        return (dx + dy) == 1;
    }

    // Change equals so 2 Position objects with the same x and y are considered equal
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Position))
        {
            return false;
        }
        Position other = (Position) obj;
        return this.m_x == other.m_x && this.m_y == other.m_y;
    }

    // Change hashCode so 2 Position objects with the same x and y have the same hash code bc hashset use hashcode
    @Override
    public int hashCode()
    {
        return Objects.hash(this.m_x, this.m_y);
    }

    @Override
    public String toString()
    {
        return "(" + this.m_x + ", " + this.m_y + ")";
    }
}