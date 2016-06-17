package pl.poznan.put.fc.antipaymentGuard.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
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
    private String currencyName;
    private Date expirationDate;
    private Date createdAt;

    private AmountCondition amountCondition;
    private NumberCondition numberCondition;

    public PayCard() {

    }

    public PayCard(String name, String cardNumber, String bankName, String currencyName, Date expirationDate, AmountCondition amountCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.currencyName = currencyName;
        this.expirationDate = expirationDate;
        this.amountCondition = amountCondition;
        this.numberCondition = null;
        this.createdAt = new Date();
    }

    public PayCard(String name, String cardNumber, String bankName, String currencyName, Date expirationDate, NumberCondition numberCondition) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.currencyName = currencyName;
        this.expirationDate = expirationDate;
        this.numberCondition = numberCondition;
        this.amountCondition = null;
        this.createdAt = new Date();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Condition getCondition() {
        if(amountCondition != null) {
            return amountCondition;
        } else {
            return numberCondition;
        }
    }

    public List<PayCardTransaction> getTransactionsFromMonth(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date endDate = calendar.getTime();
        return SugarRecord.find(PayCardTransaction.class, "pay_card = ? and date >= ? and date <= ?",
                id.toString(),
                Long.toString(startDate.getTime()),
                Long.toString(endDate.getTime()));
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void addTransaction(PayCardTransaction transaction) {
        SugarRecord.save(this);
        if(amountCondition != null) {
            amountCondition.addTransaction(transaction);
        } else {
            numberCondition.addTransaction(transaction);
        }
    }

    public void removeTransaction(PayCardTransaction transaction) {
        SugarRecord.save(this);
        if(amountCondition != null) {
            amountCondition.removeTransaction(transaction);
        } else {
            numberCondition.removeTransaction(transaction);
        }
    }
}
