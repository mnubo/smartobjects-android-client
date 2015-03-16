package com.mnubo.platform.android.sdk.internal.connect.sslfactory;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SSLHostnameCheckDisabledRequestFactory implements SSLConfigureSNIRequestFactory {

    private static class SSLHostnameCheckDisabledClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

        private final HostnameVerifier verifier;

        public SSLHostnameCheckDisabledClientHttpRequestFactory(HostnameVerifier verifier) {
            this.verifier = verifier;
        }

        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
            }
            super.prepareConnection(connection, httpMethod);
        }

    }

    private static class NullHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    @Override
    public void configure(final RestTemplate restTemplate) {
        HostnameVerifier verifier = new NullHostnameVerifier();
        SSLHostnameCheckDisabledClientHttpRequestFactory factory = new SSLHostnameCheckDisabledClientHttpRequestFactory(verifier);
        restTemplate.setRequestFactory(factory);
    }
}
