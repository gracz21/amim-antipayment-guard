package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.orm.SugarRecord;

import java.text.DecimalFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class AmountCondition extends SugarRecord implements Condition {
    private double conditionValue;
    private double conditionStatus;

    public AmountCondition() {

    }

    public AmountCondition(double conditionValue) {
        this.conditionValue = conditionValue;
        this.conditionStatus = 0;
    }

    public double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public double getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(double conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    @Override
    public void calculateConditionStatus(List<PayCardTransaction> transactions) {
        conditionStatus = 0;
        for(PayCardTransaction transaction: transactions) {
            if(transaction.getAmount() < 0) {
                conditionStatus += transaction.getAmount();
            }
        }
        this.save();
    }

    @Override
    public void addTransaction(PayCardTransaction transaction) {
        if(transaction.getAmount() < 0) {
            conditionStatus += transaction.getAmount();
        }
        this.save();
    }

    @Override
    public void removeTransaction(PayCardTransaction transaction) {
        if(transaction.getAmount() < 0) {
            conditionStatus -= transaction.getAmount();
        }
        this.save();
    }

    @Override
    public String getStatusString() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        return df.format(conditionStatus) + "/" + df.format(conditionValue);
    }

    @Override
    public boolean checkCondition() {
        return false;
    }

    @Override
    public void resetCondition() {
        conditionStatus = 0;
    }
}
