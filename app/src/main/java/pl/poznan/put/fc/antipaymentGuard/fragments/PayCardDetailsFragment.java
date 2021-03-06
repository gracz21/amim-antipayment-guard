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

import com.orm.SugarRecord;

import java.text.DateFormat;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;

/**
 * @author Kamil Walkowiak
 */
public class PayCardDetailsFragment extends Fragment {
    private static final String payCardArgKey = "payCardId";
    private long payCardId;
    private PayCard payCard;
    private TextView conditionStatusTextView;
    private ImageView conditionStatusIcon;
    private Context context;

    public static PayCardDetailsFragment newInstance(long payCardId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(payCardArgKey, payCardId);
        PayCardDetailsFragment bankAccountDetailsFragment = new PayCardDetailsFragment();
        bankAccountDetailsFragment.setArguments(bundle);
        return bankAccountDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payCardId = getArguments().getLong(payCardArgKey);
        payCard = SugarRecord.findById(PayCard.class, payCardId);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConditionStatusView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_card_details, container, false);
        context = view.getContext();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());

        TextView conditionTypeTextView = (TextView) view.findViewById(R.id.conditionTypeTextView);
        if(payCard.getCondition().getClass() == AmountCondition.class) {
            conditionTypeTextView.setText(context.getString(R.string.condition_transactions_amount));
        } else {
            conditionTypeTextView.setText(context.getString(R.string.condition_transactions_number));
        }

        conditionStatusTextView = (TextView) view.findViewById(R.id.conditionStatusTextView);
        conditionStatusIcon = (ImageView) view.findViewById(R.id.conditionStatusIconImageView);

        updateConditionStatusView();

        ((TextView) view.findViewById(R.id.nameTextView)).setText(payCard.getName());
        ((TextView) view.findViewById(R.id.descriptionTextView)).setText(payCard.getCardNumber());

        TextView bankNameTextView = (TextView) view.findViewById(R.id.bankNameTextView);
        if(payCard.getBankName() != null && payCard.getBankName().length() > 0) {
            bankNameTextView.setText(payCard.getBankName());
        } else {
            bankNameTextView.setVisibility(View.GONE);
            (view.findViewById(R.id.bankNameIconImageView)).setVisibility(View.GONE);
            (view.findViewById(R.id.bankNameLabelTextView)).setVisibility(View.GONE);
        }

        TextView expirationDateTextView = (TextView) view.findViewById(R.id.expirationDateTextView);
        if(payCard.getExpirationDate() != null) {
            expirationDateTextView.setText(dateFormat.format(payCard.getExpirationDate()));
        } else {
            expirationDateTextView.setVisibility(View.GONE);
            (view.findViewById(R.id.expirationDateIconImageView)).setVisibility(View.GONE);
            (view.findViewById(R.id.expirationDateLabelTextView)).setVisibility(View.GONE);
        }
        return view;
    }

    public void updateConditionStatusView() {
        payCard = SugarRecord.findById(PayCard.class, payCardId);

        String status = payCard.getCondition().getStatusString();
        if(payCard.getCondition().getClass() == AmountCondition.class) {
            status += " " + payCard.getCurrencyName();
        } else {
            status += " " + context.getString(R.string.transactions);
        }
        if(payCard.getCondition().checkCondition()) {
            conditionStatusIcon.setImageResource(R.drawable.ic_condition_fulfilled_24dp);
            conditionStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
            status = context.getString(R.string.condition_fulfilled);
        } else {
            conditionStatusIcon.setImageResource(R.drawable.ic_condition_not_fulfilled_24dp);
            conditionStatusTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        conditionStatusTextView.setText(status);
    }
}
