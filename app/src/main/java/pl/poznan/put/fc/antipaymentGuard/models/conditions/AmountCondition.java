package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * @author Kamil Walkowiak
 */
@Table(name = "AmountConditions")
public class AmountCondition extends Condition {
    @Column(name = "Amount")
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
