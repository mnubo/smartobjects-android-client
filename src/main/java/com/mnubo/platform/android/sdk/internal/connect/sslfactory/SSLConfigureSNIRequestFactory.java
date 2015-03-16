package com.mnubo.platform.android.sdk.internal.connect.sslfactory;

import org.springframework.web.client.RestTemplate;

public interface SSLConfigureSNIRequestFactory {

    void configure(final RestTemplate restTemplate);
}
