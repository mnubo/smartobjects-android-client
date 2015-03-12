package com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl;

import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.operations.impl.AbstractMnuboOperations.MnuboOperation;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TaskImplTest {

    @Test
    public void testExecute() throws Exception {
        final Task<Boolean> task = new TaskImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                return true;
            }
        });

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), equalTo(true));
        assertThat(response.getError(), is(nullValue()));
    }


    @Test
    public void testExecuteWithMnuboException() throws Exception {

        final MnuboException thrownException = new MnuboException("mnubo exception");

        final Task<Boolean> task = new TaskImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                throw thrownException;
            }
        });

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), is(nullValue()));
        assertThat(response.getError(), is(equalTo(thrownException)));
    }


    @Test
    public void testExecuteWithGenericException() throws Exception {

        final RuntimeException thrownException = new RuntimeException("Other kind of exception");

        final Task<Boolean> task = new TaskImpl<>(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                throw thrownException;
            }
        });

        MnuboResponse<Boolean> response = task.execute();
        assertThat(response.getResult(), is(nullValue()));
        assertThat(response.getError(), is(any(MnuboException.class)));
    }
}