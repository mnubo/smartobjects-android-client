package com.mnubo.platform.android.sdk.internal.services;


import com.mnubo.platform.android.sdk.internal.connect.query.PlatformPath;
import com.mnubo.platform.android.sdk.internal.connect.query.PlatformQuery;

public class AbstractMnuboService {

    private final String platformBaseUrl;

    private final PlatformPath platformPath;

    public AbstractMnuboService(String platformBaseUrl, PlatformPath platformPath) {
        this.platformBaseUrl = platformBaseUrl;
        this.platformPath = platformPath;
    }

    public PlatformPath getPlatformPath() {
        return platformPath;
    }

    public String getPlatformBaseUrl() {
        return platformBaseUrl;
    }

    public PlatformQuery getQuery() {
        return new PlatformQuery(this.getPlatformBaseUrl(), this.platformPath);
    }
}
