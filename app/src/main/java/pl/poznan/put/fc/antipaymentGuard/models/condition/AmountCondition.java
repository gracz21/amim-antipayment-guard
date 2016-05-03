package pl.poznan.put.fc.antipaymentGuard.models.condition;

/**
 * @author Kamil Walkowiak
 */
public class AmountCondition extends Condition {
    private double amount;

    public AmountCondition(long id, double amount) {
        super(id);
        this.amount = amount;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }
}
