package customer;

import item.Recipe;

import java.awt.*;

// Regular customer with standard patience and pay
public class NormalCustomer extends Customer {

    public NormalCustomer(double patienceMultiplier) {
        super(randomRecipe(Recipe.MENU), 45 * patienceMultiplier);
    }

    @Override
    public String getLabel() {
        return "Guest";
    }

    @Override
    protected String getSpriteName() {
        return "customer_normal";
    }

    @Override
    protected Color getColor() {
        return new Color(120, 180, 120);
    }
}
