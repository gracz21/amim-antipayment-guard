package pl.poznan.put.fc.antipaymentGuard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.poznan.put.fc.antipaymentGuard.condition.Condition;
import pl.poznan.put.fc.antipaymentGuard.condition.ConditionDatabaseHelper;
import pl.poznan.put.fc.antipaymentGuard.payCard.PayCard;
import pl.poznan.put.fc.antipaymentGuard.payCard.PayCardDatabaseHelper;

public class AddPayCardActivity extends AppCompatActivity {
    private final String LOG_TAG = AddPayCardActivity.class.getSimpleName();

    private Toolbar toolbar;

    private Button addPayCardButton;
    private EditText nameEditText;
    private EditText noEditText;
    private EditText bankNameEditText;
    private EditText balanceEditText;
    private EditText expirationDateEditText;
    private EditText conditionValueEditText;
    private Spinner conditionTypeSpinner;

    private DatePickerDialog expirationDatePicker;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay_card);

        findViewsByIds();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        setupDatePickerDialog();
        setListeners();
    }

    private void findViewsByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        addPayCardButton = (Button) findViewById(R.id.addPayCardButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        noEditText = (EditText) findViewById(R.id.noEditText);
        bankNameEditText = (EditText) findViewById(R.id.bankNameEditText);
        balanceEditText = (EditText) findViewById(R.id.balanceEditText);
        expirationDateEditText = (EditText) findViewById(R.id.expirationDateEditText);
        conditionValueEditText = (EditText) findViewById(R.id.conditionValueEditText);
        conditionTypeSpinner = (Spinner) findViewById(R.id.conditionTypeSpinner);
    }

    private void setupDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        expirationDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                expirationDateEditText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setListeners() {
        addPayCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPayCard();
            }
        });

        expirationDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    expirationDatePicker.show();
                }
                v.clearFocus();
            }
        });
    }

    private void addNewPayCard() {
        String name = nameEditText.getText().toString();
        String no = noEditText.getText().toString();
        String bankName = bankNameEditText.getText().toString();
        Float balance = Float.parseFloat(balanceEditText.getText().toString());
        Date expirationDate = null;
        try {
            if(!expirationDateEditText.getText().toString().isEmpty()) {
                expirationDate = dateFormatter.parse(expirationDateEditText.getText().toString());
            }

            String conditionValue = conditionValueEditText.getText().toString();
            ConditionDatabaseHelper conditionDatabaseHelper = new ConditionDatabaseHelper(getApplicationContext());
            Condition condition;
            if((conditionTypeSpinner.getSelectedItem()).equals(getString(R.string.condition_transactions_amount))) {
                Double conditionAmount = Double.parseDouble(conditionValue);
                condition = conditionDatabaseHelper.getOrCreateAmountCondition(conditionAmount);
            } else {
                int conditionNumber = Integer.parseInt(conditionValue);
                condition = conditionDatabaseHelper.getOrCreateNumberCondition(conditionNumber);
            }

            PayCard payCard = new PayCard(name, no, bankName, balance, expirationDate, condition);
            PayCardDatabaseHelper dbHelper = new PayCardDatabaseHelper(getApplicationContext());
            dbHelper.createPayCard(payCard);
            Toast.makeText(getApplicationContext(), "New pay card has been added", Toast.LENGTH_SHORT).show();
            finish();
        } catch (ParseException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }
}
