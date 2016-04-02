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

import java.util.ArrayList;
import java.util.List;

import payCard.PayCard;
import payCard.PayCardAdapter;
import payCard.PayCardDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private PayCardAdapter payCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPayCardActivity.class));
            }
        });
        ListView payCardsListView = (ListView) findViewById(R.id.payCardsListView);
        payCardAdapter = new PayCardAdapter(getApplicationContext(), new ArrayList<PayCard>());
        payCardsListView.setAdapter(payCardAdapter);
        payCardsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPayCardsOptionsDialog((PayCard) payCardAdapter.getItem(position));
                return true;
            }
        });
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

        /*Log.d("Insert", "Inserting ...");
        payCardDatabaseHelper.createPayCard(new PayCard("Test1", "000000"));
        payCardDatabaseHelper.createPayCard(new PayCard("Test2", "000001"));
        payCardDatabaseHelper.createPayCard(new PayCard("Test3", "000002"));*/

        (new FetchPayCardsTask()).execute();
    }

    private void showPayCardsOptionsDialog(PayCard selectedPayCard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(selectedPayCard.getName());
        builder.setItems(R.array.pay_cards_options_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class FetchPayCardsTask extends AsyncTask<Void, Void, List<PayCard>> {

        @Override
        protected List<PayCard> doInBackground(Void... params) {
            PayCardDatabaseHelper payCardDatabaseHelper = new PayCardDatabaseHelper(getApplicationContext());
            List<PayCard> payCards = payCardDatabaseHelper.getAllPayCards();
            return payCards;
        }

        @Override
        protected void onPostExecute(List<PayCard> payCards) {
            payCardAdapter.clear();
            payCardAdapter.addAll(payCards);
        }
    }
}
