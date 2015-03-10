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

    @Deprecated
    public void refresh() {
        userConnection = connectionOperations.refresh(userConnection);
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

    @Deprecated
    protected <Result> void execute(final MnuboOperation<Result> operation) {
        try {
            operation.executeMnuboCall();
        } catch (Exception ex) {
            Log.e(getOperationTag(), "An error has occurred while executing the request", ex);
            throw new MnuboException(ex);
        }
    }

    @Deprecated
    protected <Result> Result executeWithRefresh(final MnuboOperation<Result> operation) {
        try {
            return operation.executeMnuboCall();

        } catch (ExpiredAuthorizationException eax) {

            refresh();

            return operation.executeMnuboCall();
        }
    }

    @Deprecated
    protected <Result> void execute(final MnuboOperation<Result> operation, final CompletionCallBack<Result> callback) {
        if (callback == null) {
            execute(operation);
        } else {
            new AsyncTask<Void, Void, MnuboResponse<Result>>() {

                Result result = null;
                MnuboException error = null;

                @Override
                protected MnuboResponse<Result> doInBackground(Void... params) {
                    try {
                        result = operation.executeMnuboCall();

                    } catch (MnuboException ex) {
                        error = ex;

                    } catch (Exception ex) {
                        error = new MnuboException(ex);
                    }

                    return new MnuboResponse<>(result, error);
                }

                @Override
                protected void onPostExecute(MnuboResponse<Result> result) {
                    logIfError(result.getError());
                    callback.onCompletion(result.getResult(), result.getError());
                }
            }.execute();
        }
    }

    @Deprecated
    protected <Result> void executeWithRefresh(final MnuboOperation<Result> operation, final CompletionCallBack<Result> callback) {
        if (callback == null) {
            executeWithRefresh(operation);
        } else {

            new AsyncTask<Void, Void, MnuboResponse<Result>>() {

                Result result = null;
                MnuboException error = null;

                @Override
                protected MnuboResponse<Result> doInBackground(Void... params) {

                    try {
                        result = executeWithRefresh(operation);

                    } catch (MnuboException ex) {
                        error = ex;
                        Log.getStackTraceString(ex);

                    } catch (Exception ex) {
                        error = new MnuboException(ex);
                    }

                    return new MnuboResponse<>(result, error);
                }

                @Override
                protected void onPostExecute(MnuboResponse<Result> result) {
                    super.onPostExecute(result);
                    logIfError(result.getError());
                    callback.onCompletion(result.getResult(), result.getError());
                }


                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    error = new MnuboCancelledOperationException();
                    logIfError(error);
                    callback.onCompletion(result, error);
                }
            }.execute();
        }
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
