package com.mnubo.platform.android.sdk.api.operations.impl;

import android.os.AsyncTask;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.models.users.User;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientOperationsImplTest extends AbstractOperationsTest {

    private final ClientOperationsImpl clientOperations = new ClientOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);

    @SuppressWarnings("unchecked")
    AsyncTask<Void, Void, Boolean> mockedAsyncTask = mock(AsyncTask.class);

    @SuppressWarnings("unchecked")
    final MnuboApi.CompletionCallBack<Boolean> callback = mock(MnuboApi.CompletionCallBack.class);

    public void testCreateUser() throws Exception {

        when(mockedAsyncTaskFactory.create(any(Task.class), any(MnuboApi.CompletionCallBack.class)))
                .thenReturn(mockedAsyncTask);

        final User createdUser = new User();
        createdUser.setUsername("username");

        clientOperations.createUser(createdUser, callback);
        verify(mockedAsyncTask, only()).execute();
    }

    public void testConfirmUserCreation() throws Exception {

    }

    public void testResetPassword() throws Exception {

    }

    public void testConfirmPasswordReset() throws Exception {

    }
}