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
import com.mnubo.platform.android.sdk.internal.tasks.AsyncTaskFactory;
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
import static com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService.FailedAttemptCallback;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class ClientOperationsImplTest extends AbstractOperationsTest {

    private final ClientOperationsImpl clientOperations = new ClientOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);
    private final ClientService mockedClientService = mock(ClientService.class);
    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedCallback = mock(CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(mockedClientApiConnection.getApi()).thenReturn(mockedClientApi);
        when(mockedClientApi.clientService()).thenReturn(mockedClientService);
    }

    @Test
    public void createUser() throws Exception {
        when(AsyncTaskFactory.create(any(CreateUserTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final User createdUser = new User();
        createdUser.setUsername("username");

        clientOperations.createUser(createdUser, null);
    }

    @Test
    public void createUserWithCallback() throws Exception {

        when(AsyncTaskFactory.create(any(CreateUserTask.class), eq(mockedCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final User createdUser = new User();
        createdUser.setUsername("username");

        clientOperations.createUser(createdUser, mockedCallback);

    }

    @Test
    public void confirmUserCreation() throws Exception {

        when(AsyncTaskFactory.create(any(ConfirmUserCreationTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final UserConfirmation userConfirmation = new UserConfirmation("token", "password");
        final String username = "username";

        clientOperations.confirmUserCreation(username, userConfirmation, null);
    }

    @Test
    public void confirmUserCreationWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(ConfirmUserCreationTask.class), eq(mockedCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final UserConfirmation userConfirmation = new UserConfirmation("token", "password");

        clientOperations.confirmUserCreation("username", userConfirmation, mockedCallback);
    }

    @Test
    public void testResetPassword() throws Exception {
        when(AsyncTaskFactory.create(any(ResetPasswordTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        clientOperations.resetPassword(username, null);

    }

    @Test
    public void testResetPasswordWithCallback() throws Exception {

        when(AsyncTaskFactory.create(any(ResetPasswordTask.class), eq(mockedCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";

        clientOperations.resetPassword(username, mockedCallback);

    }

    @Test
    public void testConfirmPasswordReset() throws Exception {
        when(AsyncTaskFactory.create(any(ConfirmPasswordResetTask.class), isNull(CompletionCallBack.class), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        clientOperations.confirmPasswordReset(username, resetPassword, null);
    }

    @Test
    public void testConfirmPasswordResetWithCallback() throws Exception {
        when(AsyncTaskFactory.create(any(ConfirmPasswordResetTask.class), eq(mockedCallback), any(FailedAttemptCallback.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final ResetPassword resetPassword = new ResetPassword("token", "password", "password");

        clientOperations.confirmPasswordReset(username, resetPassword, mockedCallback);
    }
}