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

import static com.mnubo.platform.android.sdk.BuildConstants.OAUTH_PATH;
import static com.mnubo.platform.android.sdk.BuildConstants.PORT;
import static com.mnubo.platform.android.sdk.BuildConstants.PROTOCOL;
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

    private static Mnubo instance = null;

    private Mnubo(Context applicationContext, String consumerKey, String consumerSecret, String hostname, File dataStoreDirectory) {
        Validate.notNull(applicationContext, "The application context must be provided");
        Validate.notBlank(consumerKey, "The consumer key cannot be null or empty");
        Validate.notBlank(consumerSecret, "The consumer secret cannot be null or empty");
        Validate.notBlank(hostname, "The hostname cannot be null or empty");

        final String validatedPlatformUrl = buildPlatformUrl(hostname);

        final String authorizeUrl = validatedPlatformUrl + OAUTH_PATH + "/authorize";
        final String accessTokenUrl = validatedPlatformUrl + OAUTH_PATH + "/token";

        SQLiteOpenHelper repositoryHelper = new SQLiteConnectionRepositoryHelper(applicationContext);
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();

        MnuboConnectionFactory connectionFactory = new MnuboConnectionFactory(validatedPlatformUrl, consumerKey, consumerSecret, authorizeUrl, accessTokenUrl);

        connectionFactoryRegistry.addConnectionFactory(connectionFactory);

        ConnectionRepository connectionRepository = new SQLiteConnectionRepository(repositoryHelper,
                connectionFactoryRegistry, AndroidEncryptors.text(consumerSecret, "5c0744940b5c369b"));

        this.mnuboConnectionManager = new MnuboConnectionManager(connectionFactory, connectionRepository);
        this.mnuboDataStore = new MnuboFileDataStore(dataStoreDirectory != null ? dataStoreDirectory : applicationContext.getCacheDir());
        this.mnuboBufferService = new MnuboBufferServiceImpl(this.mnuboDataStore, this.mnuboConnectionManager);

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
     * @see #init(android.content.Context, String, String, String, java.io.File)
     */
    public static void init(Context context,
                            String consumerKey, String consumerSecret, String hostname) {
        init(context, consumerKey, consumerSecret, hostname, null);
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
    public static void init(Context context,
                            String consumerKey, String consumerSecret, String hostname, File dataStoreDirectory) {
        if (instance != null) {
            throw new MnuboAlreadyInitializedException();
        }

        instance = new Mnubo(context, consumerKey, consumerSecret, hostname, dataStoreDirectory);

        Log.d(TAG, SDK_INITIALIZED);
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

    private String buildPlatformUrl(final String hostname) {
        try {
            Log.d(TAG, String.format(SDK_URL_DEBUG, PROTOCOL, hostname, PORT));
            return new URL(PROTOCOL, hostname, PORT, "").toString();
        } catch (MalformedURLException muex) {
            throw new MnuboInvalidHostname(muex);
        }
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
