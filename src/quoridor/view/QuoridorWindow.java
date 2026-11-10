package quoridor.view;

import quoridor.view.screens.HomePanel;

import javax.swing.*;
import java.awt.*;

public class QuoridorWindow extends JFrame {

    /* The layout that shows only one page at a time */
    private CardLayout cardLayout;

    /* The panel that holds all the pages */
    private JPanel container;

    public QuoridorWindow() {
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

        /* We add each page with its name */
        container.add(new HomePanel(), "Acceuil");

        /* We show the home page first */
        showPage("Acceuil");

        /* To make visible */
        setVisible(true);
    }

    /* To show the page with the given name */
    public void showPage(String name) {
        cardLayout.show(container, name);
    }
}