package item;

import game.Assets;

import java.awt.*;

/**
 * A single ingredient in a specific state (raw, chopped, cooked, or burnt).
 */
public class Ingredient implements Item {
    private final IngredientType type;
    private IngredientState state;

    public Ingredient(IngredientType type) {
        this(type, IngredientState.RAW);
    }

    public Ingredient(IngredientType type, IngredientState state) {
        this.type = type;
        this.state = state;
    }

    public boolean canBeChopped() {
        return type.isChoppable() && state == IngredientState.RAW;
    }

    public boolean canBeCooked() {
        return type.isCookable() && state == IngredientState.RAW;
    }

    public void chop() {
        if (canBeChopped()) {
            state = IngredientState.CHOPPED;
        }
    }

    public void cook() {
        if (canBeCooked()) {
            state = IngredientState.COOKED;
        }
    }

    public void burn() {
        state = IngredientState.BURNT;
    }

    /** True if both ingredients have the same type AND the same state. */
    public boolean isSameAs(Ingredient other) {
        return type == other.type && state == other.state;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientState getState() {
        return state;
    }

    /** File name of the sprite, e.g. "tomato_chopped" -> assets/tomato_chopped.png */
    public String getSpriteName() {
        return type.name().toLowerCase() + "_" + state.name().toLowerCase();
    }

    @Override
    public String getName() {
        if (state == IngredientState.RAW) {
            return type.getDisplayName();
        }
        return state.getPrefix() + " " + type.getDisplayName();
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int size) {
        Assets.draw(g, getSpriteName(), x, y, size, size, getPlaceholderColor(), getPlaceholderLabel());
    }

    private Color getPlaceholderColor() {
        return switch (state) {
            case RAW -> type.getColor();
            case CHOPPED -> type.getColor().brighter();
            case COOKED -> type.getColor().darker().darker();
            case BURNT -> Color.DARK_GRAY;
        };
    }

    private String getPlaceholderLabel() {
        if (state == IngredientState.RAW) {
            return type.getDisplayName().substring(0, 3);
        }
        return state.getTag();
    }
}
