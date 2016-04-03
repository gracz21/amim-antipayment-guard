package pl.poznan.put.fc.antipaymentGuard.condition;

/**
 * @author Kamil Walkowiak
 */
public class NumberCondition extends Condition {
    private int numberOfTransactions;

    public NumberCondition(long id, int numberOfTransactions) {
        super(id);
        this.numberOfTransactions = numberOfTransactions;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }
}
