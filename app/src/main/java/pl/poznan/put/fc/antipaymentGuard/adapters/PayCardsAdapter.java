package pl.poznan.put.fc.antipaymentGuard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

/**
 * @author Kamil Walkowiak
 */
public class PayCardsAdapter extends RecyclerView.Adapter<PayCardsAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView noTextView;
        private TextView balanceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            noTextView = (TextView) itemView.findViewById(R.id.noTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);
        }

        public void setNameTextViewText(String text) {
            nameTextView.setText(text);
        }

        public void setNoTextViewText(String text) {
            noTextView.setText(text);
        }

        public void setBalanceTextViewText(String text) {
            balanceTextView.setText(text);
        }
    }

    private List<PayCard> payCards;

    public PayCardsAdapter(List<PayCard> payCards) {
        this.payCards = payCards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View payCardView = inflater.inflate(R.layout.item_pay_cards, parent, false);
        return new ViewHolder(payCardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PayCard payCard = payCards.get(position);

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        holder.setNameTextViewText(payCard.getName());
        holder.setNoTextViewText(payCard.getNo());
        holder.setBalanceTextViewText(df.format(payCard.getBalance()));
    }

    @Override
    public int getItemCount() {
        return payCards.size();
    }
}
