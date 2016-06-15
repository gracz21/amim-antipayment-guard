package pl.poznan.put.fc.antipaymentGuard.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.models.conditions.AmountCondition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.Condition;
import pl.poznan.put.fc.antipaymentGuard.models.conditions.NumberCondition;

/**
 * @author Kamil Walkowiak
 */
public class ResetConditionsService extends IntentService {
    public ResetConditionsService() {
        super("ResetConditionsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Condition> conditions = new ArrayList<>();
        conditions.addAll(SugarRecord.listAll(AmountCondition.class));
        conditions.addAll(SugarRecord.listAll(NumberCondition.class));
        for(Condition condition: conditions) {
            condition.resetCondition();
        }
    }
}
