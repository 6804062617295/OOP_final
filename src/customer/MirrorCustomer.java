package customer;

import item.Recipe;

import java.awt.*;

/**
 * Customer requesting dishes in reversed ingredient sequence.
 */
public class MirrorCustomer extends Customer {

    public MirrorCustomer(double patienceMultiplier) {
        super(randomRecipe(Recipe.MENU), 42 * patienceMultiplier);
    }

    @Override
    public boolean isReversed() {
        return true; // POLYMORPHISM: demands reflected/reversed order
    }

    @Override
    protected double getPaymentMultiplier() {
        return 1.1; // 10% bonus tip for following custom reversed order
    }

    @Override
    public String getLabel() {
        return "Mirror Demon";
    }

    @Override
    protected String getSpriteName() {
        return "customer_mirror";
    }

    @Override
    protected Color getColor() {
        return new Color(90, 150, 235); // Mystic mirror cyan-blue
    }
}
