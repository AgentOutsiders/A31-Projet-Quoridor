package quoridor.view.screens;

import quoridor.controller.GameController;
import quoridor.model.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    /* Color palette of the game screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);

    /* The controller, to send the player's clicks */
    private GameController controller;

    /* The current game, given when the player starts a game */
    private Game game;

    /* Temporary label to check that the game is received */
    private JLabel testLabel;

    public GamePanel(GameController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        /* For now, a simple text in the middle of the page */
        testLabel = new JLabel("GamePanel");
        add(testLabel);
    }

    /* To give the game to the page after its creation */
    public void setGame(Game game) {
        this.game = game;

        /* Test: we show the size of the board */
        testLabel.setText("Taille du plateau : " + game.getBoard().getSize());
    }
}