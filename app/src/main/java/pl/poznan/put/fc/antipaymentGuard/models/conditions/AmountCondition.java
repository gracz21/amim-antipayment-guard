package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import java.io.Serializable;

/**
 * @author Kamil Walkowiak
 */
public class AmountCondition extends Condition implements Serializable {
    private double amount;

    public AmountCondition() {

    }

    public AmountCondition(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }
}
