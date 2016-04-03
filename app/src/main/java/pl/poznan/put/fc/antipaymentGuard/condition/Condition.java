package pl.poznan.put.fc.antipaymentGuard.condition;

/**
 * @author Kamil Walkowiak
 */
public abstract class Condition {
    protected long id;

    protected Condition(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean checkCondition(){
        return false;
    }
}
