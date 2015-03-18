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
 */package com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl;

import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import org.junit.Test;
import org.springframework.social.ExpiredAuthorizationException;

import static com.mnubo.platform.android.sdk.api.operations.impl.AbstractMnuboOperations.MnuboOperation;
import static com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TaskWithRefreshImplTest {

    private class MockedCustomRefresher {
        private Boolean expiredConnection = true;
        private final Boolean willRefreshWorks;
        private final ConnectionRefresher connectionRefresher;

        private MockedCustomRefresher(final Boolean willRefreshWorks) {
            this.willRefreshWorks = willRefreshWorks;
            this.connectionRefresher = new ConnectionRefresher() {
                @Override
                public void refresh() {
                    if (willRefreshWorks) {
                        expiredConnection = false;
                    }
                }
            };
        }

        public ConnectionRefresher getConnectionRefresher() {
            return connectionRefresher;
        }
    }

    @Test
    public void testExecute() throws Exception {

        final Task<Boolean> task = new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                return true;
            }
        }, null);

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), equalTo(true));
        assertThat(response.getError(), is(nullValue()));
    }

    @Test
    public void testExecuteExpiringWithRefreshWorking() throws Exception {

        final MockedCustomRefresher mockedCustomRefresher = new MockedCustomRefresher(true);

        final Task<Boolean> task = new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                if (mockedCustomRefresher.expiredConnection) {
                    throw new ExpiredAuthorizationException("expired");
                }
                return true;
            }
        }, mockedCustomRefresher.getConnectionRefresher());

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), equalTo(true));
        assertThat(response.getError(), is(nullValue()));
    }

    @Test
    public void testExecuteExpiringWithRefreshNotWorking() throws Exception {

        final MockedCustomRefresher mockedCustomRefresher = new MockedCustomRefresher(false);

        final Task<Boolean> task = new TaskWithRefreshImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                if (mockedCustomRefresher.expiredConnection) {
                    throw new ExpiredAuthorizationException("expired");
                }
                return true;
            }
        }, mockedCustomRefresher.getConnectionRefresher());

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), is(nullValue()));
        assertThat(response.getError(), is(any(MnuboException.class)));
    }
}