package item;

import java.awt.*;

// Kitchen ingredients and properties
public enum IngredientType {
    //       name       placeholder color          canChop  canCook
    BREAD("Bread", new Color(222, 184, 135), false, false),
    LETTUCE("Lettuce", new Color(110, 190, 70), true, false),
    TOMATO("Tomato", new Color(225, 60, 50), true, false),
    MEAT("Meat", new Color(205, 110, 110), false, true);

    private final String displayName;
    private final Color color;
    private final boolean choppable;
    private final boolean cookable;

    IngredientType(String displayName, Color color, boolean choppable, boolean cookable) {
        this.displayName = displayName;
        this.color = color;
        this.choppable = choppable;
        this.cookable = cookable;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    public boolean isChoppable() {
        return choppable;
    }

    public boolean isCookable() {
        return cookable;
    }
}
