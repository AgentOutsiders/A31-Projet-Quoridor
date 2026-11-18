package quoridor.model.players;

import quoridor.model.Game;
import quoridor.model.board.Position;

public class BotPlayer extends Player
{
    private BotStrategy m_strategy;

    public BotPlayer(String name, Position startPos, int remainingWalls, int goalCoordinate, GoalAxis goalAxis, BotStrategy strategy)
    {
        super(name, startPos, remainingWalls, goalCoordinate, goalAxis);
        this.m_strategy = strategy;
    }

    public void setStrategy(BotStrategy strategy)
    {
        this.m_strategy = strategy;
    }

    public void computeNextTurn(Game game)
    {
        if (this.m_strategy != null)
        {
            this.m_strategy.executeTurn(this, game);
        }
    }

    @Override
    public boolean isBot()
    {
        return true;
    }
}