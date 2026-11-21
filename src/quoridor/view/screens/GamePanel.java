package quoridor.view.screens;

import quoridor.controller.GameController;
import quoridor.model.Game;
import quoridor.model.GameObserver;
import quoridor.view.components.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements GameObserver {

    /* Color palette of the game screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);

    /* The controller, to send the player's clicks */
    private GameController m_controller;

    /* The current game, given when the player starts a game */
    private Game m_game;

    /* The board drawn in the middle of the page */
    private BoardPanel m_boardPanel;

    public GamePanel(GameController controller) {
        this.m_controller = controller;

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        m_boardPanel = new BoardPanel(this.m_controller);
        add(m_boardPanel);
    }

    /* To give the game to the page after its creation */
    public void setGame(Game game) {
        this.m_game = game;

        /* The board needs the game to draw it */
        m_boardPanel.setGame(game);
    }

    /* Called by the model after each valid move: we redraw the board */
    @Override
    public void onBoardUpdated() {
        m_boardPanel.repaint();
    }

    /* Called by the model when a player reaches the opposite side */
    @Override
    public void onGameOver(String winnerName) {
        System.out.println("Winner: " + winnerName);
    }
}