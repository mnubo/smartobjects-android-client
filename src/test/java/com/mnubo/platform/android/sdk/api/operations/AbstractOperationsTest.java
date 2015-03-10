package com.mnubo.platform.android.sdk.api.operations;

import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApiFactory.ConnectionOperations;
import static org.mockito.Mockito.mock;

public class AbstractOperationsTest {

    protected ConnectionOperations connectionOperations = mock(ConnectionOperations.class);
    @SuppressWarnings("unchecked")
    protected Connection<MnuboClientApi> clientApiConnection = mock(Connection.class);
    @SuppressWarnings("unchecked")
    protected Connection<MnuboUserApi> userApiConnection = mock(Connection.class);
}
