package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import java.io.Serializable;

/**
 * @author Kamil Walkowiak
 */
public class NumberCondition extends Condition implements Serializable {
    private int transactionsNumber;

    public NumberCondition() {

    }

    public NumberCondition(int transactionsNumber) {
        this.transactionsNumber = transactionsNumber;
    }

    public int getTransactionsNumber() {
        return transactionsNumber;
    }

    public void setTransactionsNumber(int transactionsNumber) {
        this.transactionsNumber = transactionsNumber;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(transactionsNumber);
    }
}
