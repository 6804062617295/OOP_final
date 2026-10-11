package item;

import game.Assets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Plate that holds ingredients for dishes
public class Plate implements Item {
    private static final int MAX_INGREDIENTS = 4;

    private PlateColor color;
    private final List<Ingredient> ingredients = new ArrayList<>();

    public Plate() {
        this(PlateColor.RED);
    }

    public Plate(PlateColor color) {
        this.color = color;
    }

    // Adds an ingredient if valid
    public boolean addIngredient(Ingredient ingredient) {
        if (ingredient.getState() == IngredientState.BURNT) {
            return false;
        }
        if (ingredients.size() >= MAX_INGREDIENTS) {
            return false;
        }
        for (Ingredient onPlate : ingredients) {
            if (onPlate.getType() == ingredient.getType()) {
                return false;
            }
        }
        ingredients.add(ingredient);
        return true;
    }

    // Removes the top ingredient
    public Ingredient removeLastIngredient() {
        if (ingredients.isEmpty()) {
            return null;
        }
        return ingredients.remove(ingredients.size() - 1);
    }

    // Checks if plate matches recipe
    public boolean matches(Recipe recipe) {
        return matches(recipe, false);
    }

    // Checks if plate matches recipe (supports reverse order)
    public boolean matches(Recipe recipe, boolean reversed) {
        return recipe.isMadeFrom(color, ingredients, reversed);
    }

    public boolean isEmpty() {
        return ingredients.isEmpty();
    }

    public void clear() {
        ingredients.clear();
    }

    // Cycles to next plate color
    public void cycleColor() {
        this.color = this.color.next();
    }

    public PlateColor getColor() {
        return color;
    }

    public void setColor(PlateColor color) {
        this.color = color;
    }

    // Read-only list of ingredients
    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public String getName() {
        return isEmpty()
                ? (color.getDisplayName() + " Plate")
                : (color.getDisplayName() + " Plate (" + ingredients.size() + " items)");
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int size) {
        Color rimColor = color.getColor();
        g.setColor(rimColor);
        g.fillOval(x, y, size, size);

        g.setColor(rimColor.darker());
        g.setStroke(new BasicStroke(2));
        g.drawOval(x + 1, y + 1, size - 2, size - 2);
        g.setStroke(new BasicStroke(1));

        int inset = (int) (size * 0.16);
        g.setColor(new Color(245, 245, 242));
        g.fillOval(x + inset, y + inset, size - inset * 2, size - inset * 2);

        if (isEmpty()) {
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, size / 4)));
            g.setColor(rimColor.darker());
            Assets.drawCenteredText(g, color.getDisplayName().substring(0, 1), x + size / 2, y + size / 2);
            return;
        }

        // Draw ingredients
        if (ingredients.size() == 1) {
            ingredients.get(0).draw(g, x + size / 4, y + size / 4, size / 2);
        } else if (ingredients.size() == 2) {
            int itemSize = (int) (size * 0.42);
            ingredients.get(0).draw(g, x + 4, y + size / 2 - itemSize / 2, itemSize);
            ingredients.get(1).draw(g, x + size - itemSize - 4, y + size / 2 - itemSize / 2, itemSize);
        } else {
            int half = size / 2;
            for (int i = 0; i < ingredients.size(); i++) {
                int ix = x + (i % 2) * half;
                int iy = y + (i / 2) * half;
                ingredients.get(i).draw(g, ix + 2, iy + 2, half - 4);
            }
        }
    }
}
