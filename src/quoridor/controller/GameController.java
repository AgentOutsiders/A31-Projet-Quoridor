package quoridor.controller;

import quoridor.view.QuoridorWindow;

public class GameController {

    /* The window, to change the displayed page */
    private QuoridorWindow quoridorWindow;

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
        System.out.println("Start game clicked");
    }
}
