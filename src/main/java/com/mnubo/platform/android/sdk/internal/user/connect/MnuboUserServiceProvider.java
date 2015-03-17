package com.mnubo.platform.android.sdk.internal.user.connect;

import com.mnubo.platform.android.sdk.internal.connect.MnuboServiceProvider;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApiImpl;


class MnuboUserServiceProvider extends MnuboServiceProvider<MnuboUserApi> {

    public MnuboUserServiceProvider(String platformBaseUrl, String consumerKey, String consumerSecret,
                                    String authorizeUrl, String accessTokenUrl) {
        super(platformBaseUrl, consumerKey, consumerSecret,
                authorizeUrl, accessTokenUrl);
    }

    public MnuboUserApi getApi(String accessToken) {
        return new MnuboUserApiImpl(accessToken, getPlatformBaseUrl());
    }

}