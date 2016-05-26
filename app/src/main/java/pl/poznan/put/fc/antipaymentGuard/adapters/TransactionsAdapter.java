package pl.poznan.put.fc.antipaymentGuard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private List<PayCardTransaction> payCardTransactions;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView iconImageView;
        private TextView titleTextView;
        private TextView amountTextView;
        private TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.transactionIconImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.transactionTitleTextView);
            amountTextView = (TextView) itemView.findViewById(R.id.transactionAmountTextVew);
            dateTextView = (TextView) itemView.findViewById(R.id.transactionDateTextVew);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(PayCardTransaction payCardTransaction) {
            if(payCardTransaction.getAmount() > 0) {
                iconImageView.setImageResource(R.drawable.ic_transaction_in_48dp);
            } else {
                iconImageView.setImageResource(R.drawable.ic_transaction_out_48dp);
            }

            //TODO title
            titleTextView.setText("Title");

            String amount = context.getString(R.string.amount) + ": " + payCardTransaction.getAmountWithCurrencyName();
            amountTextView.setText(payCardTransaction.getAmountWithCurrencyName());

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
            String date = context.getString(R.string.transactionDate) + ": " + dateFormat.format(payCardTransaction.getDate());
            dateTextView.setText(date);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public TransactionsAdapter(List<PayCardTransaction> payCardTransactions, Context context) {
        this.payCardTransactions = payCardTransactions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transactionView = inflater.inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(transactionView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(payCardTransactions.get(position));
    }

    @Override
    public int getItemCount() {
        return payCardTransactions.size();
    }
}
