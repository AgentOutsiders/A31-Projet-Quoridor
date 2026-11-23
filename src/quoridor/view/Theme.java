package quoridor.view;

import java.awt.*;

public class Theme {

    private static final Color[] PAWN_COLORS = new Color[] {
            new Color(0x46, 0x86, 0xC3),
            new Color(0xD0, 0x2D, 0x2B),
            new Color(0x6B, 0xCC, 0x4A),
            new Color(0xFE, 0xC5, 0x1B)
    };

    public static Color getPawnColor(int index) {
        if (index >= 0 && index < PAWN_COLORS.length) {
            return PAWN_COLORS[index];
        }
        return Color.BLACK;
    }
}
