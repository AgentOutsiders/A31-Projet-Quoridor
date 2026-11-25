package quoridor.view.components;

import javax.swing.*;
import java.awt.*;

/* Small filled circle that shows a player's pawn color */
public class ColorDot extends JPanel {

    private static final int DIAMETER = 24;
    private static final Color OUTLINE = new Color(0x8A5A33);

    private Color m_color;

    public ColorDot(Color color) {
        m_color = color;
        setOpaque(false);
        setPreferredSize(new Dimension(DIAMETER, DIAMETER));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        drawCircle(g2, m_color, 0, 0, DIAMETER);
        g2.dispose();
    }

    /* Draws a filled circle with a thin outline */
    private void drawCircle(Graphics2D g2, Color color, int x, int y, int diameter) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.fillOval(x, y, diameter, diameter);

        g2.setColor(OUTLINE);
        g2.drawOval(x, y, diameter - 1, diameter - 1);
    }
}