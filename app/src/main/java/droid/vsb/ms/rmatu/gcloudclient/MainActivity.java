package droid.vsb.ms.rmatu.gcloudclient;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import droid.vsb.ms.rmatu.gcloudclient.events.ListenChangeEventsForFilesActivity;
import droid.vsb.ms.rmatu.gcloudclient.events.SubscribeChangeEventsForFilesActivity;
import droid.vsb.ms.rmatu.gcloudclient.view.CloudClientActivity;

public class MainActivity extends Activity {
    private final Class[] sActivities = new Class[] {CloudClientActivity.class, CreateEmptyFileActivity.class,
            CreateFileActivity.class, CreateFolderActivity.class, CreateFileInFolderActivity.class,
            CreateFolderInFolderActivity.class, CreateFileInAppFolderActivity.class,
            CreateFileWithCreatorActivity.class, RetrieveMetadataActivity.class,
            RetrieveContentsActivity.class, RetrieveContentsWithProgressDialogActivity.class,
            EditMetadataActivity.class, AppendContentsActivity.class, RewriteContentsActivity.class,
            PinFileActivity.class, InsertUpdateCustomPropertyActivity.class,
            DeleteCustomPropertyActivity.class, QueryFilesActivity.class,
            QueryFilesInFolderActivity.class, QueryNonTextFilesActivity.class,
            QuerySortedFilesActivity.class, QueryFilesSharedWithMeActivity.class,
            QueryFilesWithTitleActivity.class, QueryFilesWithCustomPropertyActivity.class,
            QueryStarredTextFilesActivity.class, QueryTextOrHtmlFilesActivity.class,
            ListenChangeEventsForFilesActivity.class, SubscribeChangeEventsForFilesActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] titles = getResources().getStringArray(R.array.titles_array);
        ListView mListViewSamples = (ListView) findViewById(R.id.listViewSamples);
        mListViewSamples.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));
        mListViewSamples.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
                Intent intent = new Intent(getBaseContext(), sActivities[i]);
                startActivity(intent);
            }
        });
    }
}