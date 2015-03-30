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

package com.mnubo.platform.android.sdk.internal.tasks.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;

import org.springframework.social.ExpiredAuthorizationException;

import static com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService.FailedAttemptCallback;

public abstract class TaskWithRefreshImpl<Result> extends Task<Result> {
    public final static String TASK_REFRESHING = "Refreshing token";
    private final ConnectionRefresher connectionRefresher;

    public TaskWithRefreshImpl(ApiFetcher apiFetcher, ConnectionRefresher connectionRefresher) {
        super(apiFetcher);
        this.connectionRefresher = connectionRefresher;
    }

    @Override
    public MnuboResponse<Result> executeSync(FailedAttemptCallback failedAttemptCallback) {
        Result result = null;
        MnuboException error = null;

        try {
            try {
                result = executeMnuboCall();

            } catch (ExpiredAuthorizationException eax) {
                Log.d(this.getClass().getName(), TASK_REFRESHING, eax);

                connectionRefresher.refresh();

                result = executeMnuboCall();
            }

        } catch (MnuboException ex) {
            error = ex;

        } catch (Exception ex) {
            error = new MnuboException(ex);
        }

        handleError(error, failedAttemptCallback);

        return new MnuboResponse<>(result, error);

    }


    public static interface ConnectionRefresher {
        void refresh();
    }
}
