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

import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboConnectionUnavailableException;
import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.MnuboConnectionFactory;

import org.springframework.social.connect.Connection;
import org.springframework.social.oauth2.AccessGrant;

import static com.mnubo.platform.android.sdk.Strings.BUILDING_CLIENT_CONNECTION;
import static com.mnubo.platform.android.sdk.Strings.CLIENT_CONNECTION_API;
import static com.mnubo.platform.android.sdk.Strings.CLIENT_CONNECTION_UNAVAILABLE;
import static com.mnubo.platform.android.sdk.Strings.FETCH_CLIENT_TOKEN;
import static com.mnubo.platform.android.sdk.Strings.FETCH_CLIENT_TOKEN_SUCCESS;

/**
 * Client connection is not persisted. Since it has a short life cycle, it is build when needed.
 */
public class ClientConnection extends AbstractMnuboConnection {

    private Connection<MnuboSDKApi> connection;

    public ClientConnection(MnuboConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    private Connection<MnuboSDKApi> createConnection() {

        Log.d(TAG, FETCH_CLIENT_TOKEN);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().authenticateClient();
        Log.d(TAG, FETCH_CLIENT_TOKEN_SUCCESS);

        return connectionFactory.createConnection(accessGrant);
    }

    @Override
    public void refresh() {
        connection = createConnection();
    }

    @Override
    public MnuboSDKApi getMnuboSDKApi() {
        if (connection == null) {
            Log.d(TAG, BUILDING_CLIENT_CONNECTION);
            connection = createConnection();
        }
        if (connection != null) {
            Log.d(TAG, CLIENT_CONNECTION_API);
            return connection.getApi();
        }

        Log.e(TAG, CLIENT_CONNECTION_UNAVAILABLE);
        throw new MnuboConnectionUnavailableException();
    }
}
