package com.mnubo.platform.android.sdk.internal.services;


import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;

import org.springframework.web.client.RestTemplate;

public class AbstractMnuboService {

    private final String platformBaseUrl;

    private final PlatformPath platformPath;

    private final RestTemplate restTemplate;

    public AbstractMnuboService(String platformBaseUrl, PlatformPath platformPath, RestTemplate restTemplate) {
        this.platformBaseUrl = platformBaseUrl;
        this.platformPath = platformPath;
        this.restTemplate = restTemplate;
    }

    protected String getPlatformBaseUrl() {
        return platformBaseUrl;
    }

    protected PlatformQuery getQuery() {
        return new PlatformQuery(this.getPlatformBaseUrl(), this.platformPath);
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
