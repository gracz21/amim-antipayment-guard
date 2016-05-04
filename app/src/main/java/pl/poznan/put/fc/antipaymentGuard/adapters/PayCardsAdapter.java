package pl.poznan.put.fc.antipaymentGuard.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.databaseHelpers.PayCardDatabaseHelper;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;

/**
 * @author Kamil Walkowiak
 */
public class PayCardsAdapter extends RecyclerView.Adapter<PayCardsAdapter.ViewHolder> {
    private List<PayCard> payCards;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView nameTextView;
        private TextView noTextView;
        private TextView balanceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            noTextView = (TextView) itemView.findViewById(R.id.noTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(PayCard payCard) {
            nameTextView.setText(payCard.getName());
            noTextView.setText(payCard.getNo());

            DecimalFormat df = new DecimalFormat();
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
            balanceTextView.setText(df.format(payCard.getBalance()));
        }

        @Override
        public boolean onLongClick(View v) {
            final PayCard selectedPayCard = payCards.get(getLayoutPosition());
            final Context context = v.getContext();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(selectedPayCard.getName());
            builder.setItems(R.array.pay_cards_options_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 2: {
                            PayCardDatabaseHelper payCardDatabaseHelper = new PayCardDatabaseHelper(context);
                            if(payCardDatabaseHelper.deletePayCard(selectedPayCard.getNo())) {
                                int position = payCards.indexOf(selectedPayCard);
                                payCardDatabaseHelper.deletePayCard(selectedPayCard.getNo());
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Selected pay card has been removed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Problems occurred while removing pay card", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }

        @Override
        public void onClick(View v) {
        }
    }

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
        holder.bind(payCard);
    }

    @Override
    public int getItemCount() {
        return payCards.size();
    }
}
