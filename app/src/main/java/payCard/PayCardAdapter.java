package payCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;

/**
 * @author Kamil Walkowiak
 */
public class PayCardAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PayCard> mPayCardList;

    public PayCardAdapter(Context mContext, LayoutInflater mInflater, List<PayCard> mPayCardList) {
        this.mContext = mContext;
        this.mInflater = mInflater;
        this.mPayCardList = mPayCardList;
    }

    @Override
    public int getCount() {
        return mPayCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPayCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_pay_cards, null);

            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.noTextView = (TextView) convertView.findViewById(R.id.noTextView);
            holder.balanceTextView = (TextView) convertView.findViewById(R.id.balanceTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PayCard payCard = (PayCard) getItem(position);

        holder.nameTextView.setText(payCard.getName());
        holder.noTextView.setText(payCard.getNo());
        holder.balanceTextView.setText(NumberFormat.getInstance().format(payCard.getBalance()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView nameTextView;
        public TextView noTextView;
        public TextView balanceTextView;
    }
}
