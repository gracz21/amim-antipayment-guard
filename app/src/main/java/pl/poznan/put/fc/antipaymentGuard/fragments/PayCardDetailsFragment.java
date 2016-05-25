package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

/**
 * @author Kamil Walkowiak
 */
public class PayCardDetailsFragment extends Fragment {
    private static final String payCardArgKey = "payCard";
    private PayCard payCard;
    private Context context;

    public static PayCardDetailsFragment newInstance(PayCard payCard) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(payCardArgKey, payCard);
        PayCardDetailsFragment bankAccountDetailsFragment = new PayCardDetailsFragment();
        bankAccountDetailsFragment.setArguments(bundle);
        return bankAccountDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payCard = (PayCard) getArguments().get(payCardArgKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO
        //String balance = payCard.getBalanceWithCurrencyName();

        View view = inflater.inflate(R.layout.fragment_pay_card_details, container, false);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(view.getContext().getApplicationContext());
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        ((TextView) view.findViewById(R.id.nameTextView)).setText(payCard.getName());
        ((TextView) view.findViewById(R.id.balanceTextView)).setText(df.format(payCard.getBalance()));
        ((TextView) view.findViewById(R.id.noTextView)).setText(payCard.getCardNumber());
        ((TextView) view.findViewById(R.id.bankNameTextView)).setText(payCard.getBankName());
        ((TextView) view.findViewById(R.id.expirationDateTextView)).setText(dateFormat.format(payCard.getExpirationDate()));
        return view;
    }
}
