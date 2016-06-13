package pl.poznan.put.fc.antipaymentGuard.backupTasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import pl.poznan.put.fc.antipaymentGuard.activities.MainActivity;

/**
 * @author Kamil Walkowiak
 */
public class RestoreTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "RestoreTask";

    private Context context;
    private GoogleApiClient googleApiClient;
    private DriveId driveId;
    private int result;

    public RestoreTask(Context context, GoogleApiClient googleApiClient, DriveId driveId) {
        this.context = context;
        this.googleApiClient = googleApiClient;
        this.driveId = driveId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final File file = context.getDatabasePath("apg10.db");

        DriveFile backup = Drive.DriveApi.getFile(googleApiClient, driveId);
        backup.open(googleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    result = -1;
                    return;
                }

                FileChannel sourceFileChannel = ((FileInputStream)driveContentsResult.getDriveContents().getInputStream()).getChannel();
                try {
                    FileChannel destFileChannel = new FileOutputStream(file).getChannel();
                    destFileChannel.transferFrom(sourceFileChannel, 0, sourceFileChannel.size());
                    sourceFileChannel.close();
                    destFileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = 1;
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(result == -1) {
            Toast.makeText(context, "Error while trying to restore the database", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Database have been restored.\nRestart app to see changes", Toast.LENGTH_LONG).show();
        }
    }
}
