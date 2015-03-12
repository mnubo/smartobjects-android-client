package com.mnubo.platform.android.sdk.api.operations;

import android.os.AsyncTask;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskImpl;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;

import org.junit.Before;
import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.MnuboApiFactory.ConnectionOperations;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractOperationsTest {

    protected ConnectionOperations mockedConnectionOperations = mock(ConnectionOperations.class);

    @SuppressWarnings("unchecked")
    protected final Connection<MnuboClientApi> mockedClientApiConnection = mock(Connection.class);
    @SuppressWarnings("unchecked")
    protected final Connection<MnuboUserApi> mockedUserApiConnection = mock(Connection.class);

    protected final MnuboUserApi mockedUserApi = mock(MnuboUserApi.class);
    protected final MnuboClientApi mockedClientApi = mock(MnuboClientApi.class);

    protected final AsyncTaskFactory mockedAsyncTaskFactory = mock(AsyncTaskFactory.class);

    @SuppressWarnings("unchecked")
    protected final AsyncTask<Void, Void, Boolean> mockedAsyncTask = mock(AsyncTask.class);


    @Before
    public void setUp() throws Exception {
        //if mockedCallback, then the request should be async, the task factory is required
        when(mockedAsyncTaskFactory.create(any(Task.class), any(CompletionCallBack.class)))
                .thenReturn(mockedAsyncTask);
    }
}
