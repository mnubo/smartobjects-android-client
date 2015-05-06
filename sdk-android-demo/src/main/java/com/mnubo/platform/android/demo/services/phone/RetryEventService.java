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

public class RetryEventService extends IntentService {

    private final static String SERVICE_TAG = RetryEventService.class.getName();

    public RetryEventService() {
        super(SERVICE_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Mnubo.getBufferService().retryFailedAttempts();
        } catch (MnuboException ex) {
            Log.e(SERVICE_TAG, "Unable to post phone samples", ex);
            broadcastMessage("Event posting failed.");
            return;
        }

        broadcastMessage("Retrying failed events.");

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
}
