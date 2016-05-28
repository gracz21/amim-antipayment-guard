package pl.poznan.put.fc.antipaymentGuard.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.DateFormat;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

public class PayCardTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PayCardTransaction transaction = (PayCardTransaction) getIntent().getSerializableExtra("transaction");
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        ((TextView) findViewById(R.id.nameTextView)).setText(transaction.getName());
        ((TextView) findViewById(R.id.payCardTextView)).setText(transaction.getPayCard().getName());
        ((TextView) findViewById(R.id.amountTextView)).setText(transaction.getAmountWithCurrencyName());
        ((TextView) findViewById(R.id.dateTextView)).setText(dateFormat.format(transaction.getDate()));
        ((TextView) findViewById(R.id.placeTextView)).setText(transaction.getPlace());
        ((TextView) findViewById(R.id.descriptionTextView)).setText(transaction.getDescription());
    }

}
