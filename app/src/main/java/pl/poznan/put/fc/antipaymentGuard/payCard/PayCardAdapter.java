package pl.poznan.put.fc.antipaymentGuard.payCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.poznan.put.fc.antipaymentGuard.R;

/**
 * @author Kamil Walkowiak
 */
public class PayCardAdapter extends ArrayAdapter {
    private static class ViewHolder {
        public TextView nameTextView;
        public TextView noTextView;
        public TextView balanceTextView;
    }

    public PayCardAdapter(Context context, ArrayList<PayCard> payCards) {
        super(context, R.layout.row_pay_cards, payCards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PayCard payCard = (PayCard) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_pay_cards, parent, false);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.noTextView = (TextView) convertView.findViewById(R.id.noTextView);
            viewHolder.balanceTextView = (TextView) convertView.findViewById(R.id.balanceTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        viewHolder.nameTextView.setText(payCard.getName());
        viewHolder.noTextView.setText(payCard.getNo());
        viewHolder.balanceTextView.setText(df.format(payCard.getBalance()));

        return convertView;
    }
}
