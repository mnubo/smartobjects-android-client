package com.mnubo.platform.android.sdk;

public interface BuildConstants {

    /**
     * Protocol used to perform request to the Mnubo API
     */
    String PROTOCOL = "https";

    /**
     * Path used to perform request to the Mnubo API
     */
    String PATH = "/api/v2";

    /**
     * Path used to perform oauth request to the Mnubo API
     */
    String OAUTH_PATH = PATH + "/oauth";

    /**
     * Port used to perform request to the Mnubo API
     */
    Integer PORT = 443;


    /**
     * Provider ID used to persist connection in the SQLite database
     */
    String MNUBO_PROVIDER = "MNUBO_PROVIDER_PRODUCTION";


}
