package com.mnubo.platform.android.sdk.internal.client.api;


import com.mnubo.platform.android.sdk.internal.client.services.ClientService;
import com.mnubo.platform.android.sdk.internal.client.services.impl.ClientServiceImpl;
import com.mnubo.platform.android.sdk.internal.connect.MnuboAPIErrorHandler;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class MnuboClientApiImpl extends AbstractOAuth2ApiBinding implements MnuboClientApi {

    private final ClientService clientService;

    public MnuboClientApiImpl(final String accessToken, final String platformBaseUrl, final Boolean disableSSLCertificateCheck) {
        super(accessToken);

        this.clientService = new ClientServiceImpl(platformBaseUrl, getRestTemplate());

    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new MnuboAPIErrorHandler());
        //Force the use of SNI to fetch the proper certificate
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    }


    @Override
    public ClientService clientService() {
        return this.clientService;
    }

}
