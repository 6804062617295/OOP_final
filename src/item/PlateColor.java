package item;

import java.awt.*;

/**
 * Available plate colors and their UI representations.
 */
public enum PlateColor {
    RED("Red", new Color(225, 60, 60)),
    YELLOW("Yellow", new Color(245, 205, 40)),
    GREEN("Green", new Color(55, 190, 85)),
    BLUE("Blue", new Color(50, 140, 240)),
    BLACK("Black (Secret)", new Color(35, 35, 40));

    private final String displayName;
    private final Color color;

    PlateColor(String displayName, Color color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    /** Cycles to the next plate color (Red -> Yellow -> Green -> Blue -> Black -> Red). */
    public PlateColor next() {
        PlateColor[] all = values();
        return all[(ordinal() + 1) % all.length];
    }
}
