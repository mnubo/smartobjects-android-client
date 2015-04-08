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

package com.mnubo.platform.android.sdk.api;

import com.mnubo.platform.android.sdk.api.operations.AuthenticationOperations;
import com.mnubo.platform.android.sdk.api.operations.ClientOperations;
import com.mnubo.platform.android.sdk.api.operations.SmartObjectOperations;
import com.mnubo.platform.android.sdk.api.operations.UserOperations;
import com.mnubo.platform.android.sdk.api.operations.impl.AuthenticationOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.ClientOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.SmartObjectOperationsImpl;
import com.mnubo.platform.android.sdk.api.operations.impl.UserOperationsImpl;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;

import java.io.File;

/**
 * Mnubo API that is used by the application developers to perform requests on the Mnubo system.
 */
public class MnuboApi {

    private final UserOperations userOperations;

    private final SmartObjectOperations smartObjectOperations;

    private final ClientOperations clientOperations;

    private final AuthenticationOperations authenticationOperations;

    public MnuboApi(MnuboConnectionManager connectionManager,
                    File applicationRootDir,
                    boolean enableFailedAttemptCaching) {

        this.userOperations = new UserOperationsImpl(connectionManager);
        this.smartObjectOperations = new SmartObjectOperationsImpl(connectionManager, applicationRootDir, enableFailedAttemptCaching);
        this.clientOperations = new ClientOperationsImpl(connectionManager);
        this.authenticationOperations = new AuthenticationOperationsImpl(connectionManager);

    }

    public UserOperations getUserOperations() {
        return userOperations;
    }

    public SmartObjectOperations getSmartObjectOperations() {
        return smartObjectOperations;
    }

    public ClientOperations getClientOperations() {
        return clientOperations;
    }

    public AuthenticationOperations getAuthenticationOperations() {
        return authenticationOperations;
    }

    /**
     * Interface definition for a callback to be invoked when an action an request to mnubo API as
     * returned, successful or not.
     *
     * @param <Result> The type of result that will be sent in the {link #onCompletion} method
     */
    public static interface CompletionCallBack<Result> {

        /**
         * This method is called when the request is done, success or not. The user must ensure the
         * result is not null and that no error arose when performing the request
         *
         * @param result the result of the async request
         * @param error  any error that might have occurred during the async request
         */
        void onCompletion(Result result, MnuboException error);

    }
}
