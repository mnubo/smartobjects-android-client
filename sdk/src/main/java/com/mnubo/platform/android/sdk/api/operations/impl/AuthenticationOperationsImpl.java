package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class AuthenticationOperationsImpl extends AbstractMnuboOperations implements AuthenticationOperations {

    private final static String ACTIVITY_TAG = AuthenticationOperationsImpl.class.getName();

    public AuthenticationOperationsImpl(MnuboApiFactory.ConnectionOperations connectionOperations,
                                        Connection<MnuboClientApi> clientConnection,
                                        Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    @Override
    public void logIn(final String username, final String password, final CompletionCallBack<Boolean> completionCallBack) {
        executeAsync(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                return connectionOperations.logIn(username, password);
            }
        }, completionCallBack);
    }

    @Override
    public void logOut() {
        connectionOperations.logOut();
    }

    @Override
    public Boolean isUserConnected() {
        return connectionOperations.isUserConnected();
    }

    @Override
    public String getUsername() {
        return connectionOperations.getUsername();
    }

    @Override
    public String getActivityTag() {
        return ACTIVITY_TAG;
    }
}
