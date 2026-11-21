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
    private CardLayout m_cardLayout;

    /* The panel that holds all the pages */
    private JPanel m_container;
    private HomePanel m_homePanel;
    private ConfigPanel m_configPanel;
    private GamePanel m_gamePanel;

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
        m_cardLayout = new CardLayout();
        m_container = new JPanel(m_cardLayout);

        /* We put the stack inside the window */
        setContentPane(m_container);

        /* We create each page */
        m_homePanel = new HomePanel(controller);
        m_configPanel = new ConfigPanel(controller);
        m_gamePanel = new GamePanel(controller);

        /* We add each page with its name */
        m_container.add(m_homePanel, HOME);
        m_container.add(m_configPanel, CONFIG);
        m_container.add(m_gamePanel, GAME);

        /* We show the home page first */
        showPage(HOME);

        /* To make visible */
        setVisible(true);
    }

    /* To show the page with the given name */
    public void showPage(String name) {
        m_cardLayout.show(m_container, name);
    }

    public ConfigPanel getConfigPanel() {
        return this.m_configPanel;
    }

    /* To give the game page to the controller */
    public GamePanel getGamePanel() {
        return this.m_gamePanel;
    }
}