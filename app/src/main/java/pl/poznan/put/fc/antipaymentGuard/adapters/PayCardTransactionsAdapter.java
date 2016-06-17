package pl.poznan.put.fc.antipaymentGuard.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.activities.AddPayCardTransactionActivity;
import pl.poznan.put.fc.antipaymentGuard.activities.PayCardTransactionActivity;
import pl.poznan.put.fc.antipaymentGuard.fragments.PayCardDetailsFragment;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.PayCardTransaction;

/**
 * @author Kamil Walkowiak
 */
public class PayCardTransactionsAdapter extends RecyclerView.Adapter<PayCardTransactionsAdapter.ViewHolder> {
    private PayCard payCard;
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

            titleTextView.setText(payCardTransaction.getName());

            String amount = context.getString(R.string.amount) + ": " + payCardTransaction.getAmountWithCurrencyName();
            amountTextView.setText(amount);

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
            String date = context.getString(R.string.transactionDate) + ": " + dateFormat.format(payCardTransaction.getDate());
            dateTextView.setText(date);
        }

        @Override
        public void onClick(View v) {
            PayCardTransaction transaction = payCardTransactions.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), PayCardTransactionActivity.class);
            intent.putExtra("transaction", transaction);
            v.getContext().startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            final PayCardTransaction selectedTransaction = payCardTransactions.get(getLayoutPosition());
            final Context context = v.getContext();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(selectedTransaction.getName());
            builder.setItems(R.array.context_options_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: {
                            Intent intent = new Intent(context, PayCardTransactionActivity.class);
                            intent.putExtra("transaction", selectedTransaction);
                            context.startActivity(intent);
                            break;
                        }
                        case 1: {
                            long transactionId = payCardTransactions.get(getLayoutPosition()).getId();
                            Intent intent = new Intent(context, AddPayCardTransactionActivity.class);
                            intent.putExtra("transactionId", transactionId);
                            context.startActivity(intent);
                            break;
                        }
                        case 2: {
                            payCard.removeTransaction(selectedTransaction);
                            SugarRecord.delete(selectedTransaction);
                            payCardTransactions.remove(selectedTransaction);
                            notifyItemRemoved(getLayoutPosition());
                            FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
                            String tag = "android:switcher:" + R.id.container + ":" + 0;
                            PayCardDetailsFragment payCardDetailsFragment = (PayCardDetailsFragment) manager.findFragmentByTag(tag);
                            payCardDetailsFragment.updateConditionStatusView();
                            Toast.makeText(context, R.string.transaction_deleted, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
    }

    public PayCardTransactionsAdapter(PayCard payCard, List<PayCardTransaction> payCardTransactions, Context context) {
        this.payCard = payCard;
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
