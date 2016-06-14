package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import java.io.Serializable;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public interface Condition extends Serializable {
    void calculateConditionStatus(List<PayCardTransaction> transactions);
    void addTransaction(PayCardTransaction transaction);
    void removeTransaction(PayCardTransaction transaction);
    String getStatusString();
    boolean checkCondition();
    void resetCondition();
}
