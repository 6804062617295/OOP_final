package customer;

import item.Recipe;

import java.awt.*;

// Inverted demon customer: orders ingredients in reverse
public class MirrorCustomer extends Customer {

    public MirrorCustomer(double patienceMultiplier) {
        super(randomRecipe(Recipe.MENU), 42 * patienceMultiplier);
    }

    @Override
    public boolean isReversed() {
        return true; // demands reversed order
    }

    @Override
    public boolean isFatalWhenAngry() {
        return true; // instant game over if patience runs out
    }

    @Override
    protected double getPaymentMultiplier() {
        return 1.25; // pays extra tip
    }

    @Override
    public String getLabel() {
        return "Inverted Demon";
    }

    @Override
    protected String getSpriteName() {
        return "customer_mirror";
    }

    @Override
    protected Color getColor() {
        return new Color(90, 150, 235);
    }
}
