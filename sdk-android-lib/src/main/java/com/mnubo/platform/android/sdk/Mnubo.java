/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mnubo.platform.android.sdk.Config.*;
import com.mnubo.platform.android.sdk.Config.Constants;
import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService;
import com.mnubo.platform.android.sdk.api.services.buffer.impl.MnuboBufferServiceImpl;
import com.mnubo.platform.android.sdk.api.store.MnuboDataStore;
import com.mnubo.platform.android.sdk.api.store.impl.MnuboFileDataStore;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboAlreadyInitializedException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboInvalidHostname;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotInitializedException;
import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;

import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import static com.mnubo.platform.android.sdk.Config.Constants.AUTHENTICATION_PORT;
import static com.mnubo.platform.android.sdk.Config.Constants.CLIENT_BASE_PATH;
import static com.mnubo.platform.android.sdk.Config.Constants.HOST_NAME;
import static com.mnubo.platform.android.sdk.Config.Constants.HTTP_PROTOCOL;
import static com.mnubo.platform.android.sdk.Config.Constants.MNUBO_PROVIDER;
import static com.mnubo.platform.android.sdk.Config.Constants.OAUTH_PATH;
import static com.mnubo.platform.android.sdk.Config.Constants.PLATFORM_PORT;
import static com.mnubo.platform.android.sdk.Config.Constants.SECURITY_CONSUMER_KEY;
import static com.mnubo.platform.android.sdk.Config.Constants.SECURITY_CONSUMER_SECRET;
import static com.mnubo.platform.android.sdk.Strings.SDK_ENABLE_BUFFER_SERVICE;
import static com.mnubo.platform.android.sdk.Strings.SDK_INITIALIZED;
import static com.mnubo.platform.android.sdk.Strings.SDK_URL_DEBUG;

/**
 * This class is the entry point to use the SDK. The init method must be called prior to anything
 * else.
 */
public class Mnubo {

    private static final String TAG = Mnubo.class.getName();

    private final MnuboConnectionManager mnuboConnectionManager;
    private final MnuboBufferService mnuboBufferService;
    private final MnuboDataStore mnuboDataStore;
    private final MnuboSDKConfig config;

    private static Mnubo instance = null;

    private Mnubo(Context applicationContext, MnuboSDKConfig config, File dataStoreDirectory) {
        Validate.notNull(applicationContext, "The application context must be provided");

        this.config = config;
        final String validatedPlatformUrl = buildPlatformUrl();
        final String validatedAuthUrl = buildPlatformUrl();

        final String authorizeUrl = validatedAuthUrl + OAUTH_PATH + "/authorize";
        final String accessTokenUrl = validatedAuthUrl + OAUTH_PATH + "/token";

        SQLiteOpenHelper repositoryHelper = new SQLiteConnectionRepositoryHelper(applicationContext);
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();

        MnuboConnectionFactory connectionFactory =
                new MnuboConnectionFactory(validatedPlatformUrl, config, authorizeUrl, accessTokenUrl);

        connectionFactoryRegistry.addConnectionFactory(connectionFactory);

        ConnectionRepository connectionRepository = new SQLiteConnectionRepository(repositoryHelper,
                connectionFactoryRegistry, AndroidEncryptors.text(config.getSecurityConsumerSecret(),
                                                                  "5c0744940b5c369b"));

        this.mnuboConnectionManager = new MnuboConnectionManager(connectionFactory, connectionRepository);
        this.mnuboDataStore = new MnuboFileDataStore(dataStoreDirectory != null ? dataStoreDirectory : applicationContext.getCacheDir());
        this.mnuboBufferService = new MnuboBufferServiceImpl(this.mnuboDataStore, this.mnuboConnectionManager);

    }

    /**
     * This method is used to initialise the SDK, using a properties file with advanced configuration.
     *
     * @param context        Android context of the application using the SDK
     * @param properties     properties: property instance.
     * @see #init(android.content.Context, java.util.Properties, java.io.File)
     */
    public static void init(Context context, Properties properties ) {
        init(context, buildSdkConfig(properties), null);
    }

    /**
     * This method is used to initialise the SDK, using a properties file with advanced configuration.
     * It uses the directory you specify as the working directory for the data store.
     *
     * This is the list of parameters to set:
     *      <b>client.config.hostname</b>:             mnubo's server name. For example:
     *                                                 "rest.sandbox.mnubo.com".
     *      <b>client.security.consumer-key</b>:       The unique client identity key.
     *      <b>client.security.consumer-secret</b>:    The secret key which is used in conjunction
     *                                                 with the consumer key to access the mnubo server.
     *      <b>client.config.platform-port</b>:        mnubo's server port.
     *      <b>client.config.autentication-port</b>:   Authentication server port.
     *      <b>client.http.client.base-path</b>:       This is the base common path in all request.
     *      <b>client.config.http-protocol</b>:        Use "http" for unsecure connection and "https"
     *                                                 for secure connections.
     *      <b>client.config.mnubo-local-provider</b>: Mnubo local provider.
     *
     * @param context            Android context of the application using the SDK
     * @param properties         properties: property instance.
     * @param dataStoreDirectory The directory where you want the data store to work in
     */
    public static void init(Context context, Properties properties, File dataStoreDirectory) {
        init(context, buildSdkConfig(properties), dataStoreDirectory);
    }

    /**
     * This method is used to initialise the SDK. Call it once, before you do anything else with the
     * SDK. It uses the application cache directory as the working directory for the data store.
     *
     * @param context        Android context of the application using the SDK
     * @param hostname       Hostname where your Mnubo services are available ( Eg :
     *                       mycompany.api.mnubo.com)
     * @param consumerKey    The consumer key provided by Mnubo
     * @param consumerSecret The consumer secret that came along the key given above
     */
    public static void init(Context context, String consumerKey, String consumerSecret,
                            String hostname) {
        init(context, new MnuboSDKConfig(hostname, consumerKey, consumerSecret), null);
    }

    /**
     * This method is used to initialise the SDK. Call it once, before you do anything else with the
     * SDK. It uses the directory you specify as the working directory for the data store.
     *
     * @param context            Android context of the application using the SDK
     * @param hostname           Hostname where your Mnubo services are available ( Eg :
     *                           mycompany.api.mnubo.com)
     * @param consumerKey        The consumer key provided by Mnubo
     * @param consumerSecret     The consumer secret that came along the key given above
     * @param dataStoreDirectory The directory where you want the data store to work in
     */
    public static void init(Context context, String consumerKey, String consumerSecret,
                            String hostname, File dataStoreDirectory) {
        init(context, new MnuboSDKConfig(hostname, consumerKey, consumerSecret), dataStoreDirectory);
    }

    /**
     * Returns a MnuboApi instance to be used to perform calls against the Mnubo system.
     *
     * @return an instance of {@link com.mnubo.platform.android.sdk.api.MnuboApi}
     */
    public static MnuboApi getApi() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return new MnuboApi(
                instance.mnuboConnectionManager,
                instance.mnuboBufferService
        );
    }

    /**
     * This call enables the {@link com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService}.
     * The {@link com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService} persists the
     * data of the request that fails.
     */
    public static void enableBufferService() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }

        instance.mnuboBufferService.setEnabled(true);

        Log.d(TAG, SDK_ENABLE_BUFFER_SERVICE);
    }

    /**
     * Gives you the current instance of the {@link com.mnubo.platform.android.sdk.api.store.MnuboDataStore}.
     * The current implementation writes to disk.
     *
     * @return current MnuboDataStore instance
     */
    public static MnuboDataStore getDataStore() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }

        return instance.mnuboDataStore;
    }

    /**
     * Gives you the current instance of the {@link com.mnubo.platform.android.sdk.api.store.MnuboDataStore}.
     * The current implementation writes to disk.
     *
     * @return current MnuboDataStore instance
     */
    public static MnuboBufferService getBufferService() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }

        return instance.mnuboBufferService;
    }

    private String buildPlatformUrl() {
        try {
            Log.d(TAG, String.format(SDK_URL_DEBUG, config.getHttpProtocol(), config.getHostName(),
                    config.getPlatformPort()));
            return new URL(config.getHttpProtocol(), config.getHostName(), config.getPlatformPort(),
                           "").toString();
        } catch (MalformedURLException muex) {
            throw new MnuboInvalidHostname(muex);
        }
    }

    private String buildAuthUrl() {
        try {
            Log.d(TAG, String.format(SDK_URL_DEBUG, config.getHttpProtocol(), config.getHostName(),
                                     config.getAuthenticationPort()));
            return new URL(config.getHttpProtocol(), config.getHostName(),
                           config.getAuthenticationPort(), "").toString();
        } catch (MalformedURLException muex) {
            throw new MnuboInvalidHostname(muex);
        }
    }

    private static void init(Context context, MnuboSDKConfig config, File dataStoreDirectory) {
        if (instance != null) {
            throw new MnuboAlreadyInitializedException();
        }

        Validate.notNull(config);

        instance = new Mnubo(context, config, dataStoreDirectory);

        Log.d(TAG, SDK_INITIALIZED);
    }

    private static MnuboSDKConfig buildSdkConfig(Properties properties) {
        Validate.notNull(properties);

        String name = "";
        String sk = "";
        String ss = "";

        if (properties.containsKey(HOST_NAME)) {
            name = properties.getProperty(HOST_NAME);
        }
        if (properties.containsKey(SECURITY_CONSUMER_KEY)) {
            sk = properties.getProperty(SECURITY_CONSUMER_KEY);
        }
        if (properties.containsKey(SECURITY_CONSUMER_SECRET)) {
            ss = properties.getProperty(SECURITY_CONSUMER_SECRET);
        }

        MnuboSDKConfig config = new MnuboSDKConfig(name, sk, ss);

        if (properties.containsKey(PLATFORM_PORT)) {
            config.setPlatformPort(properties.getProperty(PLATFORM_PORT));
        }
        if (properties.containsKey(AUTHENTICATION_PORT)) {
            config.setAuthenticationPort(properties.getProperty(AUTHENTICATION_PORT));
        }
        if (properties.containsKey(HTTP_PROTOCOL)) {
            config.setHttpProtocol(properties.getProperty(HTTP_PROTOCOL));
        }
        if (properties.containsKey(CLIENT_BASE_PATH)) {
            config.setBasePath(properties.getProperty(CLIENT_BASE_PATH));
        }
        if (properties.containsKey(MNUBO_PROVIDER)) {
            config.setMnuboProvider(properties.getProperty(MNUBO_PROVIDER));
        }

        return config;
    }

    /**
     * Returns the last logged in username;
     *
     * @return the last username
     */
    @Deprecated
    public static String getUsername() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.mnuboConnectionManager.getUsername();
    }


}
