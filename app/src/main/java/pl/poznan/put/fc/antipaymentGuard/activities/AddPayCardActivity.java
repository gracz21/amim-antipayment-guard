package pl.poznan.put.fc.antipaymentGuard.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.NumberCondition;

public class AddPayCardActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText nameEditText;
    private EditText noEditText;
    private EditText bankNameEditText;
    private EditText balanceEditText;
    private EditText currencyEditText;
    private EditText expirationDateEditText;
    private EditText conditionValueEditText;
    private RadioGroup conditionRadioGroup;

    private DatePickerDialog expirationDatePicker;
    private Date expirationDate;
    private DateFormat dateFormat;

    private PayCard payCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay_card);

        findViewsByIds();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey("payCardId")) {
            payCard = SugarRecord.findById(PayCard.class, bundle.getLong("payCardId"));
            setupViews();
        }

        setupDatePickerDialog();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_pay_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            addNewPayCard();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void findViewsByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        noEditText = (EditText) findViewById(R.id.amountEditText);
        bankNameEditText = (EditText) findViewById(R.id.placeEditText);
        balanceEditText = (EditText) findViewById(R.id.balanceEditText);
        currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        expirationDateEditText = (EditText) findViewById(R.id.dateEditText);
        expirationDateEditText.setKeyListener(null);
        conditionValueEditText = (EditText) findViewById(R.id.conditionValueEditText);
        conditionRadioGroup = (RadioGroup) findViewById(R.id.conditionRadioGroup);
    }

    private void setupViews() {
        nameEditText.setText(payCard.getName());
        noEditText.setText(payCard.getCardNumber());
        bankNameEditText.setText(payCard.getBankName());
        balanceEditText.setText(Double.toString(payCard.getBalance()));
        currencyEditText.setText(payCard.getCurrencyName());
        expirationDateEditText.setText(dateFormat.format(payCard.getExpirationDate()));
        Condition condition = payCard.getCondition();
        if(condition.getClass() == AmountCondition.class) {
            conditionRadioGroup.check(R.id.amountConditionRadioButton);
            conditionValueEditText.setText(Double.toString(((AmountCondition)condition).getConditionValue()));
        } else {
            conditionRadioGroup.check(R.id.numberConditionRadioButton);
            conditionValueEditText.setText(Integer.toString(((NumberCondition)condition).getConditionValue()));
        }
    }

    private void setupDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        expirationDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                expirationDate = date.getTime();
                expirationDateEditText.setText(dateFormat.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        expirationDatePicker.getDatePicker().setMinDate(new Date().getTime() - 1000);
    }

    private void setListeners() {
        expirationDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    expirationDatePicker.show();
                }
            }
        });

        expirationDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expirationDatePicker.show();
            }
        });
    }

    private void addNewPayCard() {
        String name = nameEditText.getText().toString();
        String no = noEditText.getText().toString();
        String bankName = bankNameEditText.getText().toString();
        Double balance = Double.parseDouble(balanceEditText.getText().toString());
        String currency = currencyEditText.getText().toString();
        String conditionValue = conditionValueEditText.getText().toString();

        if(payCard != null) {
            payCard.setName(name);
            payCard.setCardNumber(no);
            payCard.setBankName(bankName);
            payCard.setBalance(balance);
            payCard.setCurrencyName(currency);
            Condition condition = payCard.getCondition();
            if(conditionRadioGroup.getCheckedRadioButtonId() == R.id.amountConditionRadioButton) {
                Double conditionAmount = Double.parseDouble(conditionValue);
                if (condition.getClass() == AmountCondition.class) {
                    ((AmountCondition)condition).setConditionValue(conditionAmount);
                    ((AmountCondition)condition).save();
                } else {
                    ((AmountCondition)condition).delete();
                    condition = new AmountCondition(conditionAmount);
                    ((AmountCondition)condition).save();
                    payCard.setAmountCondition((AmountCondition)condition);
                }
            } else {
                int conditionNumber = Integer.parseInt(conditionValue);
                if (condition.getClass() == NumberCondition.class) {
                    ((NumberCondition)condition).setConditionValue(conditionNumber);
                    ((NumberCondition)condition).save();
                } else {
                    ((NumberCondition)condition).delete();
                    condition = new NumberCondition(conditionNumber);
                    ((NumberCondition)condition).save();
                    payCard.setNumberCondition((NumberCondition)condition);
                }
            }

            SugarRecord.save(payCard);
        } else {
            if (conditionRadioGroup.getCheckedRadioButtonId() == R.id.amountConditionRadioButton) {
                Double conditionAmount = Double.parseDouble(conditionValue);
                AmountCondition condition = new AmountCondition(conditionAmount);
                condition.save();
                PayCard payCard = new PayCard(name, no, bankName, balance, currency, expirationDate, condition);
                SugarRecord.save(payCard);
            } else {
                int conditionNumber = Integer.parseInt(conditionValue);
                NumberCondition condition = new NumberCondition(conditionNumber);
                condition.save();
                PayCard payCard = new PayCard(name, no, bankName, balance, currency, expirationDate, condition);
                SugarRecord.save(payCard);
            }
            Toast.makeText(getApplicationContext(), R.string.pay_card_created, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
