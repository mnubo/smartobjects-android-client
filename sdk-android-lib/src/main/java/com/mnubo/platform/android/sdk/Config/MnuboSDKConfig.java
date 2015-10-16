package com.mnubo.platform.android.sdk.Config;

import org.apache.commons.lang3.Validate;

import static com.mnubo.platform.android.sdk.Config.Constants.AUTHENTICATION_PORT;
import static com.mnubo.platform.android.sdk.Config.Constants.CLIENT_BASE_PATH;
import static com.mnubo.platform.android.sdk.Config.Constants.HOST_NAME;
import static com.mnubo.platform.android.sdk.Config.Constants.HTTP_PROTOCOL;
import static com.mnubo.platform.android.sdk.Config.Constants.MNUBO_PROVIDER;
import static com.mnubo.platform.android.sdk.Config.Constants.PLATFORM_PORT;
import static com.mnubo.platform.android.sdk.Config.Constants.SECURITY_CONSUMER_KEY;
import static com.mnubo.platform.android.sdk.Config.Constants.SECURITY_CONSUMER_SECRET;

/**
 * Created by marias on 09/10/15.
 */
public class MnuboSDKConfig {
    // default values
    private static String  DEFAULT_HTTP_PROTOCOL = "https";
    public static String  DEFAULT_BASE_PATH = "/api/v2";
    private static String  DEFAULT_OAUTH_PATH = "/oauth";
    private static Integer DEFAULT_HOST_PORT = 443;
    private static String  DEFAULT_MNUBO_PROVIDER = "MNUBO_PROVIDER_LOCAL";
    private static int     MAX_PORT_VALUE = 65536;

    // mandatory varibles
    private String hostName;
    private String SecurityConsumerKey;
    private String SecurityConsumerSecret;

    // optional variables
    private int platformPort = DEFAULT_HOST_PORT;
    private int authenticationPort = DEFAULT_HOST_PORT;
    private String httpProtocol = DEFAULT_HTTP_PROTOCOL;
    private String basePath = DEFAULT_BASE_PATH;
    private String mnuboProvider = DEFAULT_MNUBO_PROVIDER;

    public MnuboSDKConfig(String hostName, String securityConsumerKey, String securityConsumerSecret) {
        this.hostName = hostName;
        SecurityConsumerKey = securityConsumerKey;
        SecurityConsumerSecret = securityConsumerSecret;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = parseAsString(hostName, HOST_NAME);
    }

    public String getSecurityConsumerKey() {
        return SecurityConsumerKey;
    }

    public void setSecurityConsumerKey(String securityConsumerKey) {
        SecurityConsumerKey = parseAsString(securityConsumerKey, SECURITY_CONSUMER_KEY);
    }

    public String getSecurityConsumerSecret() {
        return SecurityConsumerSecret;
    }

    public void setSecurityConsumerSecret(String securityConsumerSecret) {
        SecurityConsumerSecret = parseAsString(securityConsumerSecret, SECURITY_CONSUMER_SECRET);
    }

    public int getPlatformPort() {
        return platformPort;
    }

    public void setPlatformPort(String platformPort) {
        this.platformPort = parseAsPort(platformPort, PLATFORM_PORT);
    }

    public int getAuthenticationPort() {
        return authenticationPort;
    }

    public void setAuthenticationPort(String authenticationPort) {
        this.authenticationPort = parseAsPort(authenticationPort, AUTHENTICATION_PORT);
    }

    public String getHttpProtocol() {
        return httpProtocol;
    }

    public void setHttpProtocol(String httpProtocol) {
        this.httpProtocol = parseAsHttpProtocol(httpProtocol);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = parseAsString(basePath, CLIENT_BASE_PATH);
    }

    public String getMnuboProvider() {
        return mnuboProvider;
    }

    public void setMnuboProvider(String mnuboProvider) {
        this.mnuboProvider = parseAsString(mnuboProvider, MNUBO_PROVIDER);
    }

    private String parseAsString( String propertyValue, String property )
    {
        Validate.notBlank( propertyValue, String.format( "%s property is not a valid String." , property ) );
        return propertyValue;
    }

    private int parseAsPort( String propertyValue, String property )
    {
        parseAsString(propertyValue, property);
        Validate.isTrue(!propertyValue.matches( "\\d+" ),
                        String.format( "%s property has to be an Integer positive." , property ));
        int value = Integer.parseInt(propertyValue);
        if ( value > MAX_PORT_VALUE )
        {
            throw new IllegalStateException( String.format( "%s property is not a valid port." , property ) );
        }
        return value;
    }

    private String parseAsHttpProtocol( String httpProtocol )
    {
        parseAsString(httpProtocol, HTTP_PROTOCOL);
        if ( !httpProtocol.equalsIgnoreCase( "http" ) && !httpProtocol.equalsIgnoreCase( "https" ) )
        {
            throw new IllegalStateException( String.format( "%s has to be equal to \"http\" or \"https\"" ,
                    HTTP_PROTOCOL ) );
        }
        return httpProtocol;
    }


}

