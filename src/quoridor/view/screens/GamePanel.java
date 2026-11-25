package quoridor.view.screens;

import quoridor.controller.GameController;
import quoridor.model.Game;
import quoridor.model.GameObserver;
import quoridor.model.players.Player;
import quoridor.view.Theme;
import quoridor.view.components.BoardPanel;
import quoridor.view.components.PlayerInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* Game screen: the board in the middle and one player panel in each corner */
public class GamePanel extends JPanel implements GameObserver {

    /* Color palette of the game screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);

    /* Margin around the player panels */
    private static final int MARGIN = 15;

    /* The three arrays that give the corner of each player */
    private static final int[] CORNER_COLS = {0, 2, 0, 2};
    private static final int[] CORNER_ROWS = {0, 0, 1, 1};
    private static final int[] CORNER_ANCHORS = {
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.NORTHEAST,
            GridBagConstraints.SOUTHWEST,
            GridBagConstraints.SOUTHEAST
    };

    /* The controller, to send the player's clicks */
    private GameController m_controller;

    /* The current game, given when the player starts a game */
    private Game m_game;

    /* The board drawn in the middle of the page */
    private BoardPanel m_boardPanel;

    /* One panel per player, kept to refresh them after each move */
    private List<PlayerInfoPanel> m_playerInfoPanels;

    public GamePanel(GameController controller) {
        m_controller = controller;
        m_playerInfoPanels = new ArrayList<>();

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        m_boardPanel = new BoardPanel(m_controller);

        /* The board is in the middle column and takes the two rows */
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        add(m_boardPanel, constraints);
    }

    /* To give the game to the page after its creation */
    public void setGame(Game game) {
        m_game = game;

        /* The board needs the game to draw it */
        m_boardPanel.setGame(game);

        /* Remove the panels of the previous game */
        for (PlayerInfoPanel panel : m_playerInfoPanels) {
            remove(panel);
        }
        m_playerInfoPanels.clear();

        /* Create one panel per player and add it in its corner */
        List<Player> players = m_game.getPlayers();
        for(int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerInfoPanel playerPanel = new PlayerInfoPanel(player, Theme.getPawnColor(i));
            m_playerInfoPanels.add(playerPanel);

            GridBagConstraints constraints = getCornerConstraints(i);
            add(playerPanel, constraints);

        }

        /* Ask Swing to compute the layout again and redraw */
        refreshPlayerPanels();
        revalidate();
        repaint();
    }

    /* Constraints that put a player panel in the corner number index */
    private GridBagConstraints getCornerConstraints(int index) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = CORNER_COLS[index];
        constraints.gridy = CORNER_ROWS[index];
        constraints.anchor = CORNER_ANCHORS[index];
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);

        return constraints;
    }

    /* Called by the model after each valid move: we redraw the board */
    @Override
    public void onBoardUpdated() {
        m_boardPanel.repaint();
        refreshPlayerPanels();
    }

    /* Called by the model when a player reaches the opposite side */
    @Override
    public void onGameOver(String winnerName) {
        System.out.println("Winner: " + winnerName);
    }

    private void refreshPlayerPanels() {
        List<Player> players = m_game.getPlayers();
        Player currentPlayer = m_game.getCurrentPlayer();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            m_playerInfoPanels.get(i).refresh(player == currentPlayer);
        }
    }

}