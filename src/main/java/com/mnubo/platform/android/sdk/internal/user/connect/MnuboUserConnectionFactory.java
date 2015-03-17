package com.mnubo.platform.android.sdk.internal.user.connect;

import com.mnubo.platform.android.sdk.BuildConstants;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class MnuboUserConnectionFactory extends OAuth2ConnectionFactory<MnuboUserApi> {

    public MnuboUserConnectionFactory(String platformBaseUrl, String consumerKey, String consumerSecret,
                                      String authorizeUrl, String accessTokenUrl) {
        super(BuildConstants.MNUBO_PROVIDER,
                new MnuboUserServiceProvider(platformBaseUrl, consumerKey, consumerSecret,
                        authorizeUrl, accessTokenUrl),
                new MnuboUserAdapter());
    }

}

