package quoridor.model;

public interface GameObserver
{
    void onBoardUpdated();

    void onGameOver(String winnerName);
}