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

package com.mnubo.platform.android.sdk.api.operations.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.AsyncTaskFactory;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboClientConnectionUnavailableException;
import com.mnubo.platform.android.sdk.exceptions.sdk.MnuboNotLoggedInException;
import com.mnubo.platform.android.sdk.internal.client.api.MnuboClientApi;
import com.mnubo.platform.android.sdk.internal.user.api.MnuboUserApi;

import org.springframework.social.connect.Connection;

import static com.mnubo.platform.android.sdk.Mnubo.ConnectionOperations;
import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl.TaskWithRefreshImpl.ConnectionRefresher;

public abstract class AbstractMnuboOperations {

    private Connection<MnuboClientApi> clientConnection;
    private Connection<MnuboUserApi> userConnection;

    private AsyncTaskFactory asyncTaskFactory;
    final ConnectionOperations connectionOperations;

    AbstractMnuboOperations(ConnectionOperations connectionOperations,
                            Connection<MnuboClientApi> clientConnection,
                            Connection<MnuboUserApi> userConnection) {
        this.connectionOperations = connectionOperations;
        this.userConnection = userConnection;
        this.clientConnection = clientConnection;
        this.asyncTaskFactory = new AsyncTaskFactory(getOperationTag());
    }

    public void setAsyncTaskFactory(AsyncTaskFactory asyncTaskFactory) {
        this.asyncTaskFactory = asyncTaskFactory;
    }

    ConnectionRefresher getUserConnectionRefresher() {
        return new ConnectionRefresher() {
            @Override
            public void refresh() {
                userConnection = connectionOperations.refreshUserConnection(userConnection);
            }
        };
    }

    ConnectionRefresher getClientConnectionRefresher() {
        return new ConnectionRefresher() {
            @Override
            public void refresh() {
                clientConnection = connectionOperations.getNewClientConnection();
            }
        };
    }

    MnuboUserApi getUserApi() {
        if (this.userConnection != null) {
            return this.userConnection.getApi();
        }
        throw new MnuboNotLoggedInException();
    }

    MnuboClientApi getClientApi() {
        if (this.clientConnection != null) {
            return this.clientConnection.getApi();
        }
        this.clientConnection = connectionOperations.getNewClientConnection();

        if (this.clientConnection != null) {
            return this.clientConnection.getApi();
        }
        throw new MnuboClientConnectionUnavailableException();
    }

    void execute(final Task task) {
        MnuboResponse response = task.execute();
        handleError(response.getError());
    }

    <Result> void execute(final Task<Result> task, final CompletionCallBack<Result> callback) {
        if (callback == null) {
            execute(task);
        } else {
            this.asyncTaskFactory.create(task, callback).execute();
        }
    }

    abstract String getOperationTag();

    private void handleError(MnuboException ex) {
        if (ex != null) {
            Log.e(getOperationTag(), Strings.EXCEPTION_SDK, ex);
            throw ex;
        }
    }

    public interface MnuboOperation<Result> {
        Result executeMnuboCall();
    }
}
