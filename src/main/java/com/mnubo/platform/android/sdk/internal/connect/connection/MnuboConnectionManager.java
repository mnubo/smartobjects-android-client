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

package com.mnubo.platform.android.sdk.internal.connect.connection;

import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.ClientConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.UserConnection;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.oauth2.AccessGrant;

public class MnuboConnectionManager {

    private static final String TAG = MnuboConnectionManager.class.getName();

    @Deprecated
    private String username = null;


    private final ConnectionRepository connectionRepository;
    private final MnuboConnectionFactory connectionFactory;

    private ClientConnection clientConnection;
    private UserConnection userConnection;

    public MnuboConnectionManager(MnuboConnectionFactory connectionFactory,
                                  ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
        this.connectionFactory = connectionFactory;

        this.clientConnection = new ClientConnection(connectionFactory);
        activateUserConnectionIfAvailable();
    }

    public RefreshableConnection getCurrentConnection() {
        if (userConnection != null) {
            return userConnection;
        }
        return clientConnection;
    }

    private void activateUserConnectionIfAvailable() {
        if (this.connectionRepository.findPrimaryConnection(MnuboSDKApi.class) != null) {
            userConnection = new UserConnection(connectionRepository);
        }
    }

    public void logOut() {
        this.userConnection = null;
        this.username = null;
        this.connectionRepository.removeConnections(connectionFactory.getProviderId());

    }

    public Boolean logIn(String username, String password) {
        this.logOut();

        this.createUserConnection(username, password);

        return true;
    }

    public Boolean isUserConnected() {
        return userConnection != null;
    }


    public String getUsername() {
        if (isUserConnected()) {
            return userConnection.getUsername();
        }
        return this.username;
    }


    private void createUserConnection(String username, String password) {

        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeCredentialsForAccess(username, password, null);

        this.username = username;

        Connection<MnuboSDKApi> connection = connectionFactory.createConnection(accessGrant);

        this.connectionRepository.addConnection(connection);

        this.userConnection = new UserConnection(this.connectionRepository);
    }

}
