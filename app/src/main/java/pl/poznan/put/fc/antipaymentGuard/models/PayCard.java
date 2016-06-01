package pl.poznan.put.fc.antipaymentGuard.models;

import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Ignore
    private boolean isConditionFulfilled;
    @Ignore
    private int transactionsNumber;
    @Ignore
    private double transactionsAmount;
    @Ignore
    private ArrayList<PayCardTransaction> currentMonthTransactions;

    public PayCard() {

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
        this.isConditionFulfilled = false;
        this.transactionsNumber = 0;
        this.transactionsAmount = 0.0;
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
        this.isConditionFulfilled = false;
        this.transactionsNumber = 0;
        this.transactionsAmount = 0.0;
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

    public boolean isConditionFulfilled() {
        return isConditionFulfilled;
    }

    public int getTransactionsNumber() {
        return transactionsNumber;
    }

    public double getTransactionsAmount() {
        return transactionsAmount;
    }

    public Condition getCondition() {
        if(amountCondition != null) {
            return amountCondition;
        } else {
            return numberCondition;
        }
    }

    public String getConditionStatus() {
        if(amountCondition != null) {
            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            return df.format(transactionsAmount);
        } else {
            return Integer.toString(transactionsNumber);
        }
    }

    public List<PayCardTransaction> getTransactions(int month) {
        Calendar calendar = Calendar.getInstance();
        if(month == calendar.get(Calendar.MONTH)) {
            return currentMonthTransactions;
        } else {
            return PayCardTransaction.find(PayCardTransaction.class, "pay_card = ?", getId().toString());
        }
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

    public void loadCurrentMonthTransactions() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DATE, 1);
        calendar.getTimeInMillis();
        this.currentMonthTransactions = new ArrayList<>(PayCardTransaction.find(PayCardTransaction.class, "pay_card = ? and date >= ?",
                getId().toString(), Long.toString(calendar.getTimeInMillis())));
    }

    public void registerTransaction(PayCardTransaction transaction) {
        this.currentMonthTransactions.add(transaction);
        double amount = transaction.getAmount();
        if(amount < 0) {
            this.transactionsNumber++;
            this.transactionsAmount -= transaction.getAmount();
        }
        this.balance += amount;
    }

    public void removeTransaction(PayCardTransaction transaction) {
        this.currentMonthTransactions.remove(transaction);
        double amount = transaction.getAmount();
        if(amount < 0) {
            this.transactionsNumber--;
            this.transactionsAmount += transaction.getAmount();
        }
        this.balance -= amount;
    }
}
