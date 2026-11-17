package quoridor.model.players;

import quoridor.model.board.Position;

public class HumanPlayer extends Player
{
    public HumanPlayer(String name, Position startPos, int remainingWalls, int goalCoordinate)
    {
        super(name, startPos, remainingWalls, goalCoordinate);
    }

    @Override
    public boolean isBot()
    {
        return false;
    }
}
