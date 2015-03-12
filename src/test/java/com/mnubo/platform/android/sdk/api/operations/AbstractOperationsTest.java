package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.api.operations.impl.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApiFactory.ConnectionOperations;
import static org.mockito.Mockito.mock;

public class AbstractOperationsTest {

    protected ConnectionOperations mockedConnectionOperations = mock(ConnectionOperations.class);

    @SuppressWarnings("unchecked")
    protected Connection<MnuboClientApi> mockedClientApiConnection = mock(Connection.class);
    @SuppressWarnings("unchecked")
    protected Connection<MnuboUserApi> mockedUserApiConnection = mock(Connection.class);

    protected MnuboUserApi mockedUserApi = mock(MnuboUserApi.class);
    protected MnuboClientApi mockedClientApi = mock(MnuboClientApi.class);

    protected AsyncTaskFactory mockedAsyncTaskFactory = mock(AsyncTaskFactory.class);
}
