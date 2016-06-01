package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.TransactionsAdapter;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class TransactionsListFragment extends Fragment {
    private static final String payCardArgKey = "payCard";
    private PayCard payCard;
    private List<PayCardTransaction> payCardTransactions;

    TransactionsAdapter transactionsAdapter;

    public static TransactionsListFragment newInstance(PayCard payCard) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(payCardArgKey, payCard);
        TransactionsListFragment transactionsListFragment = new TransactionsListFragment();
        transactionsListFragment.setArguments(bundle);
        return transactionsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payCard = (PayCard) getArguments().getSerializable(payCardArgKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        (new FetchTransactionsTask()).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);

        payCardTransactions = new ArrayList<>();
        transactionsAdapter = new TransactionsAdapter(payCardTransactions, getContext());
        RecyclerView rvTransactions = (RecyclerView) view.findViewById(R.id.transactionsRecyclerView);
        rvTransactions.setAdapter(transactionsAdapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setHasFixedSize(true);

        return view;
    }

    private class FetchTransactionsTask extends AsyncTask<Void, Void, List<PayCardTransaction>> {
        @Override
        protected List<PayCardTransaction> doInBackground(Void... params) {
            return payCard.getTransactions(5);
        }

        @Override
        protected void onPostExecute(List<PayCardTransaction> loadedTransactions) {
            int size = payCardTransactions.size();
            if(size != 0) {
                payCardTransactions.clear();
                transactionsAdapter.notifyItemRangeRemoved(0, size);
            }
            payCardTransactions.addAll(loadedTransactions);
            transactionsAdapter.notifyItemRangeInserted(0, loadedTransactions.size());
//            for(PayCard payCard: loadedTransactions) {
//                Log.d("Condition: ", payCard.getCondition().getClass().getSimpleName());
//                Toast.makeText(getApplicationContext(), payCard.getCondition().getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
