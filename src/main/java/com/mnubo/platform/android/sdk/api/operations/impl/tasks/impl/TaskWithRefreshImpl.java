package com.mnubo.platform.android.sdk.api.operations.impl.tasks.impl;

import android.util.Log;

import com.mnubo.platform.android.sdk.api.operations.impl.MnuboResponse;
import com.mnubo.platform.android.sdk.api.operations.impl.tasks.Task;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;

import org.springframework.social.ExpiredAuthorizationException;

import static com.mnubo.platform.android.sdk.api.operations.impl.AbstractMnuboOperations.MnuboOperation;

public class TaskWithRefreshImpl<Result> implements Task<Result> {
    private final MnuboOperation<Result> operation;
    private final ConnectionRefresher connectionRefresher;

    public TaskWithRefreshImpl(MnuboOperation<Result> operation, final ConnectionRefresher connectionRefresher) {
        this.operation = operation;
        this.connectionRefresher = connectionRefresher;
    }

    @Override
    public MnuboResponse<Result> execute() {
        Result result = null;
        MnuboException error = null;

        try {
            try {
                result = operation.executeMnuboCall();

            } catch (ExpiredAuthorizationException eax) {

                connectionRefresher.refresh();

                result = operation.executeMnuboCall();
            }

        } catch (MnuboException ex) {
            error = ex;
            Log.getStackTraceString(ex);

        } catch (Exception ex) {
            error = new MnuboException(ex);
        }

        return new MnuboResponse<>(result, error);

    }

    public static interface ConnectionRefresher {
        void refresh();
    }
}
