package pl.poznan.put.fc.antipaymentGuard.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.NumberCondition;

/**
 * @author Kamil Walkowiak
 */
@Table(name = "PayCards")
public class PayCard extends Model {
    @Column(name = "Name")
    private String name;
    @Column(name = "CardNumber")
    private String cardNumber;
    @Column(name = "BankName")
    private String bankName;
    @Column(name = "Balance")
    private double balance;
    @Column(name = "ExpirationDate")
    private Date expirationDate;
    @Column(name="Condition", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private AmountCondition amountCondition;
    @Column(name = "NumberCondition", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private NumberCondition numberCondition;

    public PayCard() {

    }

    public PayCard(String name, String cardNumber, String bankName, double balance, Date expirationDate) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
    }

    public PayCard(String name, String cardNumber, String bankName, double balance, Date expirationDate, AmountCondition amountCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
        this.amountCondition = amountCondition;
        this.numberCondition = null;
    }

    public PayCard(String name, String cardNumber, String bankName, double balance, Date expirationDate, NumberCondition numberCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.expirationDate = expirationDate;
        this.numberCondition = numberCondition;
        this.amountCondition = null;
    }

    public String getName() {
        return name;
    }

    public String getCardNumber() {
        return cardNumber;
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

    public AmountCondition getAmountCondition() {
        return amountCondition;
    }

    public NumberCondition getNumberCondition() {
        return numberCondition;
    }

    public Condition getCondition() {
        if(amountCondition != null) {
            return amountCondition;
        } else {
            return numberCondition;
        }
    }

    public List<Transaction> getTransactions() {
        return getMany(Transaction.class, "PayCard");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setAmountCondition(AmountCondition amountCondition) {
        this.amountCondition = amountCondition;
        this.numberCondition = null;
    }

    public void setNumberCondition(NumberCondition numberCondition) {
        this.numberCondition = numberCondition;
        this.amountCondition = null;
    }
}
