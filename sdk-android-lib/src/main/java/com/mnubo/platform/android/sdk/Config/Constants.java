package com.mnubo.platform.android.sdk.Config;

/**
 * Created by marias on 09/10/15.
 */
public interface Constants {
    // Config
    String CONFIG = "client.config.";
    String HOST_NAME = CONFIG + "hostname";
    String PLATFORM_PORT = CONFIG + "platform-port";
    String AUTHENTICATION_PORT = CONFIG + "autentication-port";
    String HTTP_PROTOCOL = CONFIG + "http-protocol";
    String MNUBO_PROVIDER = CONFIG + "mnubo-local-provider";
    String SECUTIRY = "client.security.";
    String SECURITY_CONSUMER_KEY = SECUTIRY + "consumer-key";
    String SECURITY_CONSUMER_SECRET = SECUTIRY + "consumer-secret";
    String CLIENT = "client.http.client.";
    String CLIENT_BASE_PATH = CLIENT + "base-path";

    String OAUTH_PATH = "/oauth";
}
