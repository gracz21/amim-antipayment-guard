package pl.poznan.put.fc.antipaymentGuard.condition;

/**
 * @author Kamil Walkowiak
 */
public abstract class Condition {
    protected int id;

    protected Condition(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean checkCondition(){
        return false;
    }
}
