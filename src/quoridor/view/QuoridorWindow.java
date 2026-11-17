package quoridor.view;

import quoridor.controller.GameController;
import quoridor.view.screens.ConfigPanel;
import quoridor.view.screens.GamePanel;
import quoridor.view.screens.HomePanel;

import javax.swing.*;
import java.awt.*;

public class QuoridorWindow extends JFrame {

    /* Names of the pages */
    public static final String HOME = "Home";
    public static final String CONFIG = "Config";
    public static final String GAME = "Game";

    /* The layout that shows only one page at a time */
    private CardLayout cardLayout;

    /* The panel that holds all the pages */
    private JPanel container;
    private HomePanel homePanel;
    private ConfigPanel configPanel;
    private GamePanel gamePanel;

    public QuoridorWindow(GameController controller) {
        /* For the title */
        setTitle("Quoridor");

        /* For the size of the window */
        setSize(1100, 880);

        /* To close the program */
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* To center the window */
        setLocationRelativeTo(null);

        /* We create the stack of pages */
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        /* We put the stack inside the window */
        setContentPane(container);

        /* We create each page */
        homePanel = new HomePanel(controller);
        configPanel = new ConfigPanel(controller);
        gamePanel = new GamePanel(controller);

        /* We add each page with its name */
        container.add(homePanel, HOME);
        container.add(configPanel, CONFIG);
        container.add(gamePanel, GAME);

        /* We show the home page first */
        showPage(HOME);

        /* To make visible */
        setVisible(true);
    }

    /* To show the page with the given name */
    public void showPage(String name) {
        cardLayout.show(container, name);
    }

    public ConfigPanel getConfigPanel() {
        return this.configPanel;
    }

    /* To give the game page to the controller */
    public GamePanel getGamePanel() {
        return this.gamePanel;
    }
}