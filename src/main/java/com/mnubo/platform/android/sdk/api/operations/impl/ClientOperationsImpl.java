package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.api.operations.ClientOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.security.ResetPassword;
import com.mnubo.platform.android.sdk.models.security.UserConfirmation;
import com.mnubo.platform.android.sdk.models.users.User;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class ClientOperationsImpl extends AbstractMnuboOperations implements ClientOperations {

    private final static String ACTIVITY_TAG = ClientOperationsImpl.class.getName();


    public ClientOperationsImpl(MnuboApiFactory.ConnectionOperations connectionOperations,
                                Connection<MnuboClientApi> clientConnection,
                                Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    @Override
    public void createUser(final User user, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getClientApi().clientSdkOperations().createUser(user);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public void confirmUserCreation(final String username, final UserConfirmation confirmation, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getClientApi().clientSdkOperations().confirmUserCreation(username, confirmation);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public void resetPassword(final String username, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getClientApi().clientSdkOperations().resetPassword(username);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public void confirmPasswordReset(final String username, final ResetPassword resetPassword, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getClientApi().clientSdkOperations().confirmPasswordReset(username, resetPassword);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public String getActivityTag() {
        return ACTIVITY_TAG;
    }
}
