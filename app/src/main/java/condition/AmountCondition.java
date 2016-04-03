package condition;

/**
 * @author Kamil Walkowiak
 */
public class AmountCondition extends Condition {
    private double amount;

    public AmountCondition(int id, double amount) {
        super(id);
        this.amount = amount;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }
}
