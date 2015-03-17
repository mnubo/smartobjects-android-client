package com.mnubo.platform.android.sdk.internal.client.connect;

import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApiImpl;
import com.mnubo.platform.android.sdk.internal.connect.MnuboServiceProvider;


public class MnuboClientServiceProvider extends MnuboServiceProvider<MnuboClientApi> {

    public MnuboClientServiceProvider(String platformBaseUrl, String consumerKey, String consumerSecret,
                                      String authorizeUrl, String accessTokenUrl) {
        super(platformBaseUrl, consumerKey, consumerSecret,
                authorizeUrl, accessTokenUrl);
    }

    public MnuboClientApi getApi(String accessToken) {
        return new MnuboClientApiImpl(accessToken, getPlatformBaseUrl());
    }

}