package com.mnubo.platform.android.sdk.api.operations.impl;

import android.os.AsyncTask;
import android.util.Log;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboCancelledOperationException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboClientConnectionUnavailableException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.MnuboApiFactory.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;

public abstract class AbstractMnuboOperations {

    private Connection<MnuboClientApi> clientConnection;
    private Connection<MnuboUserApi> userConnection;

    private final AsyncTaskFactory asyncTaskFactory;
    protected final ConnectionOperations connectionOperations;

    protected AbstractMnuboOperations(ConnectionOperations connectionOperations,
                                      Connection<MnuboClientApi> clientConnection,
                                      Connection<MnuboUserApi> userConnection) {
        this.connectionOperations = connectionOperations;
        this.userConnection = userConnection;
        this.clientConnection = clientConnection;
        this.asyncTaskFactory = new AsyncTaskFactory(getOperationTag());
    }

    public ConnectionRefresher getConnectionRefresher() {
        return new ConnectionRefresher() {
            @Override
            public void refresh() {
                userConnection = connectionOperations.refresh(userConnection);
            }
        };
    }

    public MnuboUserApi getUserApi() {
        if (this.userConnection != null) {
            return this.userConnection.getApi();
        }
        throw new MnuboNotLoggedInException();
    }

    public MnuboClientApi getClientApi() {
        if (this.clientConnection != null) {
            return this.clientConnection.getApi();
        }
        this.clientConnection = connectionOperations.getClientConnection();

        if (this.clientConnection != null) {
            return this.clientConnection.getApi();
        }
        throw new MnuboClientConnectionUnavailableException();
    }

    protected void execute(final Task task) {
        task.execute();
    }

    protected <Result> void execute(final Task<Result> task, final CompletionCallBack<Result> callback) {
        this.asyncTaskFactory.create(task, callback);
    }

    abstract String getOperationTag();

    private void logIfError(MnuboException ex) {
        if (ex != null) {
            Log.e(getOperationTag(), Strings.EXCEPTION_SDK, ex);
        }
    }

    public interface MnuboOperation<Result> {
        Result executeMnuboCall();
    }
}
