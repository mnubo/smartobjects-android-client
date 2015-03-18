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
 */package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationOperationsImplTest extends AbstractOperationsTest {

    private final AuthenticationOperationsImpl authenticationOperations = new AuthenticationOperationsImpl(mockedConnectionOperations, mockedClientApiConnection, mockedUserApiConnection);

    @SuppressWarnings("unchecked")
    private final MnuboApi.CompletionCallBack<Boolean> mockedCallback = mock(MnuboApi.CompletionCallBack.class);

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        authenticationOperations.setAsyncTaskFactory(mockedAsyncTaskFactory);
    }

    @Test
    public void logInAsyncTest() throws Exception {
        authenticationOperations.logIn("username", "password", null);

        verify(mockedConnectionOperations, only()).logIn("username", "password");
    }

    @Test
    public void logInWithCallbackTest() throws Exception {

        final String username = "username";
        final String password = "password";

        authenticationOperations.logIn(username, password, mockedCallback);
        verify(mockedAsyncTask, only()).execute();
    }

    @Test
    public void logOutAsyncTest() throws Exception {
        authenticationOperations.logOut();

        verify(mockedConnectionOperations, only()).logOut();
    }

    @Test
    public void isUserConnected() throws Exception {
        when(mockedConnectionOperations.isUserConnected()).thenReturn(false);
        Boolean isUserConnected = authenticationOperations.isUserConnected();
        assertFalse(isUserConnected);
        verify(mockedConnectionOperations, only()).isUserConnected();
    }

    @Test
    public void getUsernameTest() throws Exception {
        final String expectedUsername = "username";
        when(mockedConnectionOperations.getUsername()).thenReturn(expectedUsername);

        String username = authenticationOperations.getUsername();
        assertThat(username, equalTo(expectedUsername));

        verify(mockedConnectionOperations, only()).getUsername();
    }
}