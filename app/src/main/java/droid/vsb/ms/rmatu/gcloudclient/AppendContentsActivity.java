/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package droid.vsb.ms.rmatu.gcloudclient;

import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import droid.vsb.ms.rmatu.gcloudclient.view.BaseActivity;

/**
 * An activity to illustrate how to edit contents of a Drive file.
 */
public class AppendContentsActivity extends BaseActivity {
    private static final String TAG = "AppendContentsActivity";

    @Override
    protected void onDriveClientReady() {
        pickTextFile()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                appendContents(driveId.asDriveFile());
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No file selected", e);
                        showMessage(getString(R.string.file_not_selected));
                        finish();
                    }
                });
    }

    private void appendContents(DriveFile file) {
        // [START open_for_append]
        Task<DriveContents> openTask =
                getDriveResourceClient().openFile(file, DriveFile.MODE_READ_WRITE);
        // [END open_for_append]
        // [START append_contents]
        openTask.continueWithTask(new Continuation<DriveContents, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                        DriveContents driveContents = task.getResult();
                        ParcelFileDescriptor pfd = driveContents.getParcelFileDescriptor();
                        long bytesToSkip = pfd.getStatSize();
                        try (InputStream in = new FileInputStream(pfd.getFileDescriptor())) {
                            // Skip to end of file
                            while (bytesToSkip > 0) {
                                long skipped = in.skip(bytesToSkip);
                                bytesToSkip -= skipped;
                            }
                        }
                        try (OutputStream out = new FileOutputStream(pfd.getFileDescriptor())) {
                            out.write("Hello world".getBytes());
                        }
                        // [START commit_contents_with_metadata]
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                                              .setStarred(true)
                                                              .setLastViewedByMeDate(new Date())
                                                              .build();
                        Task<Void> commitTask =
                                getDriveResourceClient().commitContents(driveContents, changeSet);
                        // [END commit_contents_with_metadata]
                        return commitTask;
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage(getString(R.string.content_updated));
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to update contents", e);
                        showMessage(getString(R.string.content_update_failed));
                        finish();
                    }
                });
        // [END append_contents]
    }
}
