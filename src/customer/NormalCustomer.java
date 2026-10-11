package customer;

import item.Recipe;

import java.awt.*;

/** A regular customer. Orders anything, average patience, normal price. */
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
