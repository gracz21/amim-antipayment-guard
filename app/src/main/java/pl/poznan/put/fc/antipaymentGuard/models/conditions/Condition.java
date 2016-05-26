package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.orm.SugarRecord;

/**
 * @author Kamil Walkowiak
 */
public abstract class Condition extends SugarRecord {
    public Condition() {
    }

    public abstract boolean checkCondition();
}
