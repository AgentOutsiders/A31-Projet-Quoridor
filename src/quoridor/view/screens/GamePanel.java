package quoridor.view.screens;

import quoridor.controller.GameController;
import quoridor.model.Game;
import quoridor.view.components.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    /* Color palette of the game screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);

    /* The controller, to send the player's clicks */
    private GameController controller;

    /* The current game, given when the player starts a game */
    private Game game;

    /* The board drawn in the middle of the page */
    private BoardPanel boardPanel;

    public GamePanel(GameController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        boardPanel = new BoardPanel();
        add(boardPanel);
    }

    /* To give the game to the page after its creation */
    public void setGame(Game game) {
        this.game = game;

        /* The board needs the game to draw it */
        boardPanel.setGame(game);
    }
}