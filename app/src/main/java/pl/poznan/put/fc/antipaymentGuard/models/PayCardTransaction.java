package pl.poznan.put.fc.antipaymentGuard.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author Kamil Walkowiak
 */
public class PayCardTransaction extends SugarRecord implements Serializable {
    private Date date;
    private Double amount;
    private String place;
    private String description;

    private PayCard payCard;

    public PayCardTransaction() {

    }

    public PayCardTransaction(Date date, Double amount, String place, String description, PayCard payCard) {
        this.date = date;
        this.amount = amount;
        this.place = place;
        this.description = description;
        this.payCard = payCard;
    }

    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }

    public PayCard getPayCard() {
        return payCard;
    }

    public String getAmountWithCurrencyName() {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        return df.format(amount) + " " + payCard.getCurrencyName();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPayCard(PayCard payCard) {
        this.payCard = payCard;
    }
}
