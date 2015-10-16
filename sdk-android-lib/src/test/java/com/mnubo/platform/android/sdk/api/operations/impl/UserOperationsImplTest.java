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
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.GetUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdatePasswordTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.user.UpdateUserTask;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class UserOperationsImplTest extends AbstractOperationsTest {

    private final UserOperationsImpl userOperations = new UserOperationsImpl(mockedConnectionManager, mockedMnuboBufferService);
    private final UserService mockedUserService = mock(UserService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<User> mockedUserCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(mockedMnuboSDKApi.userService()).thenReturn(mockedUserService);
    }

    @Test
    public void testSyncGetUser() throws Exception {

        final String username = "username";
        final User expectedResult = new User();

        final GetUserTask mockedTask = mock(GetUserTask.class);
        when(mockedTask.executeSync(eq(mockedConnectionManager))).thenReturn(new MnuboResponse<>(expectedResult, null));
        when(TaskFactory.newGetUserTask(eq(username))).thenReturn(mockedTask);

        final User result = userOperations.getUser(username).getResult();

        assertEquals(expectedResult, result);
        verify(mockedTask, only()).executeSync(eq(mockedConnectionManager));

    }

    @Test
    public void testAsyncGetUser() throws Exception {
        final String username = "username";

        final GetUserTask mockedTask = mock(GetUserTask.class);
        when(TaskFactory.newGetUserTask(eq(username))).thenReturn(mockedTask);

        userOperations.getUserAsync(username, null);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), isNull(CompletionCallBack.class));

    }

    @Test
    public void testGetUserWithCallback() throws Exception {
        final String username = "username";

        final GetUserTask mockedTask = mock(GetUserTask.class);
        when(TaskFactory.newGetUserTask(eq(username))).thenReturn(mockedTask);

        userOperations.getUserAsync(username, mockedUserCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), eq(mockedUserCallback));

    }

    @Test
    public void testUpdateSync() throws Exception {
        final String username = "username";
        final User updatedUser = new User();

        final UpdateUserTask mockedTask = mock(UpdateUserTask.class);
        when(mockedTask.executeSync(eq(mockedConnectionManager))).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newUpdateUserTask(eq(username), eq(updatedUser))).thenReturn(mockedTask);

        final Boolean result = userOperations.update(username, updatedUser).getResult();

        assertTrue(result);
        verify(mockedTask, only()).executeSync(eq(mockedConnectionManager));

    }

    @Test
    public void testUpdateAsync() throws Exception {

        final String username = "username";
        final User updatedUser = new User();

        final UpdateUserTask mockedTask = mock(UpdateUserTask.class);
        when(TaskFactory.newUpdateUserTask(eq(username), eq(updatedUser))).thenReturn(mockedTask);

        userOperations.updateAsync(username, updatedUser, null);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), isNull(CompletionCallBack.class));

    }

    @Test
    public void testUpdateAsyncWithCallback() throws Exception {

        final String username = "username";
        final User updatedUser = new User();

        final UpdateUserTask mockedTask = mock(UpdateUserTask.class);
        when(TaskFactory.newUpdateUserTask(eq(username), eq(updatedUser))).thenReturn(mockedTask);

        userOperations.updateAsync(username, updatedUser, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncUpdatePassword() throws Exception {
        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        final UpdatePasswordTask mockedTask = mock(UpdatePasswordTask.class);
        when(mockedTask.executeSync(eq(mockedConnectionManager))).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newUpdatePasswordTask(eq(username), eq(updatePassword))).thenReturn(mockedTask);

        final Boolean result = userOperations.updatePassword(username, updatePassword).getResult();

        assertTrue(result);
        verify(mockedTask, only()).executeSync(eq(mockedConnectionManager));

    }

    @Test
    public void testAsyncUpdatePassword() throws Exception {
        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        final UpdatePasswordTask mockedTask = mock(UpdatePasswordTask.class);
        when(TaskFactory.newUpdatePasswordTask(eq(username), eq(updatePassword))).thenReturn(mockedTask);

        userOperations.updatePasswordAsync(username, updatePassword, null);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), isNull(CompletionCallBack.class));

    }

    @Test
    public void testUpdatePasswordWithCallback() throws Exception {
        final String username = "username";
        final UpdatePassword updatePassword = new UpdatePassword("old", "new", "new");

        final UpdatePasswordTask mockedTask = mock(UpdatePasswordTask.class);
        when(TaskFactory.newUpdatePasswordTask(eq(username), eq(updatePassword))).thenReturn(mockedTask);

        userOperations.updatePasswordAsync(username, updatePassword, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), eq(mockedSuccessCallback));

    }
}