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
package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.authentication.LogInTask;

import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class AuthenticationOperationsImplTest extends AbstractOperationsTest {

    private final AuthenticationOperationsImpl authenticationOperations = new AuthenticationOperationsImpl(mockedConnectionManager, mockedMnuboBufferService);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Boolean> mockedCallback = mock(CompletionCallBack.class);


    @Test
    public void logInSync() throws Exception {
        final String username = "username";
        final String password = "password";

        final LogInTask mockedTask = mock(LogInTask.class);
        when(mockedTask.executeSync(eq(mockedConnectionManager))).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newLogInTask(eq(username), eq(password))).thenReturn(mockedTask);

        Boolean success = authenticationOperations.logIn(username, password).getResult();

        verify(mockedTask, only()).executeSync(eq(mockedConnectionManager));
        assertTrue(success);
    }

    @Test
    public void logInAsync() throws Exception {
        final String username = "username";
        final String password = "password";

        final LogInTask mockedTask = mock(LogInTask.class);
        when(TaskFactory.newLogInTask(eq(username), eq(password))).thenReturn(mockedTask);

        authenticationOperations.logInAsync(username, password, null);

        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), isNull(CompletionCallBack.class));
    }

    @Test
    public void logInAsyncWithCallbackTest() throws Exception {
        final String username = "username";
        final String password = "password";

        final LogInTask mockedTask = mock(LogInTask.class);
        when(TaskFactory.newLogInTask(eq(username), eq(password))).thenReturn(mockedTask);

        authenticationOperations.logInAsync(username, password, mockedCallback);
        verify(mockedTask, only()).executeAsync(eq(mockedConnectionManager), eq(mockedCallback));
    }

    @Test
    public void logOutAsyncTest() throws Exception {
        authenticationOperations.logOut();

        verify(mockedConnectionManager, only()).logOut();
    }

    @Test
    public void isUserConnected() throws Exception {
        when(mockedConnectionManager.isUserConnected()).thenReturn(false);

        Boolean isUserConnected = authenticationOperations.isUserConnected();

        assertFalse(isUserConnected);
        verify(mockedConnectionManager, only()).isUserConnected();
    }

    @Test
    public void getUsernameTest() throws Exception {
        final String expectedUsername = "username";
        when(mockedConnectionManager.getUsername()).thenReturn(expectedUsername);

        String username = authenticationOperations.getUsername();
        assertThat(username, equalTo(expectedUsername));

        verify(mockedConnectionManager, only()).getUsername();
    }
}