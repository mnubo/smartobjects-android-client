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

import com.mnubo.platform.android.sdk.api.operations.UserOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newFindUserObjectsTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newGetUserTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newUpdatePasswordTask;
import static com.mnubo.platform.android.sdk.internal.tasks.TaskFactory.newUpdateUserTask;

public class UserOperationsImpl extends AbstractMnuboOperations implements UserOperations {

    public UserOperationsImpl(ConnectionOperations connectionOperations,
                              Connection<MnuboClientApi> clientConnection,
                              Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<SmartObjects> findUserObjects(String username, Boolean details, String objectModelName) {
        final Task<SmartObjects> task = newFindUserObjectsTask(getApiFetcher(), username, details, objectModelName, getUserConnectionRefresher());
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findUserObjectsAsync(final String username, final Boolean details, final String objectModelName, final CompletionCallBack<SmartObjects> completionCallBack) {
        final Task<SmartObjects> task = newFindUserObjectsTask(getApiFetcher(), username, details, objectModelName, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<User> getUser(String username) {
        final Task<User> task = newGetUserTask(getApiFetcher(), username, getUserConnectionRefresher());
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getUserAsync(final String username, final CompletionCallBack<User> completionCallBack) {
        final Task<User> task = newGetUserTask(getApiFetcher(), username, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> update(String username, User updatedUser) {
        final Task<Boolean> task = newUpdateUserTask(getApiFetcher(), username, updatedUser, getUserConnectionRefresher());
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsync(final String username, final User updatedUser, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newUpdateUserTask(getApiFetcher(), username, updatedUser, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MnuboResponse<Boolean> updatePassword(String username, UpdatePassword newPassword) {
        final Task<Boolean> task = newUpdatePasswordTask(getApiFetcher(), username, newPassword, getUserConnectionRefresher());
        return task.executeSync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePasswordAsync(final String username, final UpdatePassword newPassword, final CompletionCallBack<Boolean> completionCallBack) {
        final Task<Boolean> task = newUpdatePasswordTask(getApiFetcher(), username, newPassword, getUserConnectionRefresher());
        task.executeAsync(completionCallBack);
    }

}
