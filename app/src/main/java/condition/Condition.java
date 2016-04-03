package condition;

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
