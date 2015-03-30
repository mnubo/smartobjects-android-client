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

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.internal.services.UserService;
import com.mnubo.platform.android.sdk.internal.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.FindUserObjectsTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.GetUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdatePasswordTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdateUserTask;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService.FailedAttemptCallback;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class UserOperationsImplTest extends AbstractOperationsTest {

    private final UserOperationsImpl userOperations = new UserOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final UserService mockedUserService = mock(UserService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObjects> mockedSmartObjectsCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<User> mockedUserCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.userService()).thenReturn(mockedUserService);
    }

    @Test
    public void testFindUserObjects() throws Exception {
        when(AsyncTaskFactory.create(
                any(FindUserObjectsTask.class),
                isNull(CompletionCallBack.class),
                any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        userOperations.findUserObjects(username, null);

    }

    @Test
    public void testFindUserObjectsWithCallback() throws Exception {
        when(AsyncTaskFactory.create(
                any(FindUserObjectsTask.class),
                eq(mockedSmartObjectsCallback),
                any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        userOperations.findUserObjects(username, mockedSmartObjectsCallback);


    }

    @Test
    public void testFindUserObjectsDetails() throws Exception {
        when(AsyncTaskFactory.create(any(FindUserObjectsTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);


        final String username = "username";

        userOperations.findUserObjects(username, true, null);

    }

    @Test
    public void testFindUserObjectsDetailsWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(FindUserObjectsTask.class), eq(mockedSmartObjectsCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        userOperations.findUserObjects(username, true, mockedSmartObjectsCallback);

    }

    @Test
    public void testFindUserObjectsDetailsAndObjectModel() throws Exception {
        when(AsyncTaskFactory.create(any(FindUserObjectsTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final String objectModelName = "objectModelName";

        userOperations.findUserObjects(username, true, objectModelName, null);

    }

    @Test
    public void testFindUserObjectsDetailsAndObjectModelWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(FindUserObjectsTask.class), eq(mockedSmartObjectsCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final String objectModelName = "objectModelName";

        userOperations.findUserObjects(username, true, objectModelName, mockedSmartObjectsCallback);

    }

    @Test
    public void testGetUser() throws Exception {
        when(AsyncTaskFactory.create(any(UpdatePasswordTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        userOperations.getUser(username, null);

    }

    @Test
    public void testGetUserWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(GetUserTask.class), eq(mockedUserCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        userOperations.getUser(username, mockedUserCallback);

    }

    @Test
    public void testUpdateAsync() throws Exception {
        when(AsyncTaskFactory.create(any(UpdateUserTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final User updatedUser = new User();
        updatedUser.setUsername(username);

        userOperations.update(username, updatedUser, null);

    }

    @Test
    public void testUpdateAsyncWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(UpdateUserTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final User updatedUser = new User();
        updatedUser.setUsername(username);

        userOperations.update(username, updatedUser, mockedSuccessCallback);

    }

    @Test
    public void testUpdatePassword() throws Exception {
        when(AsyncTaskFactory.create(any(UpdatePasswordTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        userOperations.updatePassword(username, updatePassword, null);

    }

    @Test
    public void testUpdatePasswordWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(UpdatePasswordTask.class), eq(mockedSuccessCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        userOperations.updatePassword(username, updatePassword, mockedSuccessCallback);

    }
}