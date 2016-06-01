package pl.poznan.put.fc.antipaymentGuard.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orm.query.Select;

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            List<PayCard> result = Select.from(PayCard.class).list();
            for(PayCard payCard: result) {
                payCard.loadCurrentMonthTransactions();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<PayCard> loadedPayCards) {
            int size = payCards.size();
            if(size != 0) {
                payCards.clear();
                payCardAdapter.notifyItemRangeRemoved(0, size);
            }
            payCards.addAll(loadedPayCards);
            payCardAdapter.notifyItemRangeInserted(0, loadedPayCards.size());
//            for(PayCard payCard: loadedPayCards) {
//                Log.d("Condition: ", payCard.getCondition().getClass().getSimpleName());
//                Toast.makeText(getApplicationContext(), payCard.getCondition().getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
