package com.mnubo.platform.android.demo.services.phone;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import com.mnubo.platform.android.demo.intentutils.Actions;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.model.phone.PhoneEvent;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.store.MnuboDataStore;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.joda.time.DateTime;

import static com.mnubo.platform.android.demo.model.phone.PhoneEvent.Type;

public class PhoneEventService extends IntentService {

    private final static String SERVICE_TAG = PhoneEventService.class.getName();
    private final static String MYSTORE = "MY_STORE";

    private MnuboApi mnuboApi;
    private MnuboDataStore mnuboDataStore;
    private String objectId;
    private LocationManager locationManager;
    private Type eventType = Type.install;


    public PhoneEventService() {
        super(SERVICE_TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String eventTypeExtra = intent.getStringExtra(Extras.EXTRA_EVENT_TYPE);
        objectId = intent.getStringExtra(Extras.EXTRA_USEROBJECT_UUID);
        if (!TextUtils.isEmpty(eventTypeExtra)) {
            eventType = Type.valueOf(eventTypeExtra);
        }

        this.mnuboApi = Mnubo.getApi();
        this.mnuboDataStore = Mnubo.getDataStore();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Samples samples = prepareSamples(this.eventType);

        SdkId id = SdkId.build(this.objectId, IdType.objectid);
        try {
            mnuboApi.getSmartObjectOperations().addSamplesAsync(id, samples, null);
        } catch (MnuboException ex) {
            Log.e(SERVICE_TAG, "Unable to post phone samples", ex);
            broadcastMessage("Event posting failed.");
            return;
        }

        broadcastMessage("Event posted");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void broadcastMessage(final String message) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Actions.ACTION_TOAST_MESSAGE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(Extras.EXTRA_SERVICE_MESSAGE, message);
        sendBroadcast(broadcastIntent);
    }

    private Samples prepareSamples(Type eventType) {

        PhoneEvent event;

        switch (eventType) {

            case install:
                event = createInstallEvent();
                break;

            case launch:
                event = createLaunchedEvent();
                break;

            default:
                event = createInstallEvent();
                break;
        }

        Samples samples = new Samples();
        samples.addSample(event.getSample());
        return samples;
    }

    private PhoneEvent createBasePhoneEvent() {

        final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final String appVersion = "1.9.7.2090";
        final String unknown = "unknown";

        return new PhoneEvent()
                .location(location)
                .timestamp(DateTime.now().toString())
                .appVersion(appVersion)
                .bdaddr(unknown);
    }

    private PhoneEvent createLaunchedEvent() {
        return createBasePhoneEvent()
                .type(Type.launch).appUsageState("launched");
    }

    private PhoneEvent createInstallEvent() {
        return createBasePhoneEvent()
                .type(Type.install)
                .appFunnelState("installed")
                .appLifeState("installed");


    }
}
