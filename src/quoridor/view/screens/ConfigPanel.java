package quoridor.view.screens;

import quoridor.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfigPanel extends JPanel {

    /* Color palette of the config screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);
    private static final Color PRIMARY = new Color(0x8B, 0x3A, 0x2B);
    private static final Color TEXT_LIGHT = new Color(0xFB, 0xF6, 0xEE);
    private static final Color TEXT_DARK = new Color(0x3B, 0x2A, 0x1A);
    private static final Color UNSELECTED = new Color(0xFB, 0xF6, 0xEE);
    private static final Color DISABLED = new Color(0xE2, 0xD6, 0xC3);
    private static final Color DISABLED_TEXT = new Color(0xB0, 0xA0, 0x8A);
    private static final Color ACTION = new Color(0x3B, 0x2A, 0x1A);
    private static final Color ACTION_HOVER = new Color(0x5A, 0x41, 0x30);

    /* Game mode choice */
    private JToggleButton classicButton;
    private JToggleButton pacmanButton;

    /* Grid size choice */
    private JToggleButton grid9Button;
    private JToggleButton grid11Button;

    /* Number of walls choice */
    private JToggleButton wall8Button;
    private JToggleButton wall10Button;
    private JToggleButton wall12Button;

    /* Number of players choice */
    private JToggleButton player2Button;
    private JToggleButton player4Button;

    /* Opponent choice */
    private JToggleButton humanButton;
    private JToggleButton computerButton;

    /* Groups of buttons, to clear the selection in Pacman mode */
    private ButtonGroup wallGroup;
    private ButtonGroup playerGroup;
    private ButtonGroup opponentGroup;

    /* Action buttons */
    private JButton startButton;
    private JButton backButton;

    public ConfigPanel(GameController controller) {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        /* We create the buttons */
        classicButton = createToggleButton("Classique");
        pacmanButton = createToggleButton("Pacman");

        grid9Button = createToggleButton("9 x 9");
        grid11Button = createToggleButton("11 x 11");

        wall8Button = createToggleButton("8");
        wall10Button = createToggleButton("10");
        wall12Button = createToggleButton("12");

        player2Button = createToggleButton("2");
        player4Button = createToggleButton("4");

        humanButton = createToggleButton("Humain");
        computerButton = createToggleButton("Ordinateur");

        startButton = createButton("Lancer la partie");
        backButton = createButton("Retour");

        /* Only one button can be selected in each group */
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(classicButton);
        modeGroup.add(pacmanButton);

        ButtonGroup gridGroup = new ButtonGroup();
        gridGroup.add(grid9Button);
        gridGroup.add(grid11Button);

        wallGroup = new ButtonGroup();
        wallGroup.add(wall8Button);
        wallGroup.add(wall10Button);
        wallGroup.add(wall12Button);

        playerGroup = new ButtonGroup();
        playerGroup.add(player2Button);
        playerGroup.add(player4Button);

        opponentGroup = new ButtonGroup();
        opponentGroup.add(humanButton);
        opponentGroup.add(computerButton);

        /* Default choices */
        classicButton.setSelected(true);
        grid9Button.setSelected(true);
        wall10Button.setSelected(true);
        player2Button.setSelected(true);
        humanButton.setSelected(true);

        /* When the mode or the number of players changes, we update the other options */
        classicButton.addActionListener(e -> updateOptions());
        pacmanButton.addActionListener(e -> updateOptions());
        player2Button.addActionListener(e -> updateOptions());
        player4Button.addActionListener(e -> updateOptions());

        /* We apply the rules once at the start */
        updateOptions();

        /* The rows of buttons: one column per button, all the same width */
        JPanel modeRow = new JPanel(new GridLayout(1, 2, 8, 0));
        modeRow.setOpaque(false);
        modeRow.add(classicButton);
        modeRow.add(pacmanButton);

        JPanel gridRow = new JPanel(new GridLayout(1, 2, 8, 0));
        gridRow.setOpaque(false);
        gridRow.add(grid9Button);
        gridRow.add(grid11Button);

        JPanel wallRow = new JPanel(new GridLayout(1, 3, 8, 0));
        wallRow.setOpaque(false);
        wallRow.add(wall8Button);
        wallRow.add(wall10Button);
        wallRow.add(wall12Button);

        JPanel playerRow = new JPanel(new GridLayout(1, 2, 8, 0));
        playerRow.setOpaque(false);
        playerRow.add(player2Button);
        playerRow.add(player4Button);

        JPanel opponentRow = new JPanel(new GridLayout(1, 2, 8, 0));
        opponentRow.setOpaque(false);
        opponentRow.add(humanButton);
        opponentRow.add(computerButton);

        JPanel actionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(backButton);
        actionRow.add(startButton);

        /* We place the sections on the page, one per line */
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 24, 0);

        constraints.gridy = 0;
        add(createSection("Mode de jeu", modeRow), constraints);

        constraints.gridy = 1;
        add(createSection("Taille de la grille", gridRow), constraints);

        constraints.gridy = 2;
        add(createSection("Murs par joueur", wallRow), constraints);

        constraints.gridy = 3;
        add(createSection("Nombre de joueurs", playerRow), constraints);

        constraints.gridy = 4;
        add(createSection("Adversaire", opponentRow), constraints);

        /* The action buttons, with more space above them */
        constraints.gridy = 5;
        constraints.insets = new Insets(16, 0, 0, 0);
        add(actionRow, constraints);

        /* Button actions */
        startButton.addActionListener(e -> controller.startGame());
        backButton.addActionListener(e -> controller.goHome());
    }

    /* Creates a section: a title above a row of buttons */
    private JPanel createSection(String title, JPanel row) {
        JPanel section = new JPanel(new BorderLayout(0, 8));
        section.setOpaque(false);

        /* The title of the section */
        JLabel label = new JLabel(title);
        label.setForeground(TEXT_DARK);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));

        /* Title on top, row below */
        section.add(label, BorderLayout.NORTH);
        section.add(row, BorderLayout.CENTER);

        return section;
    }

    /* Creates a button that stays selected, with our colors */
    private JToggleButton createToggleButton(String text) {
        JToggleButton button = new JToggleButton(text);

        /* Small width: the layout stretches the buttons to fill the row */
        button.setPreferredSize(new Dimension(120, 50));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));

        /* To remove the default Swing style */
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        /* The cursor changes to a hand when you hover over it */
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /* Colors at the start: not selected */
        updateColors(button);

        /* Each time the button is selected or unselected, we update its colors */
        button.addItemListener(e -> updateColors(button));

        return button;
    }

    /* Creates a dark brown action button with a hover effect */
    private JButton createButton(String text) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(120, 50));
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(ACTION);
        button.setForeground(TEXT_LIGHT);

        /* To remove the default Swing style */
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        /* The cursor changes to a hand when you hover over it */
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /* Hover effect */
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACTION_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACTION);
            }
        });

        return button;
    }

    /* Grey if disabled, brick red if selected, cream if not */
    private void updateColors(JToggleButton button) {
        if (!button.isEnabled()) {
            button.setBackground(DISABLED);
            button.setForeground(DISABLED_TEXT);
        }
        else if (button.isSelected()) {
            button.setBackground(PRIMARY);
            button.setForeground(TEXT_LIGHT);
        }
        else {
            button.setBackground(UNSELECTED);
            button.setForeground(TEXT_DARK);
        }
    }

    /* Enables, disables or forces the options depending on the mode and the number of players */
    private void updateOptions() {
        boolean pacman = pacmanButton.isSelected();

        if (pacman) {
            /* Pacman: 9 x 9 grid, and no walls, players or opponent choice */
            grid9Button.setSelected(true);
            wallGroup.clearSelection();
            playerGroup.clearSelection();
            opponentGroup.clearSelection();
        } else {
            /* Classic: back to the default choices if nothing is selected */
            if (wallGroup.getSelection() == null) {
                wall10Button.setSelected(true);
            }
            if (playerGroup.getSelection() == null) {
                player2Button.setSelected(true);
            }
            if (opponentGroup.getSelection() == null) {
                humanButton.setSelected(true);
            }
        }

        /* No computer with 4 players */
        boolean fourPlayers = player4Button.isSelected();
        if (fourPlayers) {
            humanButton.setSelected(true);
        }

        /* Grid, walls and players: only in classic mode */
        grid11Button.setEnabled(!pacman);
        wall8Button.setEnabled(!pacman);
        wall10Button.setEnabled(!pacman);
        wall12Button.setEnabled(!pacman);
        player2Button.setEnabled(!pacman);
        player4Button.setEnabled(!pacman);

        /* Opponent: only in classic mode, computer only with 2 players */
        humanButton.setEnabled(!pacman);
        computerButton.setEnabled(!pacman && !fourPlayers);

        /* We refresh the colors */
        updateColors(grid9Button);
        updateColors(grid11Button);
        updateColors(wall8Button);
        updateColors(wall10Button);
        updateColors(wall12Button);
        updateColors(player2Button);
        updateColors(player4Button);
        updateColors(humanButton);
        updateColors(computerButton);
    }
}