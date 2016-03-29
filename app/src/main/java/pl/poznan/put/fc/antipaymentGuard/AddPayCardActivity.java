package pl.poznan.put.fc.antipaymentGuard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPayCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addPayCardButton = (Button) findViewById(R.id.addPayCardButton);
        if (addPayCardButton != null) {
            addPayCardButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
                            String no = ((EditText) findViewById(R.id.noEditText)).getText().toString();
                            String bankName = ((EditText) findViewById(R.id.bankNameEditText)).getText().toString();
                            Float balance = Float.parseFloat(((EditText) findViewById(R.id.balanceEditText)).getText().toString());
                        }
                    }
            );
        }
    }

}
