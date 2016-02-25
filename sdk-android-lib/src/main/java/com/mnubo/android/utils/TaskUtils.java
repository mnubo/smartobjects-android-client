/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy
 * 	of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 * 	furnished to do so, subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in
 * 	all copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * 	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * 	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * 	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * 	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * 	THE SOFTWARE.
 */

package com.mnubo.android.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.exceptions.MnuboCancelledOperationException;
import com.mnubo.android.exceptions.MnuboNetworkException;

import java.util.concurrent.Callable;

import lombok.NonNull;
import lombok.Value;

public class TaskUtils {

    private final static String TAG = TaskUtils.class.getName();

    public static <T> void executeAsync(@NonNull final Callable<T> runnable, @NonNull final CompletionCallback<T> callback) {
        new AsyncTask<Void, Void, MnuboResponse<T>>() {
            @Override
            protected MnuboResponse<T> doInBackground(Void... params) {
                T result = null;
                MnuboException exception = null;
                try {
                    result = runnable.call();
                } catch (MnuboNetworkException e) {
                    Log.d(TAG, "AN NETWORK ERROR OCCURED, SAVING YOUR PAYLOAD", e);
                    exception = e;
                } catch (Exception e) {
                    Log.e(TAG, "An error occurred while executing async task", e);
                    exception = new MnuboException("An error occured while running the asynchronous task.", e);
                }

                return new MnuboResponse<>(result, exception);
            }

            @Override
            protected void onPostExecute(MnuboResponse<T> resultMnuboResponse) {
                super.onPostExecute(resultMnuboResponse);
                if (resultMnuboResponse.hasError()) {
                    callback.onFailure(resultMnuboResponse.getError());
                } else {
                    callback.onSuccess(resultMnuboResponse.getResult());
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                callback.onFailure(new MnuboCancelledOperationException());
            }
        }.execute();
    }

    @Value
    static class MnuboResponse<ResultType> {
        private final ResultType result;
        private final MnuboException error;

        public boolean hasError() {
            return error != null;
        }
    }
}
