package customer;

import item.Recipe;

import java.awt.*;

// VIP customer: low patience, pays 2x
public class VipCustomer extends Customer {

    public VipCustomer(double patienceMultiplier) {
        super(randomRecipe(Recipe.MENU), 30 * patienceMultiplier);
    }

    @Override
    protected double getPaymentMultiplier() {
        return 2.0;
    }

    @Override
    public String getLabel() {
        return "VIP";
    }

    @Override
    protected String getSpriteName() {
        return "customer_vip";
    }

    @Override
    protected Color getColor() {
        return new Color(250, 210, 50);
    }
}
