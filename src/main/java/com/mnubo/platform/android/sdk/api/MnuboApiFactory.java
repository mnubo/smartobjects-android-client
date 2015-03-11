package com.mnubo.platform.android.sdk.api;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mnubo.platform.android.sdk.BuildConstants;
import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboInvalidHostname;
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

public class MnuboApiFactory {
    private static final String TAG = MnuboApiFactory.class.getName();
    private static final String FILENAME = "mnubo_username.txt";

    private Connection<MnuboClientApi> clientConnection;
    private final MnuboUserConnectionFactory userConnectionFactory;
    private final MnuboClientConnectionFactory clientConnectionFactory;
    private final ConnectionRepository connectionRepository;
    private final Context applicationContext;

    public MnuboApiFactory(Context applicationContext, String hostname, String consumerKey, String consumerSecret) {
        Validate.notNull(applicationContext);
        Validate.notBlank(consumerKey);
        Validate.notBlank(consumerSecret);
        Validate.notBlank(hostname);

        this.applicationContext = applicationContext;

        final String validatedPlatformUrl = buildPlatformUrl(hostname);

        final String authorizeUrl = OAUTH_PATH + "/authorize";
        final String accessTokenUrl = OAUTH_PATH + "/token";

        SQLiteOpenHelper repositoryHelper = new SQLiteConnectionRepositoryHelper(applicationContext);
        ConnectionFactoryRegistry connectionFactoryRegistry = new ConnectionFactoryRegistry();

        this.userConnectionFactory = new MnuboUserConnectionFactory(validatedPlatformUrl, consumerKey, consumerSecret, authorizeUrl, accessTokenUrl, BuildConstants.DISABLE_CERTIFICATE_HOSTNAME_CHECK);
        this.clientConnectionFactory = new MnuboClientConnectionFactory(validatedPlatformUrl, consumerKey, consumerSecret, authorizeUrl, accessTokenUrl, BuildConstants.DISABLE_CERTIFICATE_HOSTNAME_CHECK);

        connectionFactoryRegistry.addConnectionFactory(this.userConnectionFactory);

        this.connectionRepository = new SQLiteConnectionRepository(repositoryHelper,
                connectionFactoryRegistry, AndroidEncryptors.text("password", "5c0744940b5c369b"));
    }

    public MnuboApi getApi() {
        return new MnuboApi(getConnectionOperations(), clientConnection, getUserConnection());
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

    private ConnectionOperations getConnectionOperations() {
        return new ConnectionOperations() {
            @Override
            public Connection<MnuboUserApi> refresh(Connection<MnuboUserApi> userConnection) {
                if (userConnection != null) {
                    userConnection.refresh();
                    connectionRepository.updateConnection(userConnection);

                    Log.v(TAG, Strings.FETCH_REFRESH_USER_TOKEN_SUCCESS);

                    return userConnection;
                }
                return null;
            }

            @Override
            public Connection<MnuboClientApi> getClientConnection() {
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
     * This method is used to save the username locally. It shouldn't
     * be required if the {@link com.mnubo.platform.android.sdk.internal.user.connect.MnuboUserAdapter}
     * has a way to fetch the User information using the user token in the
     * authorization header.
     *
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
     *
     * Read the saved username
     *
     * @return read username
     */
    @Deprecated
    public String readUsername() {
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
     *
     * Deletes the saved username
     */
    @Deprecated
    public void deleteSavedUsername() {
        try {
            boolean fis = this.applicationContext.deleteFile(FILENAME);
        } catch (Exception ex) {
            Log.e(TAG, "Unable to persist username", ex);
        }
    }

    public static interface ConnectionOperations {
        public Connection<MnuboUserApi> refresh(Connection<MnuboUserApi> userConnection);

        public Connection<MnuboClientApi> getClientConnection();

        public Boolean logIn(String username, String password);

        public void logOut();

        public Boolean isUserConnected();

        public String getUsername();
    }

}
