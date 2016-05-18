package pl.poznan.put.fc.antipaymentGuard.models.conditions;

import com.activeandroid.Model;

/**
 * @author Kamil Walkowiak
 */
public abstract class Condition extends Model {
    public Condition() {
    }

    public abstract boolean checkCondition();
}
