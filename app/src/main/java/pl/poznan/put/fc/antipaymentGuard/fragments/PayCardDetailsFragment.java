package pl.poznan.put.fc.antipaymentGuard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;

/**
 * @author Kamil Walkowiak
 */
public class PayCardDetailsFragment extends Fragment {
    private static final String payCardArgKey = "payCard";
    private PayCard payCard;

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
        View view = inflater.inflate(R.layout.fragment_pay_card_details, container, false);
        Context context = view.getContext();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());

        TextView conditionTypeTextView = (TextView) view.findViewById(R.id.conditionTypeTextView);
        if(payCard.getCondition().getClass() == AmountCondition.class) {
            conditionTypeTextView.setText(context.getString(R.string.condition_transactions_amount));
        } else {
            conditionTypeTextView.setText(context.getString(R.string.condition_transactions_number));
        }

        TextView conditionStatusTextView = (TextView) view.findViewById(R.id.conditionStatusTextView);
        ImageView conditionStatusIcon = (ImageView) view.findViewById(R.id.conditionStatusIconImageView);
        String status = payCard.getConditionStatus() + "/" + payCard.getCondition().toString();
        if(payCard.getCondition().getClass() == AmountCondition.class) {
            status += " " + payCard.getCurrencyName();
        } else {
            status += " " + context.getString(R.string.transactions);
        }
        if(payCard.isConditionFulfilled()) {
            conditionStatusIcon.setImageResource(R.drawable.ic_condition_fulfilled_24dp);
            conditionStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
            status = context.getString(R.string.condition_fulfilled);
        } else {
            conditionStatusIcon.setImageResource(R.drawable.ic_condition_not_fulfilled_24dp);
            conditionStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        conditionStatusTextView.setText(status);

        ((TextView) view.findViewById(R.id.nameTextView)).setText(payCard.getName());
        ((TextView) view.findViewById(R.id.balanceTextView)).setText(payCard.getBalanceWithCurrencyName());
        ((TextView) view.findViewById(R.id.descriptionTextView)).setText(payCard.getCardNumber());
        ((TextView) view.findViewById(R.id.bankNameTextView)).setText(payCard.getBankName());
        ((TextView) view.findViewById(R.id.expirationDateTextView)).setText(dateFormat.format(payCard.getExpirationDate()));
        return view;
    }
}
