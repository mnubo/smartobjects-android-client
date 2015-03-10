package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;

import org.junit.Test;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class AuthenticationOperationsImplTest extends AbstractOperationsTest {

    private AuthenticationOperations authenticationOperations = new AuthenticationOperationsImpl(connectionOperations, clientApiConnection, userApiConnection);

    @Test
    public void logInTest() throws Exception {
        authenticationOperations.logIn("username", "password", null);

        verify(connectionOperations, only()).logIn("username", "password");
    }
}