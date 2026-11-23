package quoridor.model.players;

import quoridor.model.Game;
import quoridor.model.board.Board;
import quoridor.model.board.Position;
import quoridor.model.board.Wall;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathBotStrategy implements BotStrategy
{
    public ShortestPathBotStrategy()
    {
    }

    @Override
    public void executeTurn(BotPlayer bot, Game game)
    {
        Board board = game.getBoard();
        Player opponent = getOpponent(bot, game);

        int myDist = board.getShortestPath(bot.getPosition(), bot.getGoalCoordinate(), bot.getGoalAxis()).size();
        int oppDist;
        if (opponent != null)
        {
            oppDist = board.getShortestPath(opponent.getPosition(), opponent.getGoalCoordinate(), opponent.getGoalAxis()).size();
        }
        else
        {
            oppDist = Integer.MAX_VALUE;
        }

        // Place a wall only if the opponent is closer to their goal than the bot
        if (opponent != null && bot.getRemainingWalls() > 0 && oppDist < myDist)
        {
            Wall bestWall = findBestWall(bot, opponent, game, myDist, oppDist);
            if (bestWall != null && game.placeWallForCurrentPlayer(bestWall))
            {
                return;
            }
        }

        // Move to the valid cell that minimizes remaining BFS distance
        Position bestMove = findBestMove(bot, game);
        if (bestMove != null)
        {
            game.moveCurrentPlayer(bestMove);
        }
    }

    private Position findBestMove(BotPlayer bot, Game game)
    {
        Board board = game.getBoard();
        List<Position> validMoves = game.getValidMovesForCurrentPlayer();
        Position bestMove = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (Position candidate : validMoves)
        {
            List<Position> path = board.getShortestPath(candidate, bot.getGoalCoordinate(), bot.getGoalAxis());
            if (!path.isEmpty() && path.size() < shortestDistance)
            {
                shortestDistance = path.size();
                bestMove = candidate;
            }
        }

        return bestMove;
    }

    private Wall findBestWall(BotPlayer bot, Player opponent, Game game, int currentMyDist, int currentOppDist)
    {
        Board board = game.getBoard();
        int size = board.getSize();
        Wall bestWall = null;
        int bestAdvantage = currentOppDist - currentMyDist;

        List<Wall> candidates = new ArrayList<>();
        for (int x = 0; x < size - 1; x++)
        {
            for (int y = 0; y < size - 1; y++)
            {
                candidates.add(new Wall(new Position(x, y), true));
                candidates.add(new Wall(new Position(x, y), false));
            }
        }

        for (Wall wall : candidates)
        {
            if (!board.canPlaceWall(wall))
            {
                continue;
            }

            board.addWall(wall);

            boolean validForAll = true;
            for (Player p : game.getPlayers())
            {
                if (!board.hasPathToGoal(p))
                {
                    validForAll = false;
                    break;
                }
            }

            if (validForAll)
            {
                int newMyDist = board.getShortestPath(bot.getPosition(), bot.getGoalCoordinate(), bot.getGoalAxis()).size();
                int newOppDist = board.getShortestPath(opponent.getPosition(), opponent.getGoalCoordinate(), opponent.getGoalAxis()).size();
                int newAdvantage = newOppDist - newMyDist;

                // Keep the wall only if it actually slows the opponent down more than the bot
                if (newOppDist > currentOppDist && newAdvantage > bestAdvantage)
                {
                    bestAdvantage = newAdvantage;
                    bestWall = wall;
                }
            }

            board.removeWall(wall);
        }

        return bestWall;
    }

    private Player getOpponent(BotPlayer bot, Game game)
    {
        for (Player player : game.getPlayers())
        {
            if (player != bot)
            {
                return player;
            }
        }
        return null;
    }
}
