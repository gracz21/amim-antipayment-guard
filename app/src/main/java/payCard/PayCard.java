package payCard;

import java.util.ArrayList;
import java.util.List;

import transaction.Transaction;

/**
 * @author Kamil Walkowiak
 */
public class PayCard {
    private String name;
    private String no;
    private double balance;
    //private List<Transaction> transactionsHistory;

    public PayCard(String name, String no) {
        this.name = name;
        this.no = no;
        this.balance = 0.0;
        //this.transactionsHistory = new ArrayList<>();
    }


}
