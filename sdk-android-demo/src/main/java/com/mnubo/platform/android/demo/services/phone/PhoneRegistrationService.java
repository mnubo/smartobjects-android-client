package com.mnubo.platform.android.demo.services.phone;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.mnubo.platform.android.demo.R;
import com.mnubo.platform.android.demo.intentutils.Actions;
import com.mnubo.platform.android.demo.intentutils.Extras;
import com.mnubo.platform.android.demo.model.phone.Phone;
import com.mnubo.platform.android.sdk.Mnubo;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import org.joda.time.DateTime;

public class PhoneRegistrationService extends IntentService {

    private final static String SERVICE_TAG = PhoneRegistrationService.class.getName();

    private MnuboApi mnuboApi;
    private LocationManager locationManager;

    public PhoneRegistrationService() {
        super(SERVICE_TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.mnuboApi = Mnubo.getApi();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        final String username = mnuboApi.getAuthenticationOperations().getUsername();
        final String registrationDate = DateTime.now().toString();
        final Location location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final String serial = TextUtils.isEmpty(Build.SERIAL) ? "UNKNOWN" : Build.SERIAL;
        final String model = TextUtils.isEmpty(Build.MODEL) ? "UNKNOWN" : Build.MODEL;
        final String appName = "CONNECTED WATCH ENTERPRISE";
        final String appVersion = "1.9.7.2090";

        Phone phone = new Phone()
                .deviceId(Build.SERIAL)
                .owner(username)
                .registrationDate(registrationDate)
                .registationLocation(location)
                .phoneId(serial)
                .phoneModel(model)
                .phoneCapacity("unknown")
                .phoneAppName(appName)
                .phoneAppVersion(appVersion);

        try {
            mnuboApi.getSmartObjectOperations().createObjectAsync(phone.getSmartObject(), true, null);
            broadcastMessage(getString(R.string.successful_phone_registration));
        } catch (MnuboException ex) {
            Log.e(SERVICE_TAG, "Unable to createUserAsync phone object.", ex);
            broadcastMessage(getString(R.string.failed_phone_registration));
        }
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
