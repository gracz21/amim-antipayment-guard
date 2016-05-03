package pl.poznan.put.fc.antipaymentGuard.models;

import java.util.Date;

/**
 * @author Kamil Walkowiak
 */
public class Transaction {
    private Date date;
    private Double amount;
    private String place;
    private String description;

    public Transaction(Date date, Double amount, String place, String description) {
        this.date = date;
        this.amount = amount;
        this.place = place;
        this.description = description;
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
}
