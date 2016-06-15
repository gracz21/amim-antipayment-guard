package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.orm.SugarRecord;

import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class NumberCondition extends SugarRecord implements Condition {
    private int conditionValue;
    private int conditionStatus;

    public NumberCondition() {

    }

    public NumberCondition(int conditionValue) {
        this.conditionValue = conditionValue;
        this.conditionStatus = 0;
    }

    public int getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(int conditionValue) {
        this.conditionValue = conditionValue;
    }

    public int getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(int conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    @Override
    public void calculateConditionStatus(List<PayCardTransaction> transactions) {
        conditionStatus = 0;
        for(PayCardTransaction transaction: transactions) {
            if(transaction.getAmount() < 0) {
                conditionStatus++;
            }
        }
        this.save();
    }

    @Override
    public void addTransaction(PayCardTransaction transaction) {
        if(transaction.getAmount() < 0) {
            conditionStatus++;
        }
        this.save();
    }

    @Override
    public void removeTransaction(PayCardTransaction transaction) {
        if(transaction.getAmount() < 0) {
            conditionStatus--;
        }
        this.save();
    }

    @Override
    public String getStatusString() {
        return Integer.toString(conditionStatus) + "/" + Integer.toString(conditionValue);
    }

    @Override
    public boolean checkCondition() {
        return conditionValue <= conditionStatus;
    }

    @Override
    public void resetCondition() {
        conditionStatus = 0;
        this.save();
    }
}
