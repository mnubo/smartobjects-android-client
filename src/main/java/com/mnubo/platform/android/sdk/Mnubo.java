package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.util.Log;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboAlreadyInitializedException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotInitializedException;

/**
 * This class is the entry point to use the SDK. The init method must be called prior
 * to anything else.
 */
public class Mnubo {

    private static final String MNUBO_TAG = Mnubo.class.getName();

    private static MnuboApiFactory mnuboApiFactory;

    private Mnubo() {
    }

    /**
     * This method is used to initialise the SDK. Call it once, before you do anything
     * else with the SDK.
     *
     * @param context        Android context of the application using the SDK
     * @param hostname       Hostname where your Mnubo services are available
     *                       ( Eg : mycompany.api.mnubo.com)
     * @param consumerKey    The consumer key provided by Mnubo
     * @param consumerSecret The consumer secret that came along the key given above
     */
    public static void init(Context context,
                            String consumerKey, String consumerSecret, String hostname) {
        if (mnuboApiFactory != null) {
            throw new MnuboAlreadyInitializedException();
        }
        Log.d(MNUBO_TAG, Strings.SDK_INITIALIZED);

        mnuboApiFactory = new MnuboApiFactory(context, hostname, consumerKey, consumerSecret);
    }

    /**
     * Returns a MnuboApi instance to be used to perform calls against the Mnubo
     * system.
     *
     * @return an instance of MnuboApi
     */
    public static MnuboApi getApi() {
        if (mnuboApiFactory == null) {
            throw new MnuboNotInitializedException();
        }

        return mnuboApiFactory.getApi();
    }

    /**
     * Returns the last logged in username;
     *
     * @return the last username
     */
    @Deprecated
    public static String getUsername() {
        if (mnuboApiFactory == null) {
            throw new MnuboNotInitializedException();
        }

        return mnuboApiFactory.readUsername();
    }


}
