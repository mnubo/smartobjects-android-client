package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.api.operations.UserOperations;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;
import com.mnubo.platform.android.sdk.models.security.UpdatePassword;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObjects;
import com.mnubo.platform.android.sdk.models.users.User;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class UserOperationsImpl extends AbstractMnuboOperations implements UserOperations {

    private final static String ACTIVITY_TAG = UserOperationsImpl.class.getName();

    public UserOperationsImpl(MnuboApiFactory.ConnectionOperations connectionOperations,
                              Connection<MnuboClientApi> clientConnection,
                              Connection<MnuboUserApi> userConnection) {
        super(connectionOperations, clientConnection, userConnection);
    }

    @Override
    public void findUserObjects(final String username, final CompletionCallBack<SmartObjects> completionCallBack) {
        executeWithRefresh(new MnuboOperation<SmartObjects>() {
            @Override
            public SmartObjects executeMnuboCall() {
                return getUserApi().userService().findUserObjects(username);
            }
        }, completionCallBack);
    }

    @Override
    public void findUserObjects(final String username, final Boolean details, final CompletionCallBack<SmartObjects> completionCallBack) {
        executeWithRefresh(new MnuboOperation<SmartObjects>() {
            @Override
            public SmartObjects executeMnuboCall() {
                return getUserApi().userService().findUserObjects(username, details);
            }
        }, completionCallBack);
    }

    @Override
    public void findUserObjects(final String username, final Boolean details, final String objectModelName, final CompletionCallBack<SmartObjects> completionCallBack) {
        executeWithRefresh(new MnuboOperation<SmartObjects>() {
            @Override
            public SmartObjects executeMnuboCall() {
                return getUserApi().userService().findUserObjects(username, details, objectModelName);
            }
        }, completionCallBack);
    }

    @Override
    public void getUser(final String username, final CompletionCallBack<User> completionCallBack) {
        executeWithRefresh(new MnuboOperation<User>() {
            @Override
            public User executeMnuboCall() {
                return getUserApi().userService().getUser(username);
            }
        }, completionCallBack);
    }

    @Override
    public void update(final String username, final User updatedUser, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().userService().update(username, updatedUser);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public void updatePassword(final String username, final UpdatePassword newPassword, final CompletionCallBack<Boolean> completionCallBack) {
        executeWithRefresh(new MnuboOperation<Boolean>() {
            @Override
            public Boolean executeMnuboCall() {
                getUserApi().userService().updatePassword(username, newPassword);
                return true;
            }
        }, completionCallBack);
    }

    @Override
    public String getActivityTag() {
        return ACTIVITY_TAG;
    }
}
