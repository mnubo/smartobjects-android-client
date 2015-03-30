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

package com.mnubo.platform.android.sdk.api.services.cache;

import com.mnubo.platform.android.sdk.internal.tasks.Task;

/**
 * Interface that wraps query execution
 */
public interface MnuboFileCachingService {

    /**
     * This will try to send any data that was previously persisted to the device.
     * This will use the current thread for networking. Make sure you call this from a
     * thread that is not the Main/UI thread.
     */
    public void retryFailedAttempts();

    /**
     * Gets the count of failed attempts that was written to the device
     *
     * @return the count of failed attempts in the store
     */
    public int getFailedAttemptsCount();

    /**
     * Callback used when a failure occurs.
     */
    public static interface FailedAttemptCallback {

        /**
         * This method is called when the request fails. The failed task is used to retrieve data and
         * any information that could be used to perform a retry later.
         * @param failedTask is used to retrieve data
         */
        void onFailure(Task failedTask);

    }
}
