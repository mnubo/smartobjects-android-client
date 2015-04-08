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

import com.mnubo.platform.android.sdk.api.services.cache.MnuboFileCachingService;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;

public abstract class TaskImpl<Result> extends Task<Result> {

    public TaskImpl(RefreshableConnection refreshableConnection) {
        super(refreshableConnection);
    }

    @Override
    public MnuboResponse<Result> executeSync(MnuboFileCachingService.FailedAttemptCallback failedCallback) {


        Result result = null;
        MnuboException error = null;
        try {
            result = executeMnuboCall();

        } catch (MnuboException ex) {
            error = ex;

        } catch (Exception ex) {
            error = new MnuboException(ex);
        }

        handleError(error, failedCallback);

        return new MnuboResponse<>(result, error);

    }
}
