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

package droid.vsb.ms.rmatu.gcloudclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import droid.vsb.ms.rmatu.gcloudclient.R;

/**
 * An activity to create a file inside a folder.
 */
public class CreateFileInFolderActivity extends BaseActivity {
    private static final String TAG = "CreateFileActivity";

    String mUploadFileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent  uploadFileIntent =  getIntent();
        if (uploadFileIntent!=null)
        {
            mUploadFileName = uploadFileIntent.getStringExtra("fileName");
        }

    }

    @Override
    protected void onDriveClientReady() {
        pickFolder()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                createFileInFolder(driveId.asDriveFolder());
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No folder selected", e);
                        showMessage(getString(R.string.folder_not_selected));
                        finish();
                    }
                });
    }

    private void createFileInFolder(final DriveFolder parent) {
        getDriveResourceClient()
                .createContents()
                .continueWithTask(new Continuation<DriveContents, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<DriveContents> task)
                            throws Exception {
                        File uploadFile = new File(mUploadFileName);
                        InputStream inStream = new FileInputStream(uploadFile);

                        DriveContents contents = task.getResult();
                        OutputStream outputStream = contents.getOutputStream();

                        byte[] buffer = new byte[100000];
                        int bytesRead = 0;
                        while ((bytesRead = inStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                                              .setTitle(uploadFile.getName())
                                                              .setStarred(true)
                                                              .build();

                        return getDriveResourceClient().createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                showMessage(getString(R.string.file_created,
                                        driveFile.getDriveId().encodeToString()));
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        showMessage(getString(R.string.file_create_error));
                    }
                });
    }
}