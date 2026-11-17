package quoridor;

import quoridor.controller.GameController;
import quoridor.view.QuoridorWindow;

public class Main
{
    public static void main(String[] args) {
        GameController controller = new GameController();
        QuoridorWindow quoridorWindow = new QuoridorWindow(controller);
        controller.setWindow(quoridorWindow);
    }
}