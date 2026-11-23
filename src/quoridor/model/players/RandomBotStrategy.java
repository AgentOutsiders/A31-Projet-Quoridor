package quoridor.model.players;

import quoridor.model.Game;
import quoridor.model.board.Position;
import quoridor.model.board.Wall;

import java.util.List;
import java.util.Random;

public class RandomBotStrategy implements BotStrategy
{
    private final Random m_random;

    public RandomBotStrategy()
    {
        this.m_random = new Random();
    }

    @Override
    public void executeTurn(BotPlayer bot, Game game)
    {
        // 50% chance to try placing a wall if the bot still has walls
        if (bot.getRemainingWalls() > 0 && this.m_random.nextBoolean())
        {
            int maxCoord = game.getBoard().getSize() - 1;
            int wallX = this.m_random.nextInt(maxCoord);
            int wallY = this.m_random.nextInt(maxCoord);
            boolean isHorizontal = this.m_random.nextBoolean();

            Wall randomWall = new Wall(new Position(wallX, wallY), isHorizontal);
            if (game.placeWallForCurrentPlayer(randomWall))
            {
                return;
            }
        }

        // Otherwise (or if random wall placement failed), move randomly
        List<Position> validMoves = game.getValidMovesForCurrentPlayer();
        if (!validMoves.isEmpty())
        {
            Position chosenMove = validMoves.get(this.m_random.nextInt(validMoves.size()));
            game.moveCurrentPlayer(chosenMove);
        }
    }
}
