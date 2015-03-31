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
import com.mnubo.platform.android.sdk.internal.services.ClientService;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmPasswordResetTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ConfirmUserCreationTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.CreateUserTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.client.ResetPasswordTask;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class ClientOperationsImplTest extends AbstractOperationsTest {

    private final ClientOperationsImpl clientOperations = new ClientOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final ClientService mockedClientService = mock(ClientService.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(mockedClientApiConnection.getApi()).thenReturn(mockedClientApi);
        when(mockedClientApi.clientService()).thenReturn(mockedClientService);
    }

    @Test
    public void testSyncCreateUser() throws Exception {
        final User createdUser = new User();
        final CreateUserTask mockedTask = mock(CreateUserTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newCreateUserTask(any(Task.ApiFetcher.class), eq(createdUser), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final Boolean result = clientOperations.createUser(createdUser).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncCreateUser() throws Exception {
        final User createdUser = new User();

        final CreateUserTask mockedTask = mock(CreateUserTask.class);
        when(TaskFactory.newCreateUserTask(any(Task.ApiFetcher.class), eq(createdUser), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.createUserAsync(createdUser, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncCreateUserWithCallback() throws Exception {
        final User createdUser = new User();

        final CreateUserTask mockedTask = mock(CreateUserTask.class);
        when(TaskFactory.newCreateUserTask(any(Task.ApiFetcher.class), eq(createdUser), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.createUserAsync(createdUser, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncConfirmUserCreation() throws Exception {
        final UserConfirmation userConfirmation = new UserConfirmation("token", "password");
        final String username = "username";

        final ConfirmUserCreationTask mockedTask = mock(ConfirmUserCreationTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newConfirmUserCreationTask(any(Task.ApiFetcher.class), eq(username), eq(userConfirmation), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final Boolean result = clientOperations.confirmUserCreation(username, userConfirmation).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncConfirmUserCreation() throws Exception {
        final UserConfirmation userConfirmation = new UserConfirmation("token", "password");
        final String username = "username";

        final ConfirmUserCreationTask mockedTask = mock(ConfirmUserCreationTask.class);
        when(TaskFactory.newConfirmUserCreationTask(any(Task.ApiFetcher.class), eq(username), eq(userConfirmation), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.confirmUserCreationAsync(username, userConfirmation, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncConfirmUserCreationWithCallback() throws Exception {
        final UserConfirmation userConfirmation = new UserConfirmation("token", "password");
        final String username = "username";

        final ConfirmUserCreationTask mockedTask = mock(ConfirmUserCreationTask.class);
        when(TaskFactory.newConfirmUserCreationTask(any(Task.ApiFetcher.class), eq(username), eq(userConfirmation), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.confirmUserCreationAsync(username, userConfirmation, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncResetPassword() throws Exception {
        final String username = "username";

        final ResetPasswordTask mockedTask = mock(ResetPasswordTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newResetPasswordTask(any(Task.ApiFetcher.class), eq(username), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final Boolean result = clientOperations.resetPassword(username).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();

    }

    @Test
    public void testAsyncResetPassword() throws Exception {
        final String username = "username";

        final ResetPasswordTask mockedTask = mock(ResetPasswordTask.class);
        when(TaskFactory.newResetPasswordTask(any(Task.ApiFetcher.class), eq(username), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.resetPasswordAsync(username, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncResetPasswordWithCallback() throws Exception {

        final String username = "username";

        final ResetPasswordTask mockedTask = mock(ResetPasswordTask.class);
        when(TaskFactory.newResetPasswordTask(any(Task.ApiFetcher.class), eq(username), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.resetPasswordAsync(username, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncConfirmPasswordReset() throws Exception {
        final String username = "username";
        final ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        final ConfirmPasswordResetTask mockedTask = mock(ConfirmPasswordResetTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newConfirmPasswordResetTask(any(Task.ApiFetcher.class), eq(username), eq(resetPassword), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final Boolean result = clientOperations.confirmPasswordReset(username, resetPassword).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncConfirmPasswordReset() throws Exception {
        final String username = "username";
        final ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        final ConfirmPasswordResetTask mockedTask = mock(ConfirmPasswordResetTask.class);
        when(TaskFactory.newConfirmPasswordResetTask(any(Task.ApiFetcher.class), eq(username), eq(resetPassword), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.confirmPasswordResetAsync(username, resetPassword, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncConfirmPasswordResetWithCallback() throws Exception {

        final String username = "username";
        final ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        final ConfirmPasswordResetTask mockedTask = mock(ConfirmPasswordResetTask.class);
        when(TaskFactory.newConfirmPasswordResetTask(any(Task.ApiFetcher.class), eq(username), eq(resetPassword), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        clientOperations.confirmPasswordResetAsync(username, resetPassword, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }
}