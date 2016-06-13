package pl.poznan.put.fc.antipaymentGuard.backupTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import pl.poznan.put.fc.antipaymentGuard.R;

/**
 * @author Kamil Walkowiak
 */
public class BackupTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = "BackupTask";

    private Context context;
    private GoogleApiClient googleApiClient;
    private int result;

    public BackupTask(Context context, GoogleApiClient googleApiClient) {
        this.context = context;
        this.googleApiClient = googleApiClient;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final File file = context.getDatabasePath("apg10.db");

        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                        if (!driveContentsResult.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }
                        Log.i(TAG, "New contents created.");
                        final DriveContents driveContents = driveContentsResult.getDriveContents();
                        FileChannel destFileChannel = ((FileOutputStream)driveContentsResult.getDriveContents().getOutputStream()).getChannel();
                        try {
                            FileChannel sourceFileChannel = new FileInputStream(file).getChannel();
                            destFileChannel.transferFrom(sourceFileChannel, 0, sourceFileChannel.size());
                            sourceFileChannel.close();
                            destFileChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MetadataChangeSet meta = new MetadataChangeSet.Builder().setTitle("apg_backup.db").setMimeType("application/x-sqlite3").build();
                        Drive.DriveApi.getRootFolder(googleApiClient)
                                .createFile(googleApiClient, meta, driveContents)
                                .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                    @Override
                                    public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                        if (!driveFileResult.getStatus().isSuccess()) {
                                            result = -1;
                                        } else {
                                            result = 1;
                                        }
                                    }
                                });
                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(result == -1) {
            Toast.makeText(context, R.string.backup_error, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.backup_success, Toast.LENGTH_LONG).show();
        }
    }
}
