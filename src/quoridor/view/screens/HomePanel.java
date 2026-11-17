package quoridor.view.screens;

import quoridor.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePanel extends JPanel {

    /* Color palette of the home screen */
    private static final Color BACKGROUND = new Color(0xEF, 0xE4, 0xD2);
    private static final Color TITLE_BACKGROUND = new Color(0x8A, 0x5A, 0x33);
    private static final Color PRIMARY = new Color(0x8B, 0x3A, 0x2B);
    private static final Color PRIMARY_HOVER = new Color(0xA2, 0x49, 0x3A);
    private static final Color TEXT_LIGHT = new Color(0xFB, 0xF6, 0xEE);

    public HomePanel(GameController controller) {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        JLabel titleLabel = createLabel("Quoridor");
        JButton playButton = createButton("Jouer");
        JButton quitButton = createButton("Quitter");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;

        /* The title with a big space below it */
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 40, 0);
        add(titleLabel, constraints);

        /* Normal space between the buttons */
        constraints.insets = new Insets(10, 0, 10, 0);

        /* The play button */
        constraints.gridy = 1;
        add(playButton, constraints);

        /* The quit button */
        constraints.gridy = 2;
        add(quitButton, constraints);

        /* Button actions */
        playButton.addActionListener(e -> controller.play());
        quitButton.addActionListener(e -> controller.quit());
    }

    private JButton createButton(String buttonName) {
        JButton button = new JButton(buttonName);
        button.setBackground(PRIMARY);
        button.setForeground(TEXT_LIGHT);

        button.setPreferredSize(new Dimension(220, 60));

        /* To remove the focus line */
        button.setFocusPainted(false);

        /* To remove the default border */
        button.setBorderPainted(false);

        /* To show the background */
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 24));

        /* The cursor changes to a hand when you hover over it */
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /* Hover effect */
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY);
            }
        });

        /* We return the button */
        return button;
    }

    private JLabel createLabel(String labelName) {
        JLabel label = new JLabel(labelName);

        label.setBackground(TITLE_BACKGROUND);
        label.setForeground(TEXT_LIGHT);
        label.setPreferredSize(new Dimension(400, 100));

        /* To show the background */
        label.setOpaque(true);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 40));

        /* We return the label */
        return label;
    }
}