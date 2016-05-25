package pl.poznan.put.fc.antipaymentGuard.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.PayCardsAdapter;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView rvPayCards;

    private PayCardsAdapter payCardAdapter;

    private List<PayCard> payCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        setSupportActionBar(toolbar);

        payCards = new ArrayList<>();
        payCardAdapter = new PayCardsAdapter(payCards, this);
        rvPayCards.setAdapter(payCardAdapter);
        rvPayCards.setLayoutManager(new LinearLayoutManager(this));
        rvPayCards.setHasFixedSize(true);

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
        int size = payCards.size();
        if(size != 0) {
            payCards.clear();
            payCardAdapter.notifyItemRangeRemoved(0, size);
        }
        (new FetchPayCardsTask()).execute();
    }

    private void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        rvPayCards = (RecyclerView) findViewById(R.id.rvPayCards);
    }

    private void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPayCardActivity.class));
            }
        });
    }

    private class FetchPayCardsTask extends AsyncTask<Void, Void, List<PayCard>> {
        @Override
        protected List<PayCard> doInBackground(Void... params) {
            return new Select().from(PayCard.class).execute();
        }

        @Override
        protected void onPostExecute(List<PayCard> loadedPayCards) {
            payCards.addAll(loadedPayCards);
            payCardAdapter.notifyItemRangeInserted(0, loadedPayCards.size());
            for(PayCard payCard: loadedPayCards) {
                Log.d("Condition: ", payCard.getCondition().getClass().getSimpleName());
                Toast.makeText(getApplicationContext(), payCard.getCondition().getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
