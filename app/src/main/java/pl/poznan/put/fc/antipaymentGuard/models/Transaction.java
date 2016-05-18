package pl.poznan.put.fc.antipaymentGuard.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * @author Kamil Walkowiak
 */
@Table(name = "Transactions")
public class Transaction extends Model {
    @Column(name = "Date")
    private Date date;
    @Column(name = "Amount")
    private Double amount;
    @Column(name = "Place")
    private String place;
    @Column(name = "Description")
    private String description;
    @Column(name = "PayCard", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private PayCard payCard;

    public Transaction() {

    }

    public Transaction(Date date, Double amount, String place, String description, PayCard payCard) {
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
