package pl.poznan.put.fc.antipaymentGuard.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.NumberCondition;
import pl.poznan.put.fc.antipaymentGuard.utils.CurrentMonthStartDateUtil;

public class AddPayCardActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextInputLayout nameTextInputLayout;
    private TextInputLayout noTextInputLayout;
    private TextInputLayout currencyNameTextInputLayout;
    private TextInputLayout conditionValueTextInputLayout;

    private EditText nameEditText;
    private EditText noEditText;
    private EditText bankNameEditText;
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
            if(validate()) {
                addNewPayCard();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void findViewsByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        nameTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        noTextInputLayout = (TextInputLayout) findViewById(R.id.noTextInputLayout);
        currencyNameTextInputLayout = (TextInputLayout) findViewById(R.id.currencyTextInputLayout);
        conditionValueTextInputLayout = (TextInputLayout) findViewById(R.id.conditionValueTextInputLayout);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        noEditText = (EditText) findViewById(R.id.noTextInputEditText);
        bankNameEditText = (EditText) findViewById(R.id.placeEditText);
        currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        expirationDateEditText = (EditText) findViewById(R.id.dateEditText);
        expirationDateEditText.setKeyListener(null);
        conditionValueEditText = (EditText) findViewById(R.id.conditionValueEditText);
        conditionRadioGroup = (RadioGroup) findViewById(R.id.conditionRadioGroup);

        conditionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                conditionValueEditText.setText("");
                if(checkedId == R.id.amountConditionRadioButton) {
                    conditionValueEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                } else {
                    conditionValueEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });
    }

    private void setupViews() {
        nameEditText.setText(payCard.getName());
        noEditText.setText(payCard.getCardNumber());
        bankNameEditText.setText(payCard.getBankName());
        currencyEditText.setText(payCard.getCurrencyName());
        if(payCard.getExpirationDate() != null) {
            expirationDateEditText.setText(dateFormat.format(payCard.getExpirationDate()));
        }
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

    private boolean validate() {
        boolean valid = true;
        String info = getString(R.string.not_valid);
        if(nameEditText.length() == 0) {
            nameTextInputLayout.setError(info);
            valid = false;
        }
        if(noEditText.length() == 0) {
            noTextInputLayout.setError(info);
            valid = false;
        }
        if(currencyEditText.length() == 0) {
            currencyNameTextInputLayout.setError(info);
            valid = false;
        }
        if(conditionValueEditText.length() == 0) {
            conditionValueTextInputLayout.setError(info);
            valid = false;
        }

        return valid;
    }

    private void addNewPayCard() {
        String name = nameEditText.getText().toString();
        String no = noEditText.getText().toString();
        String bankName = bankNameEditText.getText().toString();
        String currency = currencyEditText.getText().toString();
        String conditionValue = conditionValueEditText.getText().toString();

        if(payCard != null) {
            payCard.setName(name);
            payCard.setCardNumber(no);
            payCard.setBankName(bankName);
            payCard.setCurrencyName(currency);
            Condition condition = payCard.getCondition();
            if(conditionRadioGroup.getCheckedRadioButtonId() == R.id.amountConditionRadioButton) {
                Double conditionAmount = Double.parseDouble(conditionValue);
                if (condition.getClass() == AmountCondition.class) {
                    ((AmountCondition)condition).setConditionValue(conditionAmount);
                    ((AmountCondition)condition).save();
                } else {
                    ((NumberCondition)condition).delete();
                    condition = new AmountCondition(conditionAmount);
                    ((AmountCondition)condition).save();
                    payCard.setAmountCondition((AmountCondition)condition);
                    List<PayCardTransaction> transactions = payCard.
                            getTransactionsFromMonth(CurrentMonthStartDateUtil.getCurrentMonthStartDate());
                    condition.calculateConditionStatus(transactions);
                }
            } else {
                int conditionNumber = Integer.parseInt(conditionValue);
                if (condition.getClass() == NumberCondition.class) {
                    ((NumberCondition)condition).setConditionValue(conditionNumber);
                    ((NumberCondition)condition).save();
                } else {
                    ((AmountCondition)condition).delete();
                    condition = new NumberCondition(conditionNumber);
                    ((NumberCondition)condition).save();
                    payCard.setNumberCondition((NumberCondition)condition);
                    List<PayCardTransaction> transactions = payCard.
                            getTransactionsFromMonth(CurrentMonthStartDateUtil.getCurrentMonthStartDate());
                    condition.calculateConditionStatus(transactions);
                }
            }

            SugarRecord.save(payCard);
        } else {
            if (conditionRadioGroup.getCheckedRadioButtonId() == R.id.amountConditionRadioButton) {
                Double conditionAmount = Double.parseDouble(conditionValue);
                AmountCondition condition = new AmountCondition(conditionAmount);
                condition.save();
                PayCard payCard = new PayCard(name, no, bankName, currency, expirationDate, condition);
                SugarRecord.save(payCard);
            } else {
                int conditionNumber = Integer.parseInt(conditionValue);
                NumberCondition condition = new NumberCondition(conditionNumber);
                condition.save();
                PayCard payCard = new PayCard(name, no, bankName, currency, expirationDate, condition);
                SugarRecord.save(payCard);
            }
            Toast.makeText(getApplicationContext(), R.string.pay_card_created, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
