package com.mnubo.platform.android.sdk.api.operations.impl;

import android.os.AsyncTask;
import android.util.Log;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.api.MnuboApiFactory;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboCancelledOperationException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboClientConnectionUnavailableException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public abstract class AbstractMnuboOperations {

    private Connection<MnuboClientApi> clientConnection;
    private Connection<MnuboUserApi> userConnection;

    protected final MnuboApiFactory.ConnectionOperations connectionOperations;

    protected AbstractMnuboOperations(MnuboApiFactory.ConnectionOperations connectionOperations,
                                      Connection<MnuboClientApi> clientConnection,
                                      Connection<MnuboUserApi> userConnection) {
        this.connectionOperations = connectionOperations;
        this.userConnection = userConnection;
        this.clientConnection = clientConnection;
    }

    public void refresh() {
        this.userConnection = connectionOperations.refresh(this.userConnection);
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

    protected <Result> void executeSync(final MnuboOperation<Result> operation) {
        try {
            executeWithRefresh(operation);
        } catch (Exception ex) {
            Log.e(getActivityTag(), "An error has occurred while executing the request", ex);
            throw new MnuboException(ex);
        }
    }

    protected <Result> Result executeWithRefresh(final MnuboOperation<Result> operation) {
        try {
            return operation.executeMnuboCall();

        } catch (ExpiredAuthorizationException eax) {

            refresh();

            return operation.executeMnuboCall();
        }
    }

    protected <Result> void executeAsync(final MnuboOperation<Result> operation, final CompletionCallBack<Result> callback) {
        if (callback == null) {
            executeSync(operation);
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

    protected <Result> void executeAsyncWithRefresh(final MnuboOperation<Result> operation, final CompletionCallBack<Result> callback) {
        if (callback == null) {
            executeSync(operation);
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

    abstract String getActivityTag();

    private void logIfError(MnuboException ex) {
        if (ex != null) {
            Log.e(getActivityTag(), Strings.EXCEPTION_SDK, ex);
        }
    }

    public interface MnuboOperation<Result> {
        Result executeMnuboCall();
    }
}
