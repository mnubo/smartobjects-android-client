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

package com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth2.AccessGrant;

import static com.mnubo.platform.android.sdk.Strings.FETCH_REFRESH_USER_TOKEN;
import static com.mnubo.platform.android.sdk.Strings.FETCH_REFRESH_USER_TOKEN_SUCCESS;
import static com.mnubo.platform.android.sdk.Strings.FETCH_USER_TOKEN;
import static com.mnubo.platform.android.sdk.Strings.FETCH_USER_TOKEN_SUCCESS;
import static com.mnubo.platform.android.sdk.Strings.PERSIST_USER_TOKEN;
import static com.mnubo.platform.android.sdk.Strings.REMOVE_PERSISTED_USER_CONNECTION;
import static com.mnubo.platform.android.sdk.Strings.UPDATE_PERSISTED_USER_TOKEN;
import static com.mnubo.platform.android.sdk.Strings.USER_CONNECTION_UNAVAILABLE;

public class UserConnection extends AbstractMnuboConnection {

    private final ConnectionRepository connectionRepository;

    public UserConnection(ConnectionRepository connectionRepository, MnuboConnectionFactory connectionFactory) {
        super(connectionFactory);
        this.connectionRepository = connectionRepository;
    }

    @Override
    public void refresh() {
        Connection<MnuboSDKApi> connection = getConnection();

        Log.d(TAG, FETCH_REFRESH_USER_TOKEN);
        connection.refresh();
        Log.d(TAG, FETCH_REFRESH_USER_TOKEN_SUCCESS);

        Log.d(TAG, UPDATE_PERSISTED_USER_TOKEN);
        connectionRepository.updateConnection(connection);
    }

    @Override
    public MnuboSDKApi getMnuboSDKApi() {
        return getConnection().getApi();
    }

    public String getUsername() {
        return getConnection().getKey().getProviderUserId();
    }

    private Connection<MnuboSDKApi> getConnection() {
        Connection<MnuboSDKApi> connection = this.connectionRepository.findPrimaryConnection(MnuboSDKApi.class);
        if (connection == null) {
            Log.e(TAG, USER_CONNECTION_UNAVAILABLE);
            throw new MnuboNotLoggedInException();
        }
        return connection;
    }

    public boolean isUserConnected() {
        return this.connectionRepository.findPrimaryConnection(MnuboSDKApi.class) != null;
    }

    public void logOut() {
        Log.d(TAG, REMOVE_PERSISTED_USER_CONNECTION);
        this.connectionRepository.removeConnections(connectionFactory.getProviderId());
    }

    public void logIn(String username, String password) {
        this.logOut();
        this.createUserConnection(username, password);
    }

    private void createUserConnection(String username, String password) {

        Log.d(TAG, FETCH_USER_TOKEN);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeCredentialsForAccess(username, password, null);
        Log.d(TAG, FETCH_USER_TOKEN_SUCCESS);

        Connection<MnuboSDKApi> connection = connectionFactory.createConnection(accessGrant);

        Log.d(TAG, PERSIST_USER_TOKEN);
        this.connectionRepository.addConnection(connection);
    }
}
