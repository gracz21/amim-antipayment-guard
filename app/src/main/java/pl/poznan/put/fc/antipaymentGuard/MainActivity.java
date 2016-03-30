package pl.poznan.put.fc.antipaymentGuard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    private class FetchPayCardsTask extends AsyncTask<Void, Void, List<PayCard>> {

        @Override
        protected List<PayCard> doInBackground(Void... params) {
            PayCardDatabaseHelper payCardDatabaseHelper = new PayCardDatabaseHelper(getApplicationContext());
            List<PayCard> payCards = payCardDatabaseHelper.getAllPayCards();
            payCardDatabaseHelper.close();
            return payCards;
        }

        @Override
        protected void onPostExecute(List<PayCard> payCards) {
            payCardAdapter.clear();
            payCardAdapter.addAll(payCards);
        }
    }
}
