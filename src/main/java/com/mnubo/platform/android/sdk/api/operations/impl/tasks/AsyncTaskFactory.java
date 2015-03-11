package com.mnubo.platform.android.sdk.api.operations.impl.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboCancelledOperationException;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;

public class AsyncTaskFactory {

    private final String operationTag;

    public AsyncTaskFactory(String operationTag) {
        this.operationTag = operationTag;
    }

    public <Result> AsyncTask<Void, Void, MnuboResponse<Result>> create(final Task<Result> task, final CompletionCallBack<Result> callback) {
        return new AsyncTask<Void, Void, MnuboResponse<Result>>() {
            @Override
            protected MnuboResponse<Result> doInBackground(Void... params) {
                return task.execute();
            }

            @Override
            protected void onPostExecute(MnuboResponse<Result> resultMnuboResponse) {
                super.onPostExecute(resultMnuboResponse);

                logIfError(resultMnuboResponse.getError());
                callback.onCompletion(resultMnuboResponse.getResult(), resultMnuboResponse.getError());
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();

                final MnuboCancelledOperationException mnuboCancelledOperationException = new MnuboCancelledOperationException();
                logIfError(mnuboCancelledOperationException);
                callback.onCompletion(null, mnuboCancelledOperationException);
            }
        };
    }


    private void logIfError(MnuboException ex) {
        if (ex != null) {
            Log.e(operationTag, Strings.EXCEPTION_SDK, ex);
        }
    }
}
