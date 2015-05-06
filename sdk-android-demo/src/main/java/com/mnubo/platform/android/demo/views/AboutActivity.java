package com.mnubo.platform.android.demo.views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.views.collection.AddCollectionDialog;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService;
import com.mnubo.platform.android.sdk.api.store.MnuboDataStore;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.collections.Collection;
import com.mnubo.platform.android.sdk.models.users.User;

import java.util.ArrayList;
import java.util.List;

import static com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService.RETRY_QUEUE_NAME;

public class AboutActivity extends MnuboAbstractActivity {

    static final String TAG = AboutActivity.class.getSimpleName();

    private MnuboDataStore store;
    private TextView txtFileCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        store = Mnubo.getDataStore();
        txtFileCount = (TextView) this.findViewById(R.id.txt_store_files_qty);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateFileCount();
    }

    private void updateFileCount() {
        if(txtFileCount != null){
            final int fileCount = store.getEntities(RETRY_QUEUE_NAME).size();
            txtFileCount.setText(String.format("There are %d files in the store", fileCount));
        } else {
            Log.e(TAG, "Unable to find text box for file count.");
        }
    }

}
