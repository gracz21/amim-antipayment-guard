package payCard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import transaction.Transaction;

/**
 * @author Kamil Walkowiak
 */
public class PayCard {
    private String name;
    private String no;
    private String bankName;
    private float balance;
    private Date expirationDate;
    //private List<Transaction> transactionsHistory;

    public PayCard() {}

    public PayCard(String name, String no, String bankName, float balance, Date expirationDate) {
        this.name = name;
        this.no = no;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }

    public float getBalance() {
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
