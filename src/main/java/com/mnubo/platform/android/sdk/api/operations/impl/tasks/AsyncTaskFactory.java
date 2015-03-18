/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

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
