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
package com.mnubo.platform.android.sdk.internal.tasks.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.social.ExpiredAuthorizationException;

import static com.mnubo.platform.android.sdk.internal.tasks.Task.ERROR_EXECUTING;
import static com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl.TASK_REFRESHING;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Log.class
})
public class TaskWithRefreshImplTest {

    private RefreshableConnection mockedRefreshableConnection = mock(RefreshableConnection.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Log.class);

    }

    @Test
    public void testExecute() throws Exception {

        final Task<Boolean> task = new DummyMockedTask(mockedRefreshableConnection, false);

        MnuboResponse<Boolean> response = task.executeSync();
        assertThat(response.getResult(), equalTo(true));
        assertThat(response.getError(), is(nullValue()));
    }

    @Test
    public void testExecuteExpiringWithRefreshWorking() throws Exception {
        when(Log.d(eq(DummyMockedTask.class.getName()), eq(TASK_REFRESHING), Matchers.any(ExpiredAuthorizationException.class))).thenReturn(0);

        final Task<Boolean> task = new DummyMockedTask(mockedRefreshableConnection, true);

        MnuboResponse<Boolean> response = task.executeSync();
        assertThat(response.getResult(), equalTo(true));
        assertThat(response.getError(), is(nullValue()));
    }

    @Test
    public void testExecuteExpiringWithRefreshNotWorking() throws Exception {
        when(Log.e(eq(DummyMockedTask.class.getName()), eq(ERROR_EXECUTING), Matchers.any(MnuboException.class))).thenReturn(0);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new MnuboExpiredAccessException();
            }
        }).when(mockedRefreshableConnection).refresh();

        final Task<Boolean> task = new DummyMockedTask(mockedRefreshableConnection, true);

        MnuboResponse<Boolean> response = task.executeSync();
        assertThat(response.getResult(), is(nullValue()));
        assertThat(response.getError(), is(any(MnuboException.class)));
    }

    private class DummyMockedTask extends TaskWithRefreshImpl<Boolean> {
        final RefreshableConnection mockedRefreshableConnection;
        boolean expiredConnection = false;

        private DummyMockedTask(RefreshableConnection mockedRefreshableConnection, boolean expiredConnection) {
            super(mockedRefreshableConnection);
            this.expiredConnection = expiredConnection;
            this.mockedRefreshableConnection = mockedRefreshableConnection;
        }

        @Override
        protected Boolean executeMnuboCall() {
            if (this.expiredConnection) {
                this.expiredConnection = false;
                throw new ExpiredAuthorizationException("expired");
            }
            return true;
        }
    }
}