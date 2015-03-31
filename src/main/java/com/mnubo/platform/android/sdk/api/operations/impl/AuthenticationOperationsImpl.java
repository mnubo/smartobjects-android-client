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

import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newLogInTask;

public class AuthenticationOperationsImpl extends AbstractMnuboOperations implements AuthenticationOperations {

    public AuthenticationOperationsImpl(ConnectionOperations connectionOperations,
                                        Connection<MnuboClientApi> clientConnection,
                                        Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> logIn(String username, String password) {
        final Task<Boolean> task = newLogInTask(username, password, connectionOperations);
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logInAsync(final String username, final String password, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newLogInTask(username, password, connectionOperations);
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOut() {
        connectionOperations.logOut();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isUserConnected() {
        return connectionOperations.isUserConnected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return connectionOperations.getUsername();
    }

}
