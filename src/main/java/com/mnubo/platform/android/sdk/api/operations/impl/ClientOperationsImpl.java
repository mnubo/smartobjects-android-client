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

import com.mnubo.platform.android.sdk.api.operations.ClientOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmPasswordResetTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmUserCreationTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.CreateUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ResetPasswordTask;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class ClientOperationsImpl extends AbstractMnuboOperations implements ClientOperations {

    public ClientOperationsImpl(ConnectionOperations connectionOperations,
                                Connection<MnuboClientApi> clientConnection,
                                Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUser(final User user, final CompletionCallBack<Boolean> completionCallBack) {
        final CreateUserTask task = new CreateUserTask(getApiFetcher(), user, getClientConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmUserCreation(final String username, final UserConfirmation confirmation, final CompletionCallBack<Boolean> completionCallBack) {
        final ConfirmUserCreationTask task = new ConfirmUserCreationTask(getApiFetcher(), username, confirmation, getClientConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(final String username, final CompletionCallBack<Boolean> completionCallBack) {
        final ResetPasswordTask task = new ResetPasswordTask(getApiFetcher(), username, getClientConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmPasswordReset(final String username, final ResetPassword resetPassword, final CompletionCallBack<Boolean> completionCallBack) {
        final ConfirmPasswordResetTask task = new ConfirmPasswordResetTask(getApiFetcher(), username, resetPassword, getClientConnectionRefresher());
        task.executeAsync(completionCallBack);
    }
}
