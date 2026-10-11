package customer;

import item.Recipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Kid customer: high patience, small dishes, pays less
public class KidCustomer extends Customer {

    public KidCustomer(double patienceMultiplier) {
        super(randomRecipe(kidsMenu()), 60 * patienceMultiplier);
    }

    // Dishes with 2 ingredients or fewer
    private static List<Recipe> kidsMenu() {
        List<Recipe> smallDishes = new ArrayList<>();
        for (Recipe recipe : Recipe.MENU) {
            if (recipe.getIngredients().size() <= 2) {
                smallDishes.add(recipe);
            }
        }
        return smallDishes;
    }

    @Override
    protected double getPaymentMultiplier() {
        return 0.8;
    }

    @Override
    public String getLabel() {
        return "Kid";
    }

    @Override
    protected String getSpriteName() {
        return "customer_kid";
    }

    @Override
    protected Color getColor() {
        return new Color(120, 200, 240);
    }
}
