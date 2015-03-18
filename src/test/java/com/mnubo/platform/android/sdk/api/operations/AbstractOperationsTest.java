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
 */package com.mnubo.platform.android.sdk.api.operations;

import android.os.AsyncTask;

import com.mnubo.platform.android.sdk.api.operations.impl.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.junit.Before;
import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractOperationsTest {

    protected final ConnectionOperations mockedConnectionOperations = mock(ConnectionOperations.class);

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
