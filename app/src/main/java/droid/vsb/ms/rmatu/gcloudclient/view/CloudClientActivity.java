package droid.vsb.ms.rmatu.gcloudclient.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import droid.vsb.ms.rmatu.gcloudclient.R;
import lib.folderpicker.FolderPicker;

/**
 * Created by renematuszek on 10/11/2017.
 */

public class CloudClientActivity extends AppCompatActivity {

    final static int FOLDERPICKER_CODE = 100;

    TextView tvSelectedFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_client);

        tvSelectedFile =(TextView)findViewById(R.id.chosenFile);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = intent.getExtras().getString("data");
            Log.i("folderLocation", folderLocation);

            tvSelectedFile.setText(folderLocation);

        }
    }


    public void chooseFile(View view) {
        Intent intent = new Intent(this, FolderPicker.class);
        //To show a custom title
        intent.putExtra("title", "Select file to upload");
        //To begin from a selected folder instead of sd card's root folder. Example : Pictures directory
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        //To pick files
        intent.putExtra("pickFiles", true);
        startActivityForResult(intent, FOLDERPICKER_CODE);



    }

    public void upload(View view) {
        Intent intent = new Intent(this, CreateFileInFolderActivity.class);
        intent.putExtra("fileName", tvSelectedFile.getText());
        startActivity(intent);

    }
}
