package pl.poznan.put.fc.antipaymentGuard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.payCard.PayCard;
import pl.poznan.put.fc.antipaymentGuard.payCard.PayCardAdapter;
import pl.poznan.put.fc.antipaymentGuard.payCard.PayCardDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private ListView payCardsListView;

    private PayCardAdapter payCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        setSupportActionBar(toolbar);

        payCardAdapter = new PayCardAdapter(getApplicationContext(), new ArrayList<PayCard>());
        payCardsListView.setAdapter(payCardAdapter);

        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        (new FetchPayCardsTask()).execute();
    }

    private void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        payCardsListView = (ListView) findViewById(R.id.payCardsListView);
    }

    private void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPayCardActivity.class));
            }
        });

        payCardsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPayCardsOptionsDialog((PayCard) payCardAdapter.getItem(position));
                return true;
            }
        });
    }

    private void showPayCardsOptionsDialog(final PayCard selectedPayCard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(selectedPayCard.getName());
        builder.setItems(R.array.pay_cards_options_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 2: {
                        PayCardDatabaseHelper payCardDatabaseHelper = new PayCardDatabaseHelper(getApplicationContext());
                        if(payCardDatabaseHelper.deletePayCard(selectedPayCard.getNo())) {
                            payCardAdapter.remove(selectedPayCard);
                            Toast.makeText(getApplicationContext(), "Selected pay card has been removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Problems occurred while removing pay card", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class FetchPayCardsTask extends AsyncTask<Void, Void, List<PayCard>> {
        @Override
        protected List<PayCard> doInBackground(Void... params) {
            PayCardDatabaseHelper payCardDatabaseHelper = new PayCardDatabaseHelper(getApplicationContext());
            return payCardDatabaseHelper.getAllPayCards();
        }

        @Override
        protected void onPostExecute(List<PayCard> payCards) {
            payCardAdapter.clear();
            payCardAdapter.addAll(payCards);
        }
    }
}
