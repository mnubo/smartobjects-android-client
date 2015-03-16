package com.mnubo.platform.android.sdk.internal.connect;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MnuboOAuth2Template extends OAuth2Template {

    private final String consumerKey;
    private final String accessTokenUrl;

    public MnuboOAuth2Template(String consumerKey, String consumerSecret, String authorizeUrl, String accessTokenUrl, final Boolean disableSSLCertificateCheck) {
        super(consumerKey, consumerSecret, authorizeUrl, accessTokenUrl);
        this.consumerKey = consumerKey;
        this.accessTokenUrl = accessTokenUrl;

        setUseParametersForClientAuthentication(false);
        getRestTemplate().setErrorHandler(new MnuboAPIErrorHandler());
        //Force the use of SNI to fetch the proper certificate
        getRestTemplate().setRequestFactory(new SimpleClientHttpRequestFactory());
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
