package quoridor.model.players;

import quoridor.model.Game;

public interface BotStrategy
{
    void executeTurn(BotPlayer bot, Game game);
}
