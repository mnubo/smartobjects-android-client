package com.mnubo.platform.android.sdk.api.operations.impl;

import android.os.AsyncTask;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationOperationsImplTest extends AbstractOperationsTest {

    private AuthenticationOperationsImpl authenticationOperations = new AuthenticationOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);


    @Before
    public void setUp() throws Exception {
        authenticationOperations.setAsyncTaskFactory(mockedAsyncTaskFactory);
    }

    @Test
    public void logInAsyncTest() throws Exception {
        authenticationOperations.logIn("username", "password", null);

        verify(mockedConnectionOperations, only()).logIn("username", "password");
    }

    @Test
    public void logInWithCallbackTest() throws Exception {

        @SuppressWarnings("unchecked")
        AsyncTask<Void, Void, Boolean> mockedAsyncTask = mock(AsyncTask.class);

        @SuppressWarnings("unchecked")
        final CompletionCallBack<Boolean> callback = mock(CompletionCallBack.class);

        when(mockedAsyncTaskFactory.create(any(Task.class), any(CompletionCallBack.class)))
                .thenReturn(mockedAsyncTask);

        final String username = "username";
        final String password = "password";

        authenticationOperations.logIn(username, password, callback);
        verify(mockedAsyncTask, only()).execute();
    }
}