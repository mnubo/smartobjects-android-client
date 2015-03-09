package com.mnubo.platform.android.demo;

import android.app.Application;

import com.mnubo.platform.android.sdk.Mnubo;


public class MainApplication extends Application {

    @Override
    public void onCreate() {
        Mnubo.init(this,
                getString(R.string.mnubo_consumer_key),
                getString(R.string.mnubo_consumer_secret),
                getString(R.string.mnubo_hostname));

    }

}