package com.mnubo.platform.android.demo.views.object.sensor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mnubo.platform.android.demo.MnuboAbstractActivity;
import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends MnuboAbstractActivity {

    static final String ACTIVITY_TAG = SensorActivity.class.getSimpleName();

    private String objectId;
    private String sensorName;

    private ListView lstSamples;
    private List<Sample> samples = new ArrayList<>();
    private ArrayAdapter<Sample> sampleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.objectId = extras.getString(Extras.EXTRA_USEROBJECT_UUID);
            this.sensorName = extras.getString(Extras.EXTRA_SENSOR_NAME);

            setTitle(this.sensorName);
        }

        initListViews();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mnuboApi.getAuthenticationOperations().isUserConnected()) {
            super.showLogInActivity();
        } else {
            fetchSensorSamples();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListViews() {

        lstSamples = (ListView) findViewById(R.id.lst_samples);

        this.sampleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                samples);

        this.lstSamples.setAdapter(this.sampleAdapter);
    }

    private void fetchSensorSamples() {

        if (!TextUtils.isEmpty(sensorName) && !TextUtils.isEmpty(objectId)) {

            this.mnuboApi.getSmartObjectOperations().searchSamplesAsync(SdkId.build(objectId, IdType.objectid), sensorName, new MnuboApi.CompletionCallBack<Samples>() {
                @Override
                public void onCompletion(Samples result, MnuboException ex) {
                    showProgress(false);

                    if (ex == null) {
                        samples.clear();
                        samples.addAll(result.getSamples());
                        sampleAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
