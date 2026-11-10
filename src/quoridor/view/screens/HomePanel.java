package quoridor.view.screens;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel() {
        setLayout(new GridBagLayout());

        JButton playButton = createButton("Jouer");

        JButton quitButton = createButton("Quitter");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(10, 0, 10, 0);

        constraints.gridy = 0;
        add(playButton, constraints);

        constraints.gridy = 1;
        add(quitButton, constraints);
    }

    private JButton createButton(String buttonName) {
        JButton button = new JButton(buttonName);
        button.setBackground(new Color(0x14, 0x2A, 0x4F));   // navy blue
        button.setForeground(Color.WHITE);

        button.setPreferredSize(new Dimension(200, 50));

        /* For remove the focus line */
        button.setFocusPainted(false);

        /* For remove the default border */
        button.setBorderPainted(false);

        /* For show the background */
        button.setOpaque(true);

        /* We turn back the button */
        return button;
    }
}
