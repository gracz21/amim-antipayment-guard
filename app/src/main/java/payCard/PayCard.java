package payCard;

import java.util.Date;

/**
 * @author Kamil Walkowiak
 */
public class PayCard {
    private int id;
    private String name;
    private String no;
    private String bankName;
    private double balance;
    private Date expirationDate;
    //private List<Transaction> transactionsHistory;


    public PayCard(String name, String no, String bankName, double balance, Date expirationDate) {
        this.name = name;
        this.no = no;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
    }

    public PayCard(int id, String name, String no, String bankName, double balance, Date expirationDate) {
        this(name, no, bankName, balance, expirationDate);
        this.id = id;
    }

    public int getId() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
