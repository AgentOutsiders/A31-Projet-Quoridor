package quoridor.controller;

import quoridor.model.Game;
import quoridor.model.GameMode;
import quoridor.model.board.Position;
import quoridor.view.QuoridorWindow;
import quoridor.view.screens.ConfigPanel;
import quoridor.view.screens.GamePanel;

public class GameController {

    /* The window, to change the displayed page */
    private QuoridorWindow quoridorWindow;

    /* The current game, created when the player starts a game */
    private Game game;

    /* To give the window to the controller after its creation */
    public void setWindow(QuoridorWindow quoridorWindow) {
        this.quoridorWindow = quoridorWindow;
    }

    /* Called when the player clicks on "Jouer" */
    public void play() {
        quoridorWindow.showPage(QuoridorWindow.CONFIG);
    }

    /* Called when the player clicks on "Quitter" */
    public void quit() {
        quoridorWindow.dispose();
    }

    /* Called when the player clicks on "Retour" in the config screen */
    public void goHome() {
        quoridorWindow.showPage(QuoridorWindow.HOME);
    }

    /* Called when the player clicks on "Lancer la partie" */
    public void startGame() {
        ConfigPanel configPanel = quoridorWindow.getConfigPanel();

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

        this.game = new Game(gridSize, wallCount, gameMode);

        /* The game page observes the game, to redraw the board after each move */
        GamePanel gamePanel = quoridorWindow.getGamePanel();
        game.addObserver(gamePanel);

        /* We give the game to the game page, then we show it */
        gamePanel.setGame(this.game);
        quoridorWindow.showPage(QuoridorWindow.GAME);

    }

    public void onCellClicked(Position position) {
        if (game == null) {
            System.out.println("Erreur : aucune partie n'est associée au contrôleur !");
            return;
        }

        boolean success = game.moveCurrentPlayer(position);
        System.out.println("Déplacement vers " + position + " accepté ? " + success);
    }


}
