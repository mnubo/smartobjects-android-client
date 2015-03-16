package com.mnubo.platform.android.sdk.internal.connect;

import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLConfigureSNIRequestFactory;
import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLHostnameCheckDisabledRequestFactory;
import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLServerNameIndicationRequestFactory;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MnuboOAuth2Template extends OAuth2Template {

    private final String consumerKey;
    private final String accessTokenUrl;
    private final SSLConfigureSNIRequestFactory sslsniRequestFactory;

    public MnuboOAuth2Template(String consumerKey, String consumerSecret, String authorizeUrl, String accessTokenUrl, final Boolean disableSSLCertificateCheck) {
        super(consumerKey, consumerSecret, authorizeUrl, accessTokenUrl);
        this.consumerKey = consumerKey;
        this.accessTokenUrl = accessTokenUrl;

        setUseParametersForClientAuthentication(false);
        getRestTemplate().setErrorHandler(new MnuboAPIErrorHandler());
        if (disableSSLCertificateCheck) {
            sslsniRequestFactory = new SSLHostnameCheckDisabledRequestFactory();
        } else {
            sslsniRequestFactory = new SSLServerNameIndicationRequestFactory();
        }
        sslsniRequestFactory.configure(getRestTemplate());
    }

    @Override
    public AccessGrant exchangeCredentialsForAccess(String username, String password, MultiValueMap<String, String> additionalParameters) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", consumerKey);
        params.set("username", username);
        params.set("password", password);
        params.set("grant_type", "password");
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }
        return postForAccessGrant(accessTokenUrl, params);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", consumerKey);
        params.set("refresh_token", refreshToken);
        params.set("grant_type", "refresh_token");
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }
        return postForAccessGrant(accessTokenUrl, params);
    }
}
