package com.mnubo.platform.android.sdk;

public interface BuildConstants {

    /**
     * Protocol used to perform request to the Mnubo API
     */
    String PROTOCOL = "https";

    /**
     * Path used to perform request to the Mnubo API
     */
    String PATH = "/api/v3";

    /**
     * Path used to perform oauth request to the Mnubo API
     */
    String OAUTH_PATH = PATH + "/oauth";

    /**
     * Port used to perform request to the Mnubo API
     */
    Integer PORT = 443;

    /**
     * Disable the SSL Certificate Hostname check
     */
    Boolean DISABLE_CERTIFICATE_HOSTNAME_CHECK = false;

    /**
     * Provider ID used to persist connection in the SQLite database
     */
    String MNUBO_PROVIDER = "MNUBO_PROVIDER_STAGING";


}
