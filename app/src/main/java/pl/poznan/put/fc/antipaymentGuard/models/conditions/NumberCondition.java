package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import java.io.Serializable;

/**
 * @author Kamil Walkowiak
 */
public class NumberCondition  extends Condition implements Serializable {
    private int numberOfTransactions;

    public NumberCondition() {

    }

    public NumberCondition(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    @Override
    public boolean checkCondition() {
        return false;
    }
}
