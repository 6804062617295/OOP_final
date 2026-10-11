package item;

import game.Assets;

import java.awt.*;

// Ingredient with type and state (raw, chopped, cooked, burnt)
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

    // Check if type and state match
    public boolean isSameAs(Ingredient other) {
        return type == other.type && state == other.state;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientState getState() {
        return state;
    }

    // Sprite asset name
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
