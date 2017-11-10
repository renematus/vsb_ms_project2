package droid.vsb.ms.rmatu.gcloudclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import droid.vsb.ms.rmatu.gcloudclient.events.ListenChangeEventsForFilesActivity;
import droid.vsb.ms.rmatu.gcloudclient.events.SubscribeChangeEventsForFilesActivity;
import droid.vsb.ms.rmatu.gcloudclient.view.CloudClientActivity;
import droid.vsb.ms.rmatu.gcloudclient.view.CreateFileInFolderActivity;

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