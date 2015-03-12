package com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl;

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
                if(mockedCustomRefresher.expiredConnection){
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
                if(mockedCustomRefresher.expiredConnection){
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