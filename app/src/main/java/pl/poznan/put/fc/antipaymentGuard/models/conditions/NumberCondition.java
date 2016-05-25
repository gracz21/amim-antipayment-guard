package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * @author Kamil Walkowiak
 */
@Table(name = "NumberConditions")
public class NumberCondition  extends Condition implements Serializable {
    @Column(name = "NumberOfTransactions")
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
