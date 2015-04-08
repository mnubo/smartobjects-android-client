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
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newConfirmPasswordResetTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newConfirmUserCreationTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newCreateUserTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newResetPasswordTask;

public class ClientOperationsImpl extends AbstractMnuboOperations implements ClientOperations {

    public ClientOperationsImpl(MnuboConnectionManager mnuboConnectionManager) {
        super(mnuboConnectionManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> createUser(User user) {
        final Task<Boolean> task = newCreateUserTask(mnuboConnectionManager.getCurrentConnection(), user);
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createUserAsync(final User user, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newCreateUserTask(mnuboConnectionManager.getCurrentConnection(), user);
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> confirmUserCreation(String username, UserConfirmation confirmation) {
        final Task<Boolean> task = newConfirmUserCreationTask(mnuboConnectionManager.getCurrentConnection(), username, confirmation);
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmUserCreationAsync(final String username, final UserConfirmation confirmation, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newConfirmUserCreationTask(mnuboConnectionManager.getCurrentConnection(), username, confirmation);
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> resetPassword(String username) {
        final Task<Boolean> task = newResetPasswordTask(mnuboConnectionManager.getCurrentConnection(), username);
        return task.executeSync();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPasswordAsync(final String username, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newResetPasswordTask(mnuboConnectionManager.getCurrentConnection(), username);
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> confirmPasswordReset(String username, ResetPassword resetPassword) {
        final Task<Boolean> task = newConfirmPasswordResetTask(mnuboConnectionManager.getCurrentConnection(), username, resetPassword);
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmPasswordResetAsync(final String username, final ResetPassword resetPassword, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newConfirmPasswordResetTask(mnuboConnectionManager.getCurrentConnection(), username, resetPassword);
        task.executeAsync(completionCallBack);
    }
}
