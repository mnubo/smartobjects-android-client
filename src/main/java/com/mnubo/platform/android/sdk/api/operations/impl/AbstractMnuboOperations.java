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

package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboClientConnectionUnavailableException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.internal.tasks.Task.ApiFetcher;
import static com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;

public abstract class AbstractMnuboOperations {

    private Connection<MnuboClientApi> clientConnection;
    private Connection<MnuboUserApi> userConnection;

    final ConnectionOperations connectionOperations;

    private boolean offlineCachingEnabled = false;

    AbstractMnuboOperations(ConnectionOperations connectionOperations,
                            Connection<MnuboClientApi> clientConnection,
                            Connection<MnuboUserApi> userConnection) {
        this.connectionOperations = connectionOperations;
        this.userConnection = userConnection;
        this.clientConnection = clientConnection;
    }

    /**
     * Enables or disable offline caching for failed requests.
     *
     * @param offlineCachingEnabled enable if true, disable if false
     */
    public void setOfflineCachingEnabled(boolean offlineCachingEnabled) {
        this.offlineCachingEnabled = offlineCachingEnabled;
    }

    public boolean isOfflineCachingEnabled() {
        return offlineCachingEnabled;
    }

    ConnectionRefresher getUserConnectionRefresher() {
        return new ConnectionRefresher() {
            @Override
            public void refresh() {
                userConnection = connectionOperations.refreshUserConnection(userConnection);
            }
        };
    }

    ConnectionRefresher getClientConnectionRefresher() {
        return new ConnectionRefresher() {
            @Override
            public void refresh() {
                clientConnection = connectionOperations.getNewClientConnection();
            }
        };
    }

    protected ApiFetcher getApiFetcher() {
        return new ApiFetcher() {
            @Override
            public MnuboClientApi getMnuboClientApi() {
                if (clientConnection != null) {
                    return clientConnection.getApi();
                }
                clientConnection = connectionOperations.getNewClientConnection();

                if (clientConnection != null) {
                    return clientConnection.getApi();
                }
                throw new MnuboClientConnectionUnavailableException();
            }

            @Override
            public MnuboUserApi getMnuboUserApi() {
                if (userConnection != null) {
                    return userConnection.getApi();
                }
                throw new MnuboNotLoggedInException();
            }
        };
    }
}
