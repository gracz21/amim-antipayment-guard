package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.orm.SugarRecord;

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
import pl.poznan.put.fc.antipaymentGuard.utils.CurrentMonthStartDateUtil;

/**
 * @author Kamil Walkowiak
 */
public class TransactionsListFragment extends Fragment {
    private static final String payCardArgKey = "payCardId";
    private PayCard payCard;
    private long payCardId;
    private List<PayCardTransaction> payCardTransactions;

    private SimpleDateFormat monthFormatter;
    private Spinner monthSpinner;

    PayCardTransactionsAdapter payCardTransactionsAdapter;

    public static TransactionsListFragment newInstance(long payCardId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(payCardArgKey, payCardId);
        TransactionsListFragment transactionsListFragment = new TransactionsListFragment();
        transactionsListFragment.setArguments(bundle);
        return transactionsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monthFormatter = new SimpleDateFormat("MMM yyyy");
        payCardId = getArguments().getLong(payCardArgKey);
        payCard = SugarRecord.findById(PayCard.class, payCardId);
    }

    @Override
    public void onResume() {
        super.onResume();

        payCard = SugarRecord.findById(PayCard.class, payCardId);
        Date startDate = CurrentMonthStartDateUtil.getCurrentMonthStartDate();
        (new FetchTransactionsTask()).execute(startDate);
        Log.d("TMP", "Debug");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);

        final List<String> months = generateMonthsList();
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Date startDate = null;
                try {
                    startDate = monthFormatter.parse(months.get(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                (new FetchTransactionsTask()).execute(startDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        payCardTransactions = new ArrayList<>();
        payCardTransactionsAdapter = new PayCardTransactionsAdapter(payCard, payCardTransactions, getContext());
        RecyclerView rvTransactions = (RecyclerView) view.findViewById(R.id.transactionsRecyclerView);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTransactions.setAdapter(payCardTransactionsAdapter);
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

        for(; calendar.getTimeInMillis() >= until.getTimeInMillis(); calendar.add(Calendar.MONTH, -1)) {
            result.add(monthFormatter.format(calendar.getTime()));
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
            return payCard.getTransactionsFromMonth(params[0]);
        }

        @Override
        protected void onPostExecute(List<PayCardTransaction> loadedTransactions) {
            int size = payCardTransactions.size();
            if(size != 0) {
                payCardTransactions.clear();
                payCardTransactionsAdapter.notifyDataSetChanged();
            }
            payCardTransactions.addAll(loadedTransactions);
            payCardTransactionsAdapter.notifyDataSetChanged();
        }
    }
}
