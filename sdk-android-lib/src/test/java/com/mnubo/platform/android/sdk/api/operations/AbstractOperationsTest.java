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
package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService;
import com.mnubo.platform.android.sdk.internal.api.MnuboSDKApi;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.services.buffer.MnuboBufferService.FailedAttemptCallback;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        TaskFactory.class
})
public abstract class AbstractOperationsTest {

    protected final MnuboConnectionManager mockedConnectionManager = mock(MnuboConnectionManager.class);
    protected final MnuboBufferService mockedMnuboBufferService = mock(MnuboBufferService.class);
    protected final FailedAttemptCallback mockeFailedAttemptCallback = mock(FailedAttemptCallback.class);

    protected final RefreshableConnection mockedRefreshableConnection = mock(RefreshableConnection.class);

    protected final MnuboSDKApi mockedMnuboSDKApi = mock(MnuboSDKApi.class);

    @SuppressWarnings("unchecked")
    protected final CompletionCallBack<Boolean> mockedSuccessCallback = mock(CompletionCallBack.class);


    @Before
    public void setUp() throws Exception {
        mockStatic(TaskFactory.class);
        when(mockedConnectionManager.getCurrentConnection()).thenReturn(mockedRefreshableConnection);
        when(mockedMnuboBufferService.getFailedAttemptCallback()).thenReturn(mockeFailedAttemptCallback);
    }
}
