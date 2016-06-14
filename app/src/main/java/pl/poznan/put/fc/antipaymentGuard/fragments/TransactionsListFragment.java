package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.PayCardTransactionsAdapter;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class TransactionsListFragment extends Fragment {
    private static final String payCardArgKey = "payCard";
    private PayCard payCard;
    private List<PayCardTransaction> payCardTransactions;

    private Spinner monthSpinner;

    PayCardTransactionsAdapter payCardTransactionsAdapter;

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DATE, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        Date endDate = calendar.getTime();
        (new FetchTransactionsTask()).execute(startDate, endDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);

        final List<String> months = generateMonthsList();
        months.add("May 2016");
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
                Date startDate = null;
                try {
                    startDate = dateFormat.parse(months.get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                Date endDate = calendar.getTime();
                (new FetchTransactionsTask()).execute(startDate, endDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        payCardTransactions = new ArrayList<>();
        payCardTransactionsAdapter = new PayCardTransactionsAdapter(payCard, payCardTransactions, getContext());
        RecyclerView rvTransactions = (RecyclerView) view.findViewById(R.id.transactionsRecyclerView);
        rvTransactions.setAdapter(payCardTransactionsAdapter);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setHasFixedSize(true);

        return view;
    }

    private List<String> generateMonthsList() {
        List<String> result = new ArrayList<>();

        Calendar until = Calendar.getInstance();
        until.setTime(payCard.getCreatedAt());
        setupCalendar(until);

        Calendar calendar = Calendar.getInstance();
        setupCalendar(calendar);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

        for(; calendar.getTimeInMillis() >= until.getTimeInMillis(); calendar.add(Calendar.MONTH, -1)) {
            result.add(dateFormat.format(calendar.getTime()));
        }

        return result;
    }

    private void setupCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DATE, 1);
    }

    private class FetchTransactionsTask extends AsyncTask<Date, Void, List<PayCardTransaction>> {
        @Override
        protected List<PayCardTransaction> doInBackground(Date... params) {
            return payCard.getTransactions(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(List<PayCardTransaction> loadedTransactions) {
            int size = payCardTransactions.size();
            if(size != 0) {
                payCardTransactions.clear();
                payCardTransactionsAdapter.notifyItemRangeRemoved(0, size);
            }
            payCardTransactions.addAll(loadedTransactions);
            payCardTransactionsAdapter.notifyItemRangeInserted(0, loadedTransactions.size());
        }
    }
}
