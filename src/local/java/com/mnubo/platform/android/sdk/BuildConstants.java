package com.mnubo.platform.android.sdk;

/**
 * Constants that changes according to the flavor used.
 */
public interface BuildConstants {

    /**
     * Protocol used to perform request to the Mnubo API
     */
    String PROTOCOL = "http";

    /**
     * Path used to perform request to the Mnubo API
     */
    String PATH = "/rest";

    /**
     * Path used to perform oauth request to the Mnubo API
     */
    String OAUTH_PATH = "/oauth";

    /**
     * Port used to perform request to the Mnubo API
     */
    Integer PORT = 8081;

    /**
     * Disable the SSL Certificate Hostname check
     */
    Boolean DISABLE_CERTIFICATE_HOSTNAME_CHECK = false;


    /**
     * Provider ID used to persist connection in the SQLite database
     */
    String MNUBO_PROVIDER = "MNUBO_PROVIDER_LOCAL";


}
