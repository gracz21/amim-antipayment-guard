package pl.poznan.put.fc.antipaymentGuard.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Random;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.activities.PayCardActivity;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;

/**
 * @author Kamil Walkowiak
 */
public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<PayCard> payCards = SugarRecord.listAll(PayCard.class);
        for(PayCard payCard: payCards){
            if(!payCard.getCondition().checkCondition()) {
                String notificationMessage = payCard.getName() + ": " + payCard.getCondition().getStatusString();
                if(payCard.getCondition().getClass() == AmountCondition.class) {
                    notificationMessage += " " + payCard.getCurrencyName();
                } else {
                    notificationMessage += " " + getApplicationContext().getString(R.string.transactions);
                }
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_condition_not_fulfilled_24dp)
                                .setContentTitle(getApplicationContext().getString(R.string.condition_not_fulfilled))
                                .setContentText(notificationMessage);

                Intent resultIntent = new Intent(this, PayCardActivity.class);
                resultIntent.putExtra("payCardId", payCard.getId());

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

                stackBuilder.addParentStack(PayCardActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(new Random().nextInt(), mBuilder.build());
            }
        }
    }
}
