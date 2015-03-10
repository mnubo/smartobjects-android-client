package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.MnuboApi;
import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class AuthenticationOperationsImplTest extends AbstractOperationsTest {

    private AuthenticationOperations authenticationOperations = new AuthenticationOperationsImpl(connectionOperations, clientApiConnection, userApiConnection);

    @Test
    public void logInAsyncTest() throws Exception {
        authenticationOperations.logIn("username", "password", null);

        verify(connectionOperations, only()).logIn("username", "password");
    }
    @Test
    public void logInWithCallbackTest() throws Exception {
        final MnuboApi.CompletionCallBack<Boolean> callback = new MnuboApi.CompletionCallBack<Boolean>() {
            @Override
            public void onCompletion(Boolean success, MnuboException error) {
                assertThat(success, equalTo(true));
            }
        };

        authenticationOperations.logIn("username", "password", callback);

        verify(connectionOperations, only()).logIn("username", "password");
    }
}