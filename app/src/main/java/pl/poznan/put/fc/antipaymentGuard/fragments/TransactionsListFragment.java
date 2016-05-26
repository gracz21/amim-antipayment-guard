package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.TransactionsAdapter;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class TransactionsListFragment extends Fragment {
    private static final String transactionsListArgKey = "transactionsList";
    private ArrayList<PayCardTransaction> payCardTransactions;

    public static TransactionsListFragment newInstance(ArrayList<PayCardTransaction> payCardTransactions) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(transactionsListArgKey, payCardTransactions);
        TransactionsListFragment transactionsListFragment = new TransactionsListFragment();
        transactionsListFragment.setArguments(bundle);
        return transactionsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payCardTransactions = (ArrayList<PayCardTransaction>) getArguments().getSerializable(transactionsListArgKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);

        TransactionsAdapter adapter = new TransactionsAdapter(payCardTransactions, getContext());
        RecyclerView rvTransactions = (RecyclerView) view.findViewById(R.id.transactionsRecyclerView);
        rvTransactions.setAdapter(adapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setHasFixedSize(true);

        return view;
    }
}
