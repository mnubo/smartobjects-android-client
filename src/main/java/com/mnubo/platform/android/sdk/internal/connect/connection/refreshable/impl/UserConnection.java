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

import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

public class UserConnection implements RefreshableConnection {

    private ConnectionRepository connectionRepository;

    public UserConnection(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    @Override
    public void refresh() {
        Connection<MnuboSDKApi> connection = getConnection();

        connection.refresh();
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
            throw new MnuboNotLoggedInException();
        }
        return connection;
    }
}
