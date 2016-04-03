package pl.poznan.put.fc.antipaymentGuard.payCard;

import java.util.Date;

import pl.poznan.put.fc.antipaymentGuard.condition.Condition;

/**
 * @author Kamil Walkowiak
 */
public class PayCard {
    private long id;
    private String name;
    private String no;
    private String bankName;
    private double balance;
    private Date expirationDate;
    private Condition condition;
    //private List<Transaction> transactionsHistory;

    public PayCard(String name, String no, String bankName, double balance, Date expirationDate) {
        this.name = name;
        this.no = no;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
    }


    public PayCard(String name, String no, String bankName, double balance, Date expirationDate, Condition condition) {
        this.name = name;
        this.no = no;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
        this.condition = condition;
    }

    public PayCard(long id, String name, String no, String bankName, double balance, Date expirationDate, Condition condition) {
        this(name, no, bankName, balance, expirationDate, condition);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }

    public double getBalance() {
        return balance;
    }

    public String getBankName() {
        return bankName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Condition getCondition() {
        return condition;
    }
}
