package quoridor.view.components;

import quoridor.model.Game;
import quoridor.model.board.Position;
import quoridor.model.players.Player;
import quoridor.controller.GameController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    /* Size of a cell and space between cells, in pixels */
    private static final int CELL = 50;
    private static final int GAP = 8;

    /* Diameter of a pawn, smaller than a cell */
    private static final int PAWN = 30;

    /* Thickness of the outer frame and space between the board edge and the cells */
    private static final int FRAME_MARGIN = 20;
    private static final int BOARD_MARGIN = 10;

    /* Position of the first cell : the grid is shifted by both margins */
    private static final int OFFSET = FRAME_MARGIN + BOARD_MARGIN;

    /* Roundness of the corners */
    private static final int FRAME_ARC = 30;
    private static final int BOARD_ARC = 20;
    private static final int CELL_ARC = 10;

    /* Color palette of the board */
    private static final Color FRAME = new Color(0x8A, 0x5A, 0x33);
    private static final Color BOARD = new Color(0xA9, 0x71, 0x3F);
    private static final Color CELL_COLOR = new Color(0xD9, 0xB2, 0x87);


    private static final Color[] PAWN_COLORS = new Color[] {
            new Color(0x46, 0x86, 0xC3),
            new Color(0xD0, 0x2D, 0x2B),
            new Color(0x6B, 0xCC, 0x4A),
            new Color(0xFE, 0xC5, 0x1B)
    };

    /* The current game to draw, given when the player starts a game */
    private Game game;

    /* The controller, to send the clicked cell */
    private GameController controller;

    public BoardPanel(GameController controller) {
        /* We draw the whole background ourselves, with rounded corners */
        setOpaque(false);

        this.controller = controller;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                /* We convert the click in pixels into a cell of the grid */
                Position cell = getCellAt(e.getX(), e.getY());
                if(cell != null) {
                    System.out.println("Cell cliquée : " + cell);
                    controller.onCellClicked(cell);
                }
            }
        });
    }

    /* To give the game to the board, then draw it */
    public void setGame(Game game) {
        this.game = game;

        /* The panel size depends on the grid size (9 or 11), plus the margins on both sides */
        int totalPixels = getGridPixels() + OFFSET * 2;
        setPreferredSize(new Dimension(totalPixels, totalPixels));

        /* The layout must take the new size into account, then we redraw */
        revalidate();
        repaint();
    }

    /* Width (and height) of the grid of cells, without the margins */
    private int getGridPixels() {
        int size = game.getBoard().getSize();
        return size * CELL + (size - 1) * GAP;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /* Nothing to draw before a game is started */
        if (game == null) {
            return;
        }

        /* Smooth rounded corners */
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawFrame(g2);
        drawBoard(g2);
        drawCells(g2);
        drawPawns(g2);
    }

    /* Outer wooden frame: fills the whole panel */
    private void drawFrame(Graphics2D g2) {
        g2.setColor(FRAME);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), FRAME_ARC, FRAME_ARC);
    }

    /* Inner board: inside the frame, around the cells */
    private void drawBoard(Graphics2D g2) {
        int boardSize = getGridPixels() + BOARD_MARGIN * 2;
        g2.setColor(BOARD);
        g2.fillRoundRect(FRAME_MARGIN, FRAME_MARGIN, boardSize, boardSize, BOARD_ARC, BOARD_ARC);
    }

    /* Cells: one loop for the columns, one for the rows, shifted by the margins */
    private void drawCells(Graphics2D g2) {
        int size = game.getBoard().getSize();

        g2.setColor(CELL_COLOR);
        for (int column = 0; column < size; column++) {
            for (int row = 0; row < size; row++) {
                int x = OFFSET + column * (CELL + GAP);
                int y = OFFSET + row * (CELL + GAP);
                g2.fillRoundRect(x, y, CELL, CELL, CELL_ARC, CELL_ARC);
            }
        }
    }

    /* Pawns: one circle per player, centered in its cell */
    private void drawPawns(Graphics2D g2) {
        List<Player> players = game.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            /* The player number i and its position on the grid */
            Player player = players.get(i);
            Position position = player.getPosition();

            /* Top-left corner of its cell, in pixels */
            int cellX = OFFSET + position.getX() * (CELL + GAP);
            int cellY = OFFSET + position.getY() * (CELL + GAP);

            /* Shift to center the pawn in the cell */
            int shift = (CELL - PAWN) / 2;

            /* Each player has its own color */
            g2.setColor(PAWN_COLORS[i]);
            g2.fillOval(cellX + shift, cellY + shift, PAWN, PAWN);
        }
    }

    /* Converts a click in pixels into the cell under it, or null if it is not on a cell */
    private Position getCellAt(int pixelX, int pixelY) {
        if (game == null) {
            return null;
        }

        /* Pixels from the top-left corner of the grid, without the margins */
        int gridX = pixelX - OFFSET;
        int gridY = pixelY - OFFSET;

        /* Click on the frame, before the grid */
        if (gridX < 0 || gridY < 0) {
            return null;
        }

        /* Number of the column and the row (one cell + one corridor = CELL + GAP pixels) */
        int column = gridX / (CELL + GAP);
        int row = gridY / (CELL + GAP);

        /* Position inside this cell + corridor block */
        int insideX = gridX % (CELL + GAP);
        int insideY = gridY % (CELL + GAP);

        /* Click in a corridor, between two cells */
        if (insideX >= CELL || insideY >= CELL) {
            return null;
        }

        /* Click after the last cell */
        int size = game.getBoard().getSize();
        if (column >= size || row >= size) {
            return null;
        }

        return new Position(column, row);
    }
}