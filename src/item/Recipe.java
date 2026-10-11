package item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static item.IngredientState.*;
import static item.IngredientType.*;

/**
 * A dish on the menu: a name, a price, required plate color, and exact sequence of ingredients.
 */
public class Recipe {

    /**
     * Menu recipes according to the Hell's Kitchen specifications:
     * - A: Cooked Meat
     * - B: Chopped Tomato
     * - C: Chopped Lettuce
     * - D: Raw Bread
     *
     * Red Plate: A -> B
     * Yellow Plate: B -> C
     * Green Plate: C -> D
     * Blue Plate: D -> A
     * Secret Black Plate: A -> C -> B -> D
     */
    public static final Recipe RED_DISH = new Recipe(
            "Hellfire Steak", 30, PlateColor.RED,
            new Ingredient(MEAT, COOKED),
            new Ingredient(TOMATO, CHOPPED)
    );

    public static final Recipe YELLOW_DISH = new Recipe(
            "Sunset Salad", 25, PlateColor.YELLOW,
            new Ingredient(TOMATO, CHOPPED),
            new Ingredient(LETTUCE, CHOPPED)
    );

    public static final Recipe GREEN_DISH = new Recipe(
            "Forest Toast", 25, PlateColor.GREEN,
            new Ingredient(LETTUCE, CHOPPED),
            new Ingredient(BREAD, RAW)
    );

    public static final Recipe BLUE_DISH = new Recipe(
            "Azure Burger", 35, PlateColor.BLUE,
            new Ingredient(BREAD, RAW),
            new Ingredient(MEAT, COOKED)
    );

    public static final Recipe SECRET_BLACK_DISH = new Recipe(
            "Abyssal Feast", 60, PlateColor.BLACK,
            new Ingredient(MEAT, COOKED),
            new Ingredient(LETTUCE, CHOPPED),
            new Ingredient(TOMATO, CHOPPED),
            new Ingredient(BREAD, RAW)
    );

    public static final List<Recipe> MENU = List.of(
            RED_DISH,
            YELLOW_DISH,
            GREEN_DISH,
            BLUE_DISH,
            SECRET_BLACK_DISH
    );

    public static Recipe fromPlateColor(PlateColor color) {
        if (color == null) return RED_DISH;
        return switch (color) {
            case RED -> RED_DISH;
            case YELLOW -> YELLOW_DISH;
            case GREEN -> GREEN_DISH;
            case BLUE -> BLUE_DISH;
            case BLACK -> SECRET_BLACK_DISH;
        };
    }

    private final String name;
    private final int price;
    private final PlateColor plateColor;
    private final List<Ingredient> ingredients;
    private final List<Ingredient> reversedIngredients;

    public Recipe(String name, int price, PlateColor plateColor, Ingredient... ingredients) {
        this.name = name;
        this.price = price;
        this.plateColor = plateColor;
        this.ingredients = List.of(ingredients);

        // Pre-compute the reversed list for reversed/male customer orders
        List<Ingredient> rev = new ArrayList<>(this.ingredients);
        Collections.reverse(rev);
        this.reversedIngredients = Collections.unmodifiableList(rev);
    }

    /**
     * Checks if the given ingredients match this recipe in exact sequential order.
     */
    public boolean isMadeFrom(PlateColor plateColor, List<Ingredient> items, boolean reversed) {
        if (this.plateColor != plateColor) {
            return false;
        }

        List<Ingredient> target = reversed ? reversedIngredients : ingredients;
        if (items.size() != target.size()) {
            return false;
        }

        for (int i = 0; i < target.size(); i++) {
            if (!items.get(i).isSameAs(target.get(i))) {
                return false;
            }
        }
        return true;
    }

    /** e.g. "[Red] Hellfire Steak ($30) = Cooked Meat -> Chopped Tomato" */
    public String getDescription() {
        List<String> names = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            names.add(ingredient.getName());
        }
        return "[" + plateColor.getDisplayName() + "] " + name + " ($" + price + ") = " + String.join(" -> ", names);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public PlateColor getPlateColor() {
        return plateColor;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Ingredient> getReversedIngredients() {
        return reversedIngredients;
    }
}
