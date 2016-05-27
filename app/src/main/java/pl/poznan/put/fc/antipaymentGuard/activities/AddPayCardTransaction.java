package pl.poznan.put.fc.antipaymentGuard.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

public class AddPayCardTransaction extends AppCompatActivity {
    private Toolbar toolbar;

    private TextInputEditText nameEditText;
    private TextInputEditText amountEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText placeEditText;
    private TextInputEditText descriptionEditText;

    private Date selectedDate;
    private PayCard payCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay_card_transaction);

        findViewsByIds();
        setupDatePickerDialog();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        payCard = (PayCard) getIntent().getSerializableExtra("payCard");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_pay_card_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            addNewPayCardTransaction();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void findViewsByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        nameEditText = (TextInputEditText) findViewById(R.id.nameEditText);
        amountEditText = (TextInputEditText) findViewById(R.id.amountEditText);
        dateEditText = (TextInputEditText) findViewById(R.id.dateEditText);
        placeEditText = (TextInputEditText) findViewById(R.id.placeEditText);
        descriptionEditText = (TextInputEditText) findViewById(R.id.descriptionEditText);
    }

    private void setupDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                selectedDate = date.getTime();
                dateEditText.setText(dateFormat.format(selectedDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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

    private void addNewPayCardTransaction() {
        String name = nameEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String place = placeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        PayCardTransaction transaction = new PayCardTransaction(name, selectedDate, amount, place, description, payCard);
        transaction.save();

        finish();
    }
}
