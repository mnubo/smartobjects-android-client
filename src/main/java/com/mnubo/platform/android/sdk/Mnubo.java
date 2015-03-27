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
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboAlreadyInitializedException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboInvalidHostname;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotInitializedException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.client.connect.MnuboClientConnectionFactory;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.internal.user.connect.MnuboUserConnectionFactory;

import org.apache.commons.lang3.Validate;
import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.oauth2.AccessGrant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mnubo.platform.android.sdk.BuildConstants.OAUTH_PATH;
import static com.mnubo.platform.android.sdk.BuildConstants.PORT;
import static com.mnubo.platform.android.sdk.BuildConstants.PROTOCOL;

/**
 * This class is the entry point to use the SDK. The init method must be called prior
 * to anything else.
 */
public class Mnubo {


    private static final String TAG = Mnubo.class.getName();
    private static final String FILENAME = "mnubo_username.txt";

    private Connection<MnuboClientApi> clientConnection;
    private final MnuboUserConnectionFactory userConnectionFactory;
    private final MnuboClientConnectionFactory clientConnectionFactory;
    private final ConnectionRepository connectionRepository;
    private final Context applicationContext;
    private final ConnectionOperations connectionOperations;

    private static Mnubo instance = null;

    private Mnubo(Context applicationContext, String consumerKey, String consumerSecret, String hostname) {
        Validate.notNull(applicationContext, "The application context must be provided");
        Validate.notBlank(consumerKey, "The consumer key cannot be null or empty");
        Validate.notBlank(consumerSecret, "The consumer secret cannot be null or empty");
        Validate.notBlank(hostname, "The hostname cannot be null or empty");

        this.applicationContext = applicationContext;

        final String validatedPlatformUrl = buildPlatformUrl(hostname);

        final String authorizeUrl = OAUTH_PATH + "/authorize";
        final String accessTokenUrl = OAUTH_PATH + "/token";

        SQLiteOpenHelper repositoryHelper = new SQLiteConnectionRepositoryHelper(applicationContext);
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();

        this.userConnectionFactory = new MnuboUserConnectionFactory(validatedPlatformUrl, consumerKey, consumerSecret, authorizeUrl, accessTokenUrl);
        this.clientConnectionFactory = new MnuboClientConnectionFactory(validatedPlatformUrl, consumerKey, consumerSecret, authorizeUrl, accessTokenUrl);

        connectionFactoryRegistry.addConnectionFactory(this.userConnectionFactory);

        this.connectionRepository = new SQLiteConnectionRepository(repositoryHelper,
                connectionFactoryRegistry, AndroidEncryptors.text(consumerSecret, "5c0744940b5c369b"));

        connectionOperations = newConnectionOperations();
    }

    /**
     * This method is used to initialise the SDK. Call it once, before you do anything
     * else with the SDK.
     *
     * @param context        Android context of the application using the SDK
     * @param hostname       Hostname where your Mnubo services are available
     *                       ( Eg : mycompany.api.mnubo.com)
     * @param consumerKey    The consumer key provided by Mnubo
     * @param consumerSecret The consumer secret that came along the key given above
     */
    public static void init(Context context,
                            String consumerKey, String consumerSecret, String hostname) {
        if (instance != null) {
            throw new MnuboAlreadyInitializedException();
        }

        instance = new Mnubo(context, consumerKey, consumerSecret, hostname);
    }

    /**
     * Returns a MnuboApi instance to be used to perform calls against the Mnubo
     * system.
     *
     * @return an instance of MnuboApi
     */
    public static MnuboApi getApi() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return new MnuboApi(
                instance.connectionOperations,
                instance.clientConnection,
                instance.getUserConnection(),
                instance.applicationContext.getCacheDir()
        );
    }

    private Connection<MnuboUserApi> getUserConnection() {
        return this.connectionRepository.findPrimaryConnection(MnuboUserApi.class);
    }

    private Connection<MnuboClientApi> createClientConnection() {

        AccessGrant accessGrant = userConnectionFactory.getOAuthOperations().authenticateClient();

        Log.v(TAG, Strings.FETCH_CLIENT_TOKEN_SUCCESS);

        return clientConnectionFactory.createConnection(accessGrant);
    }

    private Connection<MnuboUserApi> createUserConnection(String username, String password) {
        AccessGrant accessGrant = userConnectionFactory.getOAuthOperations().exchangeCredentialsForAccess(username, password, null);

        Log.v(TAG, Strings.FETCH_USER_TOKEN_SUCCESS);

        return userConnectionFactory.createConnection(accessGrant);
    }

    private ConnectionOperations newConnectionOperations() {
        return new ConnectionOperations() {
            @Override
            public Connection<MnuboUserApi> refreshUserConnection(Connection<MnuboUserApi> userConnection) {
                if (userConnection != null) {
                    userConnection.refresh();
                    connectionRepository.updateConnection(userConnection);

                    Log.v(TAG, Strings.FETCH_REFRESH_USER_TOKEN_SUCCESS);

                    return userConnection;
                }
                return null;
            }

            @Override
            public Connection<MnuboClientApi> getNewClientConnection() {
                clientConnection = createClientConnection();

                return clientConnection;
            }

            @Override
            public Boolean logIn(String username, String password) {
                connectionRepository.removeConnections(userConnectionFactory.getProviderId());

                saveUsername(username);

                Connection<MnuboUserApi> userConnection = createUserConnection(username, password);

                connectionRepository.addConnection(userConnection);

                return true;
            }

            @Override
            public void logOut() {
                connectionRepository.removeConnections(userConnectionFactory.getProviderId());
                deleteSavedUsername();

            }

            @Override
            public Boolean isUserConnected() {
                return getUserConnection() != null;
            }

            @Override
            public String getUsername() {
                return getUserConnection().getKey().getProviderUserId();

            }
        };
    }

    private String buildPlatformUrl(final String hostname) {
        try {
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
    public static String getUsername() {
        if (instance == null) {
            throw new MnuboNotInitializedException();
        }
        return instance.readUsername();
    }

    /**
     * This method is used to save the username locally. It shouldn't
     * be required if the {@link com.mnubo.platform.android.sdk.internal.user.connect.MnuboUserAdapter}
     * has a way to fetch the User information using the user token in the
     * authorization header.
     * <p/>
     * If the username can't be persisted or fetched from the platform
     * the connection can't be saved locally.
     *
     * @param username username to save
     */
    @Deprecated
    private void saveUsername(final String username) {

        try {
            FileOutputStream fos = this.applicationContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(username.getBytes());
            fos.close();
        } catch (Exception ex) {
            Log.e(TAG, "Unable to persist username", ex);
        }
    }

    /**
     * This method is used to save the username locally. It shouldn't
     * be required if the {@link com.mnubo.platform.android.sdk.internal.user.connect.MnuboUserAdapter}
     * has a way to fetch the User information using the user token in the
     * authorization header.
     * <p/>
     * Read the saved username
     *
     * @return read username
     */
    @Deprecated
    private String readUsername() {
        String username = null;

        try {
            FileInputStream fis = this.applicationContext.openFileInput(FILENAME);
            username = "";
            int c;
            while ((c = fis.read()) != -1) {
                username = username + Character.toString((char) c);
            }
            fis.close();
        } catch (Exception ex) {
            Log.e(TAG, "Unable to persist username", ex);
        }
        return username;
    }

    /**
     * This method is used to save the username locally. It shouldn't
     * be required if the {@link com.mnubo.platform.android.sdk.internal.user.connect.MnuboUserAdapter}
     * has a way to fetch the User information using the user token in the
     * authorization header.
     * <p/>
     * Deletes the saved username
     */
    @Deprecated
    private void deleteSavedUsername() {
        boolean deleted = this.applicationContext.deleteFile(FILENAME);

        if (!deleted) {
            Log.e(TAG, "Unable to delete username file.");
        }
    }


    public static interface ConnectionOperations {
        public Connection<MnuboUserApi> refreshUserConnection(Connection<MnuboUserApi> userConnection);

        public Connection<MnuboClientApi> getNewClientConnection();

        public Boolean logIn(String username, String password);

        public void logOut();

        public Boolean isUserConnected();

        public String getUsername();
    }


}
