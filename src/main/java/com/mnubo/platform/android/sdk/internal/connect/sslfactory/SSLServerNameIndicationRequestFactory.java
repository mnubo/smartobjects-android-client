package com.mnubo.platform.android.sdk.internal.connect.sslfactory;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SSLServerNameIndicationRequestFactory implements SSLConfigureSNIRequestFactory {

    private static class SSLWithSNIHttpRequestFactory extends SimpleClientHttpRequestFactory {

        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
            super.prepareConnection(connection, httpMethod);
        }

    }

    @Override
    public void configure(final RestTemplate restTemplate) {
        restTemplate.setRequestFactory(new SSLWithSNIHttpRequestFactory());
    }

}
