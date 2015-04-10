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

import android.util.Log;

import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.ClientConnection;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.impl.UserConnection;

import org.springframework.social.connect.ConnectionRepository;

import static com.mnubo.platform.android.sdk.Strings.CLIENT_CONNECTION_AVAILABLE;
import static com.mnubo.platform.android.sdk.Strings.USER_CONNECTION_AVAILABLE;
import static com.mnubo.platform.android.sdk.Strings.USER_LOG_IN;
import static com.mnubo.platform.android.sdk.Strings.USER_LOG_OUT;

public class MnuboConnectionManager {

    private static final String TAG = MnuboConnectionManager.class.getName();

    @Deprecated
    private String username = null;

    private final ClientConnection clientConnection;
    private final UserConnection userConnection;

    public MnuboConnectionManager(MnuboConnectionFactory connectionFactory,
                                  ConnectionRepository connectionRepository) {

        this.clientConnection = new ClientConnection(connectionFactory);
        this.userConnection = new UserConnection(connectionRepository, connectionFactory);
    }

    public RefreshableConnection getCurrentConnection() {
        if (isUserConnected()) {
            Log.d(TAG, USER_CONNECTION_AVAILABLE);
            return userConnection;
        } else {
            Log.d(TAG, CLIENT_CONNECTION_AVAILABLE);
            return clientConnection;
        }
    }

    public void logOut() {
        Log.d(TAG, USER_LOG_OUT);

        this.username = null;
        this.userConnection.logOut();
    }

    public Boolean logIn(String username, String password) {
        Log.d(TAG, String.format(USER_LOG_IN, username));
        this.username = username;
        this.userConnection.logIn(username, password);
        return true;
    }

    public Boolean isUserConnected() {
        return userConnection.isUserConnected();
    }


    public String getUsername() {
        if (isUserConnected()) {
            return userConnection.getUsername();
        } else {
            return this.username;
        }
    }

}
