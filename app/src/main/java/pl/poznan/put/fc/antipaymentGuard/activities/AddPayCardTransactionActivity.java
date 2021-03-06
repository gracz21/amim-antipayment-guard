package pl.poznan.put.fc.antipaymentGuard.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

public class AddPayCardTransactionActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout amountTextInputLayout;

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText placeEditText;
    private TextInputEditText descriptionEditText;

    private DateFormat dateFormat;
    private Date selectedDate;

    private PayCard payCard;
    private PayCardTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay_card_transaction);

        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        selectedDate = new Date();

        findViewsByIds();
        setupDatePickerDialog();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey("transactionId")) {
            transaction = SugarRecord.findById(PayCardTransaction.class, bundle.getLong("transactionId"));
            payCard = transaction.getPayCard();
            setupView();
        } else {
            payCard = SugarRecord.findById(PayCard.class, bundle.getLong("payCardId"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_pay_card_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            if(validate()) {
                addNewPayCardTransaction();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void findViewsByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        amountTextInputLayout = (TextInputLayout) findViewById(R.id.amountTextInputLayout);

        nameEditText = (TextInputEditText) findViewById(R.id.nameEditText);
        amountEditText = (TextInputEditText) findViewById(R.id.amountEditText);
        dateEditText = (TextInputEditText) findViewById(R.id.dateEditText);
        dateEditText.setText(dateFormat.format(selectedDate));
        placeEditText = (TextInputEditText) findViewById(R.id.placeEditText);
        descriptionEditText = (TextInputEditText) findViewById(R.id.descriptionEditText);
    }

    private void setupView() {
        nameEditText.setText(transaction.getName());
        amountEditText.setText(Double.toString(transaction.getAmount()));
        dateEditText.setText(dateFormat.format(transaction.getDate()));
        placeEditText.setText(transaction.getPlace());
        descriptionEditText.setText(transaction.getDescription());
    }

    private void setupDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                selectedDate = date.getTime();
                dateEditText.setText(dateFormat.format(selectedDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        dateEditText.setKeyListener(null);

        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    datePickerDialog.show();
                }
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        String info = getString(R.string.not_valid);

        if(nameEditText.length() == 0) {
            nameTextInputLayout.setError(info);
            valid = false;
        }
        if(amountEditText.length() == 0) {
            amountTextInputLayout.setError(info);
            valid = false;
        }

        return valid;
    }

    private void addNewPayCardTransaction() {
        String name = nameEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String place = placeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if(transaction != null) {
            payCard.getCondition().removeTransaction(transaction);
            transaction.setName(name);
            transaction.setAmount(amount);
            transaction.setPlace(place);
            transaction.setDescription(description);
            transaction.setDate(selectedDate.getTime());
            SugarRecord.save(transaction);
            payCard.getCondition().addTransaction(transaction);
        } else {
            PayCardTransaction transaction = new PayCardTransaction(name, selectedDate.getTime(), amount, place, description, payCard);
            SugarRecord.save(transaction);
            payCard.getCondition().addTransaction(transaction);
        }

        finish();
    }
}
