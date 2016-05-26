package pl.poznan.put.fc.antipaymentGuard.models;

import com.orm.dsl.Table;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.NumberCondition;

/**
 * @author Kamil Walkowiak
 */
@Table
public class PayCard implements Serializable {
    private Long id;
    private String name;
    private String cardNumber;
    private String bankName;
    private double balance;
    private String currencyName;
    private Date expirationDate;

    private AmountCondition amountCondition;
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

    public PayCard(String name, String cardNumber, String bankName, double balance, String currencyName, Date expirationDate, AmountCondition amountCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.currencyName = currencyName;
        this.expirationDate = expirationDate;
        this.amountCondition = amountCondition;
        this.numberCondition = null;
    }

    public PayCard(String name, String cardNumber, String bankName, double balance, String currencyName, Date expirationDate, NumberCondition numberCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.currencyName = currencyName;
        this.expirationDate = expirationDate;
        this.numberCondition = numberCondition;
        this.amountCondition = null;
    }

    public PayCard(Long id, String name, String cardNumber, String bankName, double balance, String currencyName, Date expirationDate, AmountCondition amountCondition, NumberCondition numberCondition) {
        this.id = id;
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.balance = balance;
        this.currencyName = currencyName;
        this.expirationDate = expirationDate;
        this.amountCondition = amountCondition;
        this.numberCondition = numberCondition;
    }

    public Long getId() {
        return id;
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

    public String getCurrencyName() {
        return currencyName;
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

    public List<PayCardTransaction> getTransactions() {
        return PayCardTransaction.find(PayCardTransaction.class, "pay_card = ?", getId().toString());
    }

    public String getBalanceWithCurrencyName() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        return df.format(balance) + " " + currencyName;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
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
