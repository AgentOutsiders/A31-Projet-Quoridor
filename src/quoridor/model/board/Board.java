package quoridor.model.board;

import java.util.*;
import java.util.function.Predicate;

import quoridor.model.players.GoalAxis;
import quoridor.model.players.Player;

public class Board
{
    private final int m_size;
    private final Set<Wall> m_walls;
    private final List<Position> m_pacGums;

    public Board(int size)
    {
        this.m_size = size;
        this.m_walls = new HashSet<>();
        this.m_pacGums = new ArrayList<>();
    }

    public int getSize()
    {
        return this.m_size;
    }

    public boolean isInside(Position pos)
    {
        if (pos == null)
        {
            return false;
        }
        return pos.getX() >= 0
                && pos.getX() < this.m_size
                && pos.getY() >= 0
                && pos.getY() < this.m_size;
    }

    public Set<Wall> getWalls()
    {
        return this.m_walls;
    }

    public void addWall(Wall wall)
    {
        if (wall != null)
        {
            this.m_walls.add(wall);
        }
    }

    public void removeWall(Wall wall)
    {
        this.m_walls.remove(wall);
    }

    // Checks if a wall can be placed (no overlap with existing walls)
    public boolean canPlaceWall(Wall newWall)
    {
        if (newWall == null)
        {
            return false;
        }

        int wx = newWall.getPosition().getX();
        int wy = newWall.getPosition().getY();
        if (wx < 0 || wx >= this.m_size - 1 || wy < 0 || wy >= this.m_size - 1)
        {
            return false;
        }

        for (Wall existingWall : this.m_walls)
        {
            if (existingWall.overlapsWith(newWall))
            {
                return false;
            }
        }
        return true;
    }

    // Checks if there is a wall blocking the passage between two adjacent positions
    public boolean canPass(Position from, Position to)
    {
        if (!isInside(from) || !isInside(to) || !from.isAdjacentTo(to))
        {
            return false;
        }

        for (Wall wall : this.m_walls)
        {
            if (wall.blocksPassage(from, to))
            {
                return false;
            }
        }
        return true;
    }

    // Retrieves accessible neighboring positions (no wall, within bounds)
    public List<Position> getAccessibleNeighbors(Position current)
    {
        List<Position> neighbors = new ArrayList<>();
        if (!isInside(current))
        {
            return neighbors;
        }

        Position[] directions = {
                new Position(current.getX(), current.getY() - 1), // Up
                new Position(current.getX(), current.getY() + 1), // Down
                new Position(current.getX() - 1, current.getY()), // Left
                new Position(current.getX() + 1, current.getY())  // Right
        };

        for (Position target : directions)
        {
            if (isInside(target) && canPass(current, target))
            {
                neighbors.add(target);
            }
        }

        return neighbors;
    }

    // Retrieves all valid destination cells for a pawn
    public List<Position> getValidMoves(Position current, Set<Position> occupiedPositions)
    {
        List<Position> validMoves = new ArrayList<>();
        if (!isInside(current))
        {
            return validMoves;
        }

        int[][] directions = {
                {0, -1}, // Up
                {0, 1},  // Down
                {-1, 0}, // Left
                {1, 0}   // Right
        };

        for (int[] dir : directions)
        {
            int dx = dir[0];
            int dy = dir[1];
            Position neighbor = new Position(current.getX() + dx, current.getY() + dy);

            if (!canPass(current, neighbor))
            {
                continue;
            }

            if (!occupiedPositions.contains(neighbor))
            {
                validMoves.add(neighbor);
            }
            else
            {
                Position jumpStraight = new Position(neighbor.getX() + dx, neighbor.getY() + dy);

                // Straight jump if no wall/edge behind the opponent and cell is not occupied
                if (canPass(neighbor, jumpStraight) && !occupiedPositions.contains(jumpStraight))
                {
                    validMoves.add(jumpStraight);
                }
                else
                {
                    // Blocked behind the opponent: check perpendicular left and right cells
                    int[][] perpendiculars = {
                            {dy, dx},
                            {-dy, -dx}
                    };

                    for (int[] perp : perpendiculars)
                    {
                        Position sidePos = new Position(neighbor.getX() + perp[0], neighbor.getY() + perp[1]);
                        if (canPass(neighbor, sidePos) && !occupiedPositions.contains(sidePos))
                        {
                            validMoves.add(sidePos);
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    // PATHFINDING (BFS)
    // Public method for an entire board edge (Classic mode & wall anti-trap check)
    public List<Position> getShortestPath(Position start, int goalCoordinate, GoalAxis goalAxis)
    {
        return findShortestPath(start, pos ->
        {
            if (goalAxis == GoalAxis.GOAL_X)
            {
                return pos.getX() == goalCoordinate;
            }
            return pos.getY() == goalCoordinate;
        });
    }

    // Overloaded method for an exact target cell (Pacman mode / chasing a target)
    public List<Position> getShortestPath(Position start, Position exactTarget)
    {
        if (exactTarget == null)
        {
            return Collections.emptyList();
        }
        return findShortestPath(start, pos -> pos.equals(exactTarget));
    }

    // Core BFS algorithm written ONLY ONCE
    private List<Position> findShortestPath(Position start, Predicate<Position> goalCondition)
    {
        if (!isInside(start))
        {
            return Collections.emptyList();
        }

        Queue<Position> queue = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        Position goalPosition = null;

        while (!queue.isEmpty())
        {
            Position current = queue.poll();

            if (goalCondition.test(current))
            {
                goalPosition = current;
                break;
            }

            for (Position neighbor : getAccessibleNeighbors(current))
            {
                if (!visited.contains(neighbor))
                {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (goalPosition == null)
        {
            return Collections.emptyList();
        }

        List<Position> path = new ArrayList<>();
        Position current = goalPosition;
        while (current != null)
        {
            path.add(0, current);
            current = cameFrom.get(current);
        }

        return path;
    }

    // Checks if a player still has a valid path to their goal row
    public boolean hasPathToGoal(Player player)
    {
        List<Position> path = getShortestPath(player.getPosition(), player.getGoalCoordinate(), player.getGoalAxis());
        return !path.isEmpty();
    }
}