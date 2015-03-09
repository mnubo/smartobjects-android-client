package com.mnubo.platform.android.sdk.internal.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;


public abstract class MnuboServiceProvider<API> extends AbstractOAuth2ServiceProvider<API> {

    private final String platformBaseUrl;
    private final Boolean disableSSLCertificateHostnameCheck;

    public MnuboServiceProvider(String platformBaseUrl, String consumerKey, String consumerSecret,
                                String authorizeUrl, String accessTokenUrl, Boolean disableSSLCertificateHostnameCheck) {
        super(new MnuboOAuth2Template(consumerKey,
                        consumerSecret,
                        platformBaseUrl + authorizeUrl,
                        platformBaseUrl + accessTokenUrl,
                        disableSSLCertificateHostnameCheck)
        );
        this.disableSSLCertificateHostnameCheck = disableSSLCertificateHostnameCheck;
        this.platformBaseUrl = platformBaseUrl;
    }

    public String getPlatformBaseUrl() {
        return platformBaseUrl;
    }

    public Boolean getDisableSSLCertificateHostnameCheck() {
        return disableSSLCertificateHostnameCheck;
    }
}