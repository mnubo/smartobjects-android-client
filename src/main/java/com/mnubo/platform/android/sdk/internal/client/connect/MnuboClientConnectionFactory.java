package com.mnubo.platform.android.sdk.internal.client.connect;

import com.mnubo.platform.android.sdk.BuildConstants;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class MnuboClientConnectionFactory extends OAuth2ConnectionFactory<MnuboClientApi> {


    public MnuboClientConnectionFactory(String platformBaseUrl, String consumerKey, String consumerSecret,
                                        String authorizeUrl, String accessTokenUrl, Boolean disableSSLCertificateHostnameCheck) {
        super(BuildConstants.MNUBO_PROVIDER,
                new MnuboClientServiceProvider(platformBaseUrl, consumerKey, consumerSecret,
                        authorizeUrl, accessTokenUrl, disableSSLCertificateHostnameCheck),
                new MnuboClientAdapter());
    }

}

