package quoridor.view.screens;

import quoridor.controller.GameController;
import quoridor.model.GameMode;

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
    private JToggleButton m_classicButton;
    private JToggleButton m_pacmanButton;

    /* Grid size choice */
    private JToggleButton m_grid9Button;
    private JToggleButton m_grid11Button;

    /* Number of walls choice */
    private JToggleButton m_wall8Button;
    private JToggleButton m_wall10Button;
    private JToggleButton m_wall12Button;

    /* Number of players choice */
    private JToggleButton m_player2Button;
    private JToggleButton m_player4Button;

    /* Opponent choice */
    private JToggleButton m_humanButton;
    private JToggleButton m_computerButton;

    /* Groups of buttons, to clear the selection in Pacman mode */
    private ButtonGroup m_wallGroup;
    private ButtonGroup m_playerGroup;
    private ButtonGroup m_opponentGroup;

    /* Action buttons */
    private JButton m_startButton;
    private JButton m_backButton;

    public ConfigPanel(GameController controller) {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        /* We create the buttons */
        m_classicButton = createToggleButton("Classique");
        m_pacmanButton = createToggleButton("Pacman");

        m_grid9Button = createToggleButton("9 x 9");
        m_grid11Button = createToggleButton("11 x 11");

        m_wall8Button = createToggleButton("8");
        m_wall10Button = createToggleButton("10");
        m_wall12Button = createToggleButton("12");

        m_player2Button = createToggleButton("2");
        m_player4Button = createToggleButton("4");

        m_humanButton = createToggleButton("Humain");
        m_computerButton = createToggleButton("Ordinateur");

        m_startButton = createButton("Lancer la partie");
        m_backButton = createButton("Retour");

        /* Only one button can be selected in each group */
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(m_classicButton);
        modeGroup.add(m_pacmanButton);

        ButtonGroup gridGroup = new ButtonGroup();
        gridGroup.add(m_grid9Button);
        gridGroup.add(m_grid11Button);

        m_wallGroup = new ButtonGroup();
        m_wallGroup.add(m_wall8Button);
        m_wallGroup.add(m_wall10Button);
        m_wallGroup.add(m_wall12Button);

        m_playerGroup = new ButtonGroup();
        m_playerGroup.add(m_player2Button);
        m_playerGroup.add(m_player4Button);

        m_opponentGroup = new ButtonGroup();
        m_opponentGroup.add(m_humanButton);
        m_opponentGroup.add(m_computerButton);

        /* Default choices */
        m_classicButton.setSelected(true);
        m_grid9Button.setSelected(true);
        m_wall10Button.setSelected(true);
        m_player2Button.setSelected(true);
        m_humanButton.setSelected(true);

        /* When the mode or the number of players changes, we update the other options */
        m_classicButton.addActionListener(e -> updateOptions());
        m_pacmanButton.addActionListener(e -> updateOptions());
        m_player2Button.addActionListener(e -> updateOptions());
        m_player4Button.addActionListener(e -> updateOptions());

        /* We apply the rules once at the start */
        updateOptions();

        /* The rows of buttons: one column per button, all the same width */
        JPanel modeRow = new JPanel(new GridLayout(1, 2, 8, 0));
        modeRow.setOpaque(false);
        modeRow.add(m_classicButton);
        modeRow.add(m_pacmanButton);

        JPanel gridRow = new JPanel(new GridLayout(1, 2, 8, 0));
        gridRow.setOpaque(false);
        gridRow.add(m_grid9Button);
        gridRow.add(m_grid11Button);

        JPanel wallRow = new JPanel(new GridLayout(1, 3, 8, 0));
        wallRow.setOpaque(false);
        wallRow.add(m_wall8Button);
        wallRow.add(m_wall10Button);
        wallRow.add(m_wall12Button);

        JPanel playerRow = new JPanel(new GridLayout(1, 2, 8, 0));
        playerRow.setOpaque(false);
        playerRow.add(m_player2Button);
        playerRow.add(m_player4Button);

        JPanel opponentRow = new JPanel(new GridLayout(1, 2, 8, 0));
        opponentRow.setOpaque(false);
        opponentRow.add(m_humanButton);
        opponentRow.add(m_computerButton);

        JPanel actionRow = new JPanel(new GridLayout(1, 2, 8, 0));
        actionRow.setOpaque(false);
        actionRow.add(m_backButton);
        actionRow.add(m_startButton);

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
        m_startButton.addActionListener(e -> controller.startGame());
        m_backButton.addActionListener(e -> controller.goHome());
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
        boolean pacman = m_pacmanButton.isSelected();

        if (pacman) {
            /* Pacman: 9 x 9 grid, and no walls, players or opponent choice */
            m_grid9Button.setSelected(true);
            m_wallGroup.clearSelection();
            m_playerGroup.clearSelection();
            m_opponentGroup.clearSelection();
        } else {
            /* Classic: back to the default choices if nothing is selected */
            if (m_wallGroup.getSelection() == null) {
                m_wall10Button.setSelected(true);
            }
            if (m_playerGroup.getSelection() == null) {
                m_player2Button.setSelected(true);
            }
            if (m_opponentGroup.getSelection() == null) {
                m_humanButton.setSelected(true);
            }
        }

        /* No computer with 4 players */
        boolean fourPlayers = m_player4Button.isSelected();
        if (fourPlayers) {
            m_humanButton.setSelected(true);
        }

        /* Grid, walls and players: only in classic mode */
        m_grid11Button.setEnabled(!pacman);
        m_wall8Button.setEnabled(!pacman);
        m_wall10Button.setEnabled(!pacman);
        m_wall12Button.setEnabled(!pacman);
        m_player2Button.setEnabled(!pacman);
        m_player4Button.setEnabled(!pacman);

        /* Opponent: only in classic mode, computer only with 2 players */
        m_humanButton.setEnabled(!pacman);
        m_computerButton.setEnabled(!pacman && !fourPlayers);

        /* We refresh the colors */
        updateColors(m_grid9Button);
        updateColors(m_grid11Button);
        updateColors(m_wall8Button);
        updateColors(m_wall10Button);
        updateColors(m_wall12Button);
        updateColors(m_player2Button);
        updateColors(m_player4Button);
        updateColors(m_humanButton);
        updateColors(m_computerButton);
    }

    public int getGridSize()
    {
        if(m_grid9Button.isSelected()) {
            return 9;
        }
        return 11;
    }

    public int getWallCount() {
        if(m_pacmanButton.isSelected()) {
            return 0;
        }
        else {
            if(m_wall8Button.isSelected()) {
                return 8;
            } else if (m_wall10Button.isSelected()) {
                return 10;
            }
            return 12;
        }
    }

    public GameMode getGameMode() {
        if(m_pacmanButton.isSelected()) {
            return GameMode.PACMAN;
        }
        return GameMode.CLASSIC;
    }

    public int getPlayerCount() {
        if(m_pacmanButton.isSelected()) {
            return 4;
        }
        else {
            if(m_player2Button.isSelected()) {
                return 2;
            }
            return 4;
        }
    }

    public boolean isComputerOpponent() {
        return m_computerButton.isSelected();
    }
}