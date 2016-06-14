package pl.poznan.put.fc.antipaymentGuard.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.activities.AddPayCardActivity;
import pl.poznan.put.fc.antipaymentGuard.activities.PayCardActivity;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;

/**
 * @author Kamil Walkowiak
 */
public class PayCardsAdapter extends RecyclerView.Adapter<PayCardsAdapter.ViewHolder> {
    private List<PayCard> payCards;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView iconImageView;
        private TextView nameTextView;
        private TextView noTextView;
        private TextView balanceTextView;
        private TextView remainedTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.payCardIconImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            noTextView = (TextView) itemView.findViewById(R.id.noLabelTextView);
            balanceTextView = (TextView) itemView.findViewById(R.id.balanceTextView);
            remainedTextView = (TextView) itemView.findViewById(R.id.remainedTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(PayCard payCard) {
            String status;
            if(payCard.getCondition().checkCondition()) {
                iconImageView.setImageResource(R.drawable.ic_pay_card_fulfilled_48dp);
                remainedTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                status = context.getString(R.string.condition_fulfilled);
            } else {
                iconImageView.setImageResource(R.drawable.ic_pay_card_not_fulfilled_48dp);
                remainedTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                status = context.getString(R.string.remained) + ": " + payCard.getCondition().getStatusString();
                if(payCard.getCondition().getClass() == AmountCondition.class) {
                    status += " " + payCard.getCurrencyName();
                } else {
                    status += " " + context.getString(R.string.transactions);
                }
            }
            remainedTextView.setText(status);

            nameTextView.setText(payCard.getName());

            String no = context.getString(R.string.card_no) + ": " + payCard.getCardNumber();
            noTextView.setText(no);

            String balance = context.getString(R.string.balance) + ": " + payCard.getBalanceWithCurrencyName();
            balanceTextView.setText(balance);
        }

        @Override
        public boolean onLongClick(View v) {
            final PayCard selectedPayCard = payCards.get(getLayoutPosition());
            final Context context = v.getContext();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(selectedPayCard.getName());
            builder.setItems(R.array.context_options_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: {
                            long selectedPayCardId = payCards.get(getLayoutPosition()).getId();
                            Intent intent = new Intent(context, PayCardActivity.class);
                            intent.putExtra("payCardId", selectedPayCardId);
                            context.startActivity(intent);
                            break;
                        }
                        case 1: {
                            long selectedPayCardId = payCards.get(getLayoutPosition()).getId();
                            Intent intent = new Intent(context, AddPayCardActivity.class);
                            intent.putExtra("payCardId", selectedPayCardId);
                            context.startActivity(intent);
                            break;
                        }
                        case 2: {
                            SugarRecord.delete(selectedPayCard);
                            payCards.remove(selectedPayCard);
                            notifyItemRemoved(getLayoutPosition());
                            Toast.makeText(context, R.string.pay_card_deleted, Toast.LENGTH_SHORT).show();
                            break;
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
            long selectedPayCardId = payCards.get(getLayoutPosition()).getId();
            Intent intent = new Intent(v.getContext(), PayCardActivity.class);
            intent.putExtra("payCardId", selectedPayCardId);
            v.getContext().startActivity(intent);
        }
    }

    public PayCardsAdapter(List<PayCard> payCards, Context context) {
        this.payCards = payCards;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View payCardView = inflater.inflate(R.layout.item_pay_card, parent, false);
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
