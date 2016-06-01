package pl.poznan.put.fc.antipaymentGuard.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * @author Kamil Walkowiak
 */
public class PayCardTransaction extends SugarRecord implements Serializable {
    private String name;
    private long date;
    private Double amount;
    private String place;
    private String description;

    private PayCard payCard;

    public PayCardTransaction() {

    }

    public PayCardTransaction(String name, long date, Double amount, String place, String description, PayCard payCard) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.place = place;
        this.description = description;
        this.payCard = payCard;
    }

    public String getName() {
        return name;
    }

    public long getDate() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(long date) {
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
