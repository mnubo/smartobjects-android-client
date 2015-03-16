package com.mnubo.platform.android.sdk.internal.client.api;


import com.mnubo.platform.android.sdk.internal.client.services.ClientService;
import com.mnubo.platform.android.sdk.internal.client.services.impl.ClientServiceImpl;
import com.mnubo.platform.android.sdk.internal.connect.MnuboAPIErrorHandler;
import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLHostnameCheckDisabledRequestFactory;
import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLConfigureSNIRequestFactory;
import com.mnubo.platform.android.sdk.internal.connect.sslfactory.SSLServerNameIndicationRequestFactory;

import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class MnuboClientApiImpl extends AbstractOAuth2ApiBinding implements MnuboClientApi {

    private final ClientService clientService;
    private final SSLConfigureSNIRequestFactory sslsniRequestFactory;

    public MnuboClientApiImpl(final String accessToken, final String platformBaseUrl, final Boolean disableSSLCertificateCheck) {
        super(accessToken);

        if (disableSSLCertificateCheck) {
            sslsniRequestFactory = new SSLHostnameCheckDisabledRequestFactory();
        } else {
            sslsniRequestFactory = new SSLServerNameIndicationRequestFactory();
        }
        sslsniRequestFactory.configure(getRestTemplate());

        this.clientService = new ClientServiceImpl(platformBaseUrl, getRestTemplate());

    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new MnuboAPIErrorHandler());
    }


    @Override
    public ClientService clientService() {
        return this.clientService;
    }

}
