package customer;

import item.Recipe;

import java.awt.*;

// Food critic: 3x pay if fast, half price if slow
public class CriticCustomer extends Customer {

    public CriticCustomer(double patienceMultiplier) {
        super(randomRecipe(Recipe.MENU), 40 * patienceMultiplier);
    }

    @Override
    public int pay() {
        int price = getOrder().getPrice();
        if (getPatienceRatio() > 0.5) {
            return price * 3; // great review!
        }
        return price / 2;     // bad review...
    }

    @Override
    public String getLabel() {
        return "Critic";
    }

    @Override
    protected String getSpriteName() {
        return "customer_critic";
    }

    @Override
    protected Color getColor() {
        return new Color(170, 110, 200);
    }
}
