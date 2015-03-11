package com.mnubo.platform.android.sdk.internal.user.connect;

import com.mnubo.platform.android.sdk.internal.connect.MnuboServiceProvider;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApiImpl;


public class MnuboUserServiceProvider extends MnuboServiceProvider<MnuboUserApi> {

    public MnuboUserServiceProvider(String platformBaseUrl, String consumerKey, String consumerSecret,
                                    String authorizeUrl, String accessTokenUrl, Boolean disableSSLCertificateHostnameCheck) {
        super(platformBaseUrl, consumerKey, consumerSecret,
                authorizeUrl, accessTokenUrl, disableSSLCertificateHostnameCheck);
    }

    public MnuboUserApi getApi(String accessToken) {
        return new MnuboUserApiImpl(accessToken, getPlatformBaseUrl(), getDisableSSLCertificateHostnameCheck());
    }

}