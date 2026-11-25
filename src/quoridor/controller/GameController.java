package quoridor.controller;

import quoridor.model.Game;
import quoridor.model.GameMode;
import quoridor.model.board.Position;
import quoridor.model.board.Wall;
import quoridor.view.QuoridorWindow;
import quoridor.view.screens.ConfigPanel;
import quoridor.view.screens.GamePanel;

public class GameController {

    /* The window, to change the displayed page */
    private QuoridorWindow m_quoridorWindow;

    /* The current game, created when the player starts a game */
    private Game m_game;

    /* To give the window to the controller after its creation */
    public void setWindow(QuoridorWindow quoridorWindow) {
        this.m_quoridorWindow = quoridorWindow;
    }

    /* Called when the player clicks on "Jouer" */
    public void play() {
        m_quoridorWindow.showPage(QuoridorWindow.CONFIG);
    }

    /* Called when the player clicks on "Quitter" */
    public void quit() {
        m_quoridorWindow.dispose();
    }

    /* Called when the player clicks on "Retour" in the config screen */
    public void goHome() {
        m_quoridorWindow.showPage(QuoridorWindow.HOME);
    }

    /* Called when the player clicks on "Lancer la partie" */
    public void startGame() {
        ConfigPanel configPanel = m_quoridorWindow.getConfigPanel();

        GameMode gameMode = configPanel.getGameMode();
        int gridSize = configPanel.getGridSize();
        int wallCount = configPanel.getWallCount();
        int playerCount = configPanel.getPlayerCount();
        boolean computerOpponent = configPanel.isComputerOpponent();

        System.out.println("Mode : " + gameMode);
        System.out.println("Taille de la grille : " + gridSize);
        System.out.println("Nombre de murs : " + wallCount);
        System.out.println("Nombre de joueurs : " + playerCount);
        System.out.println("Adversaire IA : " + computerOpponent);

        this.m_game = new Game(gridSize, wallCount, gameMode, playerCount, computerOpponent);

        /* The game page observes the game, to redraw the board after each move */
        GamePanel gamePanel = m_quoridorWindow.getGamePanel();
        m_game.addObserver(gamePanel);

        /* We give the game to the game page, then we show it */
        gamePanel.setGame(this.m_game);
        m_quoridorWindow.showPage(QuoridorWindow.GAME);

    }

    public void onCellClicked(Position position) {
        if (m_game == null) {
            System.out.println("Erreur : aucune partie n'est associée au contrôleur !");
            return;
        }

        boolean success = m_game.moveCurrentPlayer(position);
        System.out.println("Déplacement vers " + position + " accepté ? " + success);
    }

    public void onWallClicked(Wall wall) {
        if (m_game == null) {
            System.out.println("Erreur : aucune partie n'est associée au contrôleur !");
            return;
        }

        boolean success = m_game.placeWallForCurrentPlayer(wall);
        System.out.println("Pose du mur " + wall.getPosition() + " (Horizontal ? " + wall.isHorizontal() + ") acceptée ? " + success);
    }

}
