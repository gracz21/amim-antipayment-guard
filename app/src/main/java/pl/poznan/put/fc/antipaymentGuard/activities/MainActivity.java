package pl.poznan.put.fc.antipaymentGuard.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.poznan.put.fc.antipaymentGuard.R;
import pl.poznan.put.fc.antipaymentGuard.adapters.PayCardsAdapter;
import pl.poznan.put.fc.antipaymentGuard.backupTasks.BackupTask;
import pl.poznan.put.fc.antipaymentGuard.backupTasks.RestoreTask;
import pl.poznan.put.fc.antipaymentGuard.models.PayCard;
import pl.poznan.put.fc.antipaymentGuard.services.NotificationService;
import pl.poznan.put.fc.antipaymentGuard.services.ResetConditionsService;
import pl.poznan.put.fc.antipaymentGuard.utils.CurrentMonthStartDateUtil;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView rvPayCards;

    private PayCardsAdapter payCardAdapter;

    private List<PayCard> payCards;

    private int actionId;
    private GoogleApiClient mGoogleApiClient;

    private static final int BACKUP_ACTION = 1;
    private static final int LOAD_ACTION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        setSupportActionBar(toolbar);

        payCards = new ArrayList<>();
        payCardAdapter = new PayCardsAdapter(payCards, this);
        rvPayCards.setAdapter(payCardAdapter);
        rvPayCards.setLayoutManager(new LinearLayoutManager(this));
        rvPayCards.setHasFixedSize(true);

        setListeners();
        setupNotificationService();
        setupResetConditionService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_backup:
                actionId = BACKUP_ACTION;
                connectToGoogleDrive();
                return true;
            case R.id.action_load:
                actionId = LOAD_ACTION;
                connectToGoogleDrive();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new FetchPayCardsTask()).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    (new RestoreTask(this, mGoogleApiClient, driveId)).execute();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void findViewsById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        rvPayCards = (RecyclerView) findViewById(R.id.rvPayCards);
    }

    private void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPayCardActivity.class));
            }
        });
    }

    private void setupNotificationService() {
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10*1000, pintent);
    }

    private void setupResetConditionService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CurrentMonthStartDateUtil.getCurrentMonthStartDate());
        calendar.add(Calendar.MONTH, 1);
        long notificationTime = calendar.getTimeInMillis();

        calendar.add(Calendar.MONTH, 1);
        long nextNotificationTime = calendar.getTimeInMillis();

        Intent intent = new Intent(this, ResetConditionsService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime,
                nextNotificationTime - notificationTime, pintent);
    }

    private void connectToGoogleDrive() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        switch (actionId) {
            case BACKUP_ACTION:
                (new BackupTask(this, mGoogleApiClient)).execute();
                break;
            case LOAD_ACTION:
                IntentSender intentSender = Drive.DriveApi
                        .newOpenFileActivityBuilder()
                        .setMimeType(new String[] { "application/x-sqlite3" })
                        .build(mGoogleApiClient);
                try {
                    startIntentSenderForResult(
                            intentSender, 1, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.w(TAG, "Unable to send intent");
                }
                break;
            default:
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }
        try {
            connectionResult.startResolutionForResult(this, 3);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    private class FetchPayCardsTask extends AsyncTask<Void, Void, List<PayCard>> {
        @Override
        protected List<PayCard> doInBackground(Void... params) {
            return Select.from(PayCard.class).list();
        }

        @Override
        protected void onPostExecute(List<PayCard> loadedPayCards) {
            int size = payCards.size();
            if(size != 0) {
                payCards.clear();
                payCardAdapter.notifyDataSetChanged();
            }
            payCards.addAll(loadedPayCards);
            payCardAdapter.notifyDataSetChanged();
        }
    }
}
