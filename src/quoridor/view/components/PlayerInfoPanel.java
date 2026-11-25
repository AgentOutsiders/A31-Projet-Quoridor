package quoridor.view.components;

import quoridor.model.players.Player;

import javax.swing.*;
import java.awt.*;

/* Shows one player's information in a rounded wood frame */
public class PlayerInfoPanel extends JPanel {

    /* Colors of the panel, taken from the wood palette */
    private static final Color BACKGROUND = new Color(0xD9B287);
    private static final Color FRAME_COLOR = new Color(0x8A5A33);
    private static final Color TEXT_COLOR = new Color(0x3B2A1A);

    /* Fonts of the title and of the information lines */
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 28);
    private static final Font INFO_FONT = new Font("SansSerif", Font.BOLD, 14);

    /* Thickness of the three layers, in pixels */
    private static final int FRAME_THICKNESS = 8;
    private static final int HIGHLIGHT_THICKNESS = 3;
    private static final int PADDING = 10;

    /* Size of the rounded corners */
    private static final int ARC = 24;

    private JLabel m_playerName;
    private JLabel m_remainingWalls;
    private JLabel m_numberOfMoves;
    private Color m_playerColor;
    private Player m_player;

    /* True when it is the turn of this player */
    private boolean m_active;

    public PlayerInfoPanel(Player player, Color playerColor) {
        m_player = player;
        m_playerColor = playerColor;

        setLayout(new BorderLayout());

        /* The frame is drawn by hand, so Swing must not paint a square background */
        setOpaque(false);

        /* Empty border: keeps the text inside the frame */
        int inset = FRAME_THICKNESS + HIGHLIGHT_THICKNESS + PADDING;
        setBorder(BorderFactory.createEmptyBorder(inset, inset, inset, inset));

        /* The texts of the information lines are written by refresh */
        m_playerName = createTitleLabel(player.getName());
        m_remainingWalls = createInfoLabel("");
        m_numberOfMoves = createInfoLabel("");

        /* Name and pawn color side by side */
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        header.setOpaque(false);
        header.add(m_playerName);
        header.add(new ColorDot(m_playerColor));

        JPanel infoContainer = new JPanel(new GridLayout(2, 1));
        infoContainer.setOpaque(false);
        infoContainer.add(m_remainingWalls);
        infoContainer.add(m_numberOfMoves);

        add(header, BorderLayout.NORTH);
        add(infoContainer, BorderLayout.CENTER);

        refresh(false);
    }

    /* Updates the numbers and shows if it is the turn of this player */
    public void refresh(boolean active) {
        m_remainingWalls.setText("Murs restants : " + m_player.getRemainingWalls());
        m_numberOfMoves.setText("Déplacements : " + m_player.getMoveCount());

        m_active = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /* The highlight has the background color when it is not the turn of this player */
        Color highlightColor = m_active ? m_playerColor : BACKGROUND;

        /* Three rounded rectangles, from the outside to the inside */
        drawLayer(g2, FRAME_COLOR, 0);
        drawLayer(g2, highlightColor, FRAME_THICKNESS);
        drawLayer(g2, BACKGROUND, FRAME_THICKNESS + HIGHLIGHT_THICKNESS);

        g2.dispose();
    }

    /* Fills a rounded rectangle that is inset pixels smaller than the panel on each side */
    private void drawLayer(Graphics2D g2, Color color, int inset) {
        int arc = Math.max(0, ARC - 2 * inset);
        g2.setColor(color);
        g2.fillRoundRect(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset, arc, arc);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(BACKGROUND);
        label.setFont(INFO_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = createInfoLabel(text);
        label.setFont(TITLE_FONT);
        return label;
    }
}