package quoridor.model.board;

import quoridor.model.players.Player;

import java.util.Objects;

public class Wall
{
    private final Position m_position;
    private final boolean m_isHorizontal;

    /* Player who placed the wall, null if not placed yet */
    private final Player m_owner;

    /* Wall without owner, used by the view to say where the player wants a wall */
    public Wall(Position position, boolean isHorizontal)
    {
        this(position, isHorizontal, null);
    }

    public Wall(Position position, boolean isHorizontal, Player owner)
    {
        this.m_position = position;
        this.m_isHorizontal = isHorizontal;
        this.m_owner = owner;
    }

    public Position getPosition()
    {
        return this.m_position;
    }

    public boolean isHorizontal()
    {
        return this.m_isHorizontal;
    }

    public Player getOwner()
    {
        return this.m_owner;
    }

    /* Checks if this 2-cell wall blocks a move between two adjacent positions */
    public boolean blocksPassage(Position from, Position to)
    {
        int wx = this.m_position.getX();
        int wy = this.m_position.getY();

        if (this.m_isHorizontal)
        {
            /* Horizontal wall blocks vertical moves (Up <-> Down) on column wx or wx + 1 */
            boolean sameColumn = (from.getX() == to.getX())
                    && (from.getX() == wx || from.getX() == wx + 1);
            boolean crossesWallLine = (from.getY() == wy && to.getY() == wy + 1)
                    || (from.getY() == wy + 1 && to.getY() == wy);
            return sameColumn && crossesWallLine;
        }
        else
        {
            /* Vertical wall blocks horizontal moves (Left <-> Right) on row wy or wy + 1 */
            boolean sameRow = (from.getY() == to.getY())
                    && (from.getY() == wy || from.getY() == wy + 1);
            boolean crossesWallColumn = (from.getX() == wx && to.getX() == wx + 1)
                    || (from.getX() == wx + 1 && to.getX() == wx);
            return sameRow && crossesWallColumn;
        }
    }

    /* Checks if this wall overlaps or crosses another wall */
    public boolean overlapsWith(Wall other)
    {
        if (other == null)
        {
            return false;
        }

        int dx = Math.abs(this.m_position.getX() - other.m_position.getX());
        int dy = Math.abs(this.m_position.getY() - other.m_position.getY());

        /* Same anchor: either identical overlap or crossing in a "+" shape */
        if (dx == 0 && dy == 0)
        {
            return true;
        }

        /* Same orientation and shifted by 1 cell: half-wall overlap */
        if (this.m_isHorizontal && other.m_isHorizontal)
        {
            return dy == 0 && dx == 1;
        }
        if (!this.m_isHorizontal && !other.m_isHorizontal)
        {
            return dx == 0 && dy == 1;
        }

        return false;
    }

    /* The owner is not used: two walls at the same place are equal, whoever placed them */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Wall))
        {
            return false;
        }
        Wall other = (Wall) obj;
        return this.m_isHorizontal == other.m_isHorizontal && Objects.equals(this.m_position, other.m_position);
    }

    /* Same rule as equals, because HashSet uses hashCode */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.m_position, this.m_isHorizontal);
    }
}